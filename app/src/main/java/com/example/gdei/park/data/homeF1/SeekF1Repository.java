package com.example.gdei.park.data.homeF1;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.example.gdei.park.home.ParkMsg;
import com.example.gdei.park.home.fragment1.HistoryParkAdapter;
import com.example.gdei.park.home.fragment1.ParksList;
import com.example.gdei.park.home.fragment1.SeekF1ParkPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gdei on 2018/4/20.
 */

public class SeekF1Repository implements SeekF1Source , SeekF1Source.SeekF1Callback{
    private static final String TAG = "SeekF1Repository";
    private GeocodeSearch geocodeSearch;
    private GeocodeQuery geocodeQuery;
    private RouteSearch routeSearch;
    private static SeekF1Source INSTANCE;
    private List<ParkMsg> parks;                //停车场集合
    private ParkMsg park;
    private List<GeocodeAddress> addresses;     //地址解析出来的地址集合
    private LatLonPoint point = null;                  //封装经纬度的点
    private Bundle bundle;                      //用于返回封装经纬度的Bundle

    private LatLonPoint startPoint;
    private LatLonPoint endPoint;

    private List<String> parkNames = new ArrayList<>();     //用于记录反地址解析出来的地址名称
    private int index = 0;              //用于记录当ParkNames的指针

    private SeekF1Repository(Context context){
        parks = new ArrayList<>();
        geocodeSearch = new GeocodeSearch(context);
        routeSearch = new RouteSearch(context);
        bundle = new Bundle();

    }
    public static SeekF1Source getInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = new SeekF1Repository(context);
        }
        return INSTANCE;
    }

    @Override
    public void seekParks(final int state, final String name, final SeekF1Callback callback, final int flags) {
        //设置解析监听器   （地址解析完成后回调）
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

                //根据输入的关键字，解析出一个相似的地址名称，并添加进ParkNames集合中
                RegeocodeAddress rAddress = regeocodeResult.getRegeocodeAddress();
                parkNames.add(rAddress.getFormatAddress());

                parks.get(index).setName(parkNames.get(index));

                callback.callback(parks);
            }
            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                //System.out.println("index = " + index);

                //地址解析完成后

                parkNames.clear();  //先清除已有的地址名记录
                parks.clear();
                //如果有解析结果集
                if (geocodeResult != null && geocodeResult.getGeocodeAddressList().size() != 0){
                    addresses = geocodeResult.getGeocodeAddressList();

                    //遍历解析结果集
                    for (GeocodeAddress address : addresses) {
                        point = address.getLatLonPoint();
                        if (flags == HistoryParkAdapter.START_LOC){
                            startPoint = point;
                        }else if (flags == HistoryParkAdapter.END_LOC){
                            endPoint = point;
                        }
                        //创建一个ParkMsg类, 名字暂时不设置， 等方向地址解析时在设置
                        park = new ParkMsg("",point.getLongitude(), point.getLatitude(), 0,0,0f);
                        parks.add(park);

                        if (state == SeekF1ParkPresenter.SEEK_PARK){
                            ParksList.historyPark.add(park);
                        }

                        geocodeSearch.getFromLocationAsyn(new RegeocodeQuery(point, 20, GeocodeSearch.GPS));
                    }
                }
            }
        });
        if (!TextUtils.isEmpty(name)){
            geocodeQuery = new GeocodeQuery(name, "广州");

            geocodeSearch.getFromLocationNameAsyn(geocodeQuery);
        }
    }

    @Override
    public void deletePark(int pos, HomeFragment1Source.F1Callback callback) {
        Log.i(TAG, "deletePark: pos = " + pos);
        if (parks != null){
            parks.remove(pos);
        }

        callback.parks(parks);
    }

    @Override
    public void findParkLocation(int pos, SeekF1Callback callback) {
        if (parks != null){
            bundle.putDouble("Longitude",parks.get(pos).getLongitude() );
            bundle.putDouble("Latitude", parks.get(pos).getLatitude());
            callback.callbackBundle(bundle);
        }
    }
    @Override
    public void findHistoryParkLocation(int pos, SeekF1Callback callback) {
        if (ParksList.historyPark != null){
            Log.i(TAG, "findHistoryParkLocation: pos = " + pos);
            bundle.putDouble("Longitude",ParksList.historyPark.get(pos).getLongitude() );
            bundle.putDouble("Latitude", ParksList.historyPark.get(pos).getLatitude());
            callback.callbackBundle(bundle);
        }
    }
    @Override
    public void seekPath(boolean isExchange,final SeekF1Callback seekF1Callback) {

        Log.i(TAG, "seekPath: startPoint" + startPoint + "endPoint = " + endPoint);
        if (isExchange){
            LatLonPoint z = startPoint;
            startPoint = endPoint;
            endPoint = z;
        }
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        RouteSearch.DriveRouteQuery routeQuery = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault
                ,null, null,null);
        routeSearch.calculateDriveRouteAsyn(routeQuery);
        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
                List<PolylineOptions> polylineOptions = new ArrayList<>();      //用于返回PolylineOptions， 向地图添加线段
                DrivePath path = driveRouteResult.getPaths().get(0);
                List<DriveStep> steps = path.getSteps();
                List<LatLonPoint> latLonPoints;
                for (DriveStep step : steps) {
                    latLonPoints = step.getPolyline();
                    List<LatLng> latLngs = new ArrayList<>();
                    for (LatLonPoint point : latLonPoints) {
                        latLngs.add(new LatLng(point.getLatitude(), point.getLongitude()));
                    }
                    //创建一个PolylineOption， 用于向地图添加多线段
                    PolylineOptions options = new PolylineOptions().addAll(latLngs)
                            .color(0xffff0000)
                            .width(8);
                    polylineOptions.add(options);
                }
                seekF1Callback.callbackPolylineOption(polylineOptions);
            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });

    }



    @Override
    public void callback(List<ParkMsg> parks) {

    }

    @Override
    public void callbackBundle(Bundle bundle) {

    }

    @Override
    public void callbackPolylineOption(List<PolylineOptions> polylineOptionses) {

    }
}