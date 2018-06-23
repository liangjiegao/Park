package com.example.gdei.park.home.fragment1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.example.gdei.park.R;
import com.example.gdei.park.data.Injection;
import com.example.gdei.park.data.homeF1.SeekF1Source;
import com.example.gdei.park.home.fragment1.Navi.BaseActivity;
import com.example.gdei.park.home.fragment1.Navi.GPSNaviActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gdei on 2018/4/20.
 */

public class ShowMap extends Activity implements HomeFragment1Contract.SeekParkPresenter.MapView, AMap.OnMarkerClickListener, View.OnClickListener{
    private static final String TAG = "ShowMap";

    public static final int LOC = 0;
    public static final int PATH = 1;
    //-------------控件----------------------
    private MapView mapView;
    private Button startNavi;

    private AMap aMap;

    private Bundle bundle;      //封装了经纬度， 用于获取定位
    private CameraUpdate camera;
    private HomeFragment1Contract.SeekParkPresenter.IMapPresenter presenter;     //showMap持有的Presenter
    private HomeFragment1Contract.SeekParkPresenter seekParkPresenter;      //showMap 持有 SeekParkPresenter

    private int flags;                  //用于标志是查询定位置， 还是查询导航路线
    private MarkerOptions markerOptionsLOC;    //在地图上的标志
    private MarkerOptions markerOptionsPathStart;    //在地图上的标志
    private MarkerOptions markerOptionsPathEnd;    //在地图上的标志
    private boolean isMyLoc;    //由上一级Activity传过来， 标志开始位置是否当前定位

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_map);

       initView();
        mapView.onCreate(savedInstanceState);

        presenter = new MapPresenter(Injection.providerSeekF1Respository(this), this);
        seekParkPresenter = new SeekF1ParkPresenter(Injection.providerSeekF1Respository(this),this, flags);

        isMyLoc = Boolean.parseBoolean(getIntent().getStringExtra("isMyLoc"));
        initAMap();
        Log.i(TAG, "onCreate: "+getIntent().getAction());
        selectLocMode();
        Log.i(TAG, "onCreate:设置监听");
        //为Marker设置监听
        aMap.setOnMarkerClickListener(this);
    }

    /**
     * 初始化界面控件
     */
    private void initView(){
        mapView = findViewById(R.id.show_mapView);
        startNavi = findViewById(R.id.show_map_toNavi);
        startNavi.setOnClickListener(this);
    }
    /**
     * 选择定位类型， 是定位到单个位置， 还是两个位置的线路
     */
    private void selectLocMode(){
        //如果是两个地点的路线
        if (getIntent().getAction().equals("HomeFragment1SeekPath")){
            flags = PATH;
            Log.i(TAG, "onCreate: HomeFragment1SeekPath");
            presenter.seekPath(getIntent().getBooleanExtra("isExchange", false));
        }else if (getIntent().getAction().equals("HomeFragment1HistoryPark")){
            //如果是在历史记录拿出来的点
            Bundle bundle = getIntent().getExtras();
            seekParkPresenter.enterPark(bundle.getInt("pos"), SeekF1ParkPresenter.HISTORY_PARK, new HomeFragment1Contract.SeekParkPresenter.SeekParkCallback(){
                @Override
                public void callbackBundle(Bundle bundle) {
                    LatLng latLng = new LatLng(bundle.getDouble("Latitude"), bundle.getDouble("Longitude"));
                    updatePosition(latLng, null, null);
                }
            });
        }else if (getIntent().getAction().equals("SeekF1ParkList")){
            //如果是定位到单个位置，并且是搜索出来的地点
            flags = LOC;
            bundle = getIntent().getExtras();
            LatLng latLng = new LatLng(bundle.getDouble("Latitude"), bundle.getDouble("Longitude"));
            updatePosition(latLng, null, null);
        }
    }

    @Override
    public void updatePosition(LatLng latLng, LatLng startL, LatLng endL){

        //如果是定位，显示定位地图
        if (flags == LOC){
            markerOptionsLOC = new MarkerOptions();
            camera = CameraUpdateFactory.changeLatLng(latLng);
            aMap.moveCamera(camera);
            //创建一个标记， 用于显示定位点
            markerOptionsLOC.position(latLng);     //显示位置
            markerOptionsLOC.icon(BitmapDescriptorFactory.fromResource(R.drawable.location3));
            //添加MarkerOptions，实际上是添加MaekerOption
            aMap.addMarker(markerOptionsLOC);
        }else if (flags == PATH){
            markerOptionsPathStart = new MarkerOptions();
            //如果是导航， 显示导航地图
            camera = CameraUpdateFactory.changeLatLng(startL);
            aMap.moveCamera(camera);
            //创建一个标记， 用于显示定位点
            //MarkerOptions markerOptionsStart = new MarkerOptions();
            markerOptionsPathStart.position(startL);     //显示位置
            markerOptionsPathStart.icon(BitmapDescriptorFactory.fromResource(R.drawable.start));
            //添加MarkerOptions，实际上是添加MaekerOption
            aMap.addMarker(markerOptionsPathStart);
            markerOptionsPathEnd = new MarkerOptions();
            //创建一个标记， 用于显示定位点
            //MarkerOptions markerOptionsEnd = new MarkerOptions();
            markerOptionsPathEnd.position(endL);     //显示位置
            //markerOptionsPathEnd.
            markerOptionsPathEnd.icon(BitmapDescriptorFactory.fromResource(R.drawable.end));
            //添加MarkerOptions，实际上是添加MaekerOption
            aMap.addMarker(markerOptionsPathEnd);
        }

    }

    private String mSsw;
    private String mSsj;
    private String mSew;
    private String mSej;
    @Override
    public void seekPath(List<PolylineOptions> polylineOptions, String ssw, String ssj, String sew, String sej){
        mSsw = ssw;
        mSsj = ssj;
        mSew = sew;
        mSej = sej;
         if (aMap != null){
         for (PolylineOptions p : polylineOptions) {
             aMap.addPolyline(p);
             }
         }
    }

    private void initAMap(){
        if (aMap == null){
           // Log.i(TAG, "initAMap: ");
            aMap = mapView.getMap();
            //设置放大级别
            camera = CameraUpdateFactory.zoomTo(15);
            aMap.moveCamera(camera);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void setPresenter(HomeFragment1Contract.SeekParkPresenter.IMapPresenter presenter) {

    }

    /**
     * 监听地图中Marker被点击
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        //Log.i(TAG, "marker : "+marker.getPosition());
        //Log.i(TAG, "markerOptionsPathEnd : "+markerOptionsPathEnd.getPosition());
        if (markerOptionsPathEnd.getPosition().equals( marker.getPosition())){
            Log.i(TAG, "onMarkerClick: ");
            startActivity(new Intent(this,IndoorParkActivity.class));
        }
        return false;
    }

    /**
     * 实现OnClickListener 的点击事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.show_map_toNavi:
                toNavi(mSsw, mSsj,mSew,mSej);
                break;
        }
    }

    /**
     * 跳转到导航Activity页面
     * @param ssw
     * @param ssj
     * @param sew
     * @param sej
     */
    private void toNavi(String ssw, String ssj, String sew, String sej){

        Intent intent = new Intent(this, GPSNaviActivity.class);
        intent.putExtra("sj",ssj);
        intent.putExtra("sw",ssw);
        intent.putExtra("ej",sej);
        intent.putExtra("ew",sew);
        intent.putExtra("isMyLoc",isMyLoc);
        Log.i(TAG, "seekPath: String ssw, String ssj, String sew, String sej " +ssw +ssj +sew +sej);
        startActivity(intent);
        //finish();
    }
}
