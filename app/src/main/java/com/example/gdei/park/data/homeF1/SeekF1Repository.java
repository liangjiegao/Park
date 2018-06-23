package com.example.gdei.park.data.homeF1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
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
import com.autonavi.rtbt.IFrameForRTBT;
import com.example.gdei.park.home.ParkMsg;
import com.example.gdei.park.home.fragment1.HistoryParkAdapter;
import com.example.gdei.park.home.fragment1.ParksList;
import com.example.gdei.park.home.fragment1.SeekF1ParkPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gdei on 2018/4/20.
 */

public class SeekF1Repository implements SeekF1Source , SeekF1Source.SeekF1Callback, AMapLocationListener{
    private static final String TAG = "SeekF1Repository";
    private GeocodeSearch geocodeSearch;
    private GeocodeQuery geocodeQuery;
    private RouteSearch routeSearch;
    private LocationManager locationManager;
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    private Context mContext;   //上下文
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

    private int flags;
    private SeekF1Callback callback;

    private SeekF1Repository(Context context){
        mContext = context;
        parks = new ArrayList<>();
        geocodeSearch = new GeocodeSearch(mContext);
        routeSearch = new RouteSearch(mContext);
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
        this.callback = callback;
        this.flags = flags;
        //设置解析监听器   （地址解析完成后回调）
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

                //根据输入的关键字，解析出一个相似的地址名称，并添加进ParkNames集合中
                RegeocodeAddress rAddress = regeocodeResult.getRegeocodeAddress();
                parkNames.add(rAddress.getFormatAddress());

                parks.get(index).setName(parkNames.get(index));
                //提示更新显示停车场的条目
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
                        //创建一个ParkMsg类, 名字暂时不设置， 等反向地址解析时在设置
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
        Log.i(TAG, "位置 ---》" + name);
        if (!TextUtils.isEmpty(name) && !name.equals("[我的位置]")){
            geocodeQuery = new GeocodeQuery(name, "广州");
            geocodeSearch.getFromLocationNameAsyn(geocodeQuery);
        }else if (name.equals("[我的位置]")){

            getLngAndLatWithNetwork();

            /*
            Log.i(TAG, "位置 ---》" + name);
            //根据Gps获取位置Location
            locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);

            Location lastKnownLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLoc != null){
                getPoint(lastKnownLoc,flags,callback);
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.i(TAG, "位置 ---》" + name);
                    getPoint(location,flags,callback);

                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}
                @Override
                public void onProviderEnabled(String provider) {}
                @Override
                public void onProviderDisabled(String provider) {}
            });
            */
        }

    }

    //从网络获取经纬度
    public Location getLngAndLatWithNetwork() {

        mlocationClient = new AMapLocationClient(mContext);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
        return null;

    }

    private void getPoint(Location location,int flags, SeekF1Callback callback){
        //Log.i(TAG, "位置 ---》" + name);
        if (location == null){
            return;
        }
        //根据location获取
        LatLonPoint latLonPoint = new LatLonPoint(location.getLatitude(),location.getLongitude());
        Log.i(TAG, "flags = "+ flags);
        //如果输入的是开始位置
        if (flags == HistoryParkAdapter.START_LOC){
            startPoint = latLonPoint;
            Log.i(TAG, "选择【我的位置】");
        }else if (flags == HistoryParkAdapter.END_LOC){
            //如果输入的是结束位置
            endPoint = latLonPoint;
        }

        park = new ParkMsg("[我的位置]",latLonPoint.getLongitude(), latLonPoint.getLatitude(), 0,0,0f);
        parks.add(park);
        Log.i(TAG, "park.Name = " + parks.get(0).getName());
        //提示更新显示停车场的条目(这里起到的作用是回调获取完gps的提示，因为获取gps要一定的时间)
        callback.callback(parks);

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

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        getPoint(aMapLocation,flags,callback);
        mlocationClient.onDestroy();
    }
}