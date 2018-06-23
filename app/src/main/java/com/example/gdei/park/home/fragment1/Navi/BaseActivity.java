package com.example.gdei.park.home.fragment1.Navi;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;

import java.util.ArrayList;
import java.util.List;

import static android.view.Window.FEATURE_NO_TITLE;

/**
 * Created by gdei on 2018/6/22.
 */

public class BaseActivity extends Activity implements AMapNaviListener, AMapNaviViewListener{


    protected AMapNavi mAMapNavi;
    protected AMapNaviView mAMapNaviView;

    protected NaviLatLng mEndLatLng = new NaviLatLng(40.084894, 116.603039);
    protected NaviLatLng mStartLatLng = new NaviLatLng(39.825934, 116.342972);
    protected final List<NaviLatLng> sList = new ArrayList<>();
    protected final List<NaviLatLng> eList = new ArrayList<>();
    protected List<NaviLatLng> mWatPointList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(FEATURE_NO_TITLE); //取出标题栏

        //获取AMapNavi的对象（单例）
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapNaviView.onDestroy();
        mAMapNavi.stopNavi();
        mAMapNavi.destroy();

    }

    //-----------------------AMapNaviListener接口实现的方法------------------------------------------
    @Override
    public void onInitNaviFailure() {
        //初始化失败时调用
    }

    @Override
    public void onInitNaviSuccess() {
        //初始化成功
    }

    @Override
    public void onStartNavi(int i) {
        //开始导航时回调
    }

    @Override
    public void onTrafficStatusUpdate() {
        //当前方路况光柱信息有更新时回调
    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
        //当位置改变时回调

    }

    @Override
    public void onGetNavigationText(int i, String s) {
        //播报类型和播报文字
    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {
        //结束导航时回调
    }

    @Override
    public void onArriveDestination() {
        //到达目的地时回调
    }

    @Override
    public void onCalculateRouteFailure(int i) {
        //路线计算失败
    }

    @Override
    public void onReCalculateRouteForYaw() {
        //偏离导航重新计算路线回调
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        //拥堵后重新计算路线回调
    }

    @Override
    public void onArrivedWayPoint(int i) {
        //到达途径点回调
    }

    @Override
    public void onGpsOpenStatus(boolean b) {
        //gps开关状态回调
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        //导航引导信息回调 naviinfo 是导航信息类。
    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {
        //过时
    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {
        //导航过程中的摄像头信息回调函数
    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {
        //导航过程中的区间测速信息回调函数
    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {
        //服务区信息回调函数
    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        //显示路口放大图回调(实景图)。
    }

    @Override
    public void hideCross() {
        //关闭路口放大图回调(实景图)。
    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {
        //显示路口放大图回调(模型图)。
    }

    @Override
    public void hideModeCross() {
        //关闭路口放大图回调(模型图)。
    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {
        //过时
    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {
        //显示道路信息回调。
    }

    @Override
    public void hideLaneInfo() {
        //关闭道路信息回调。
    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        //路线规划成功回调，
        // 包括算路、导航中偏航、用户改变算路策略、行程点等触发的重算，
        // 具体算路结果可以通过com.amap.api.navi.model.AMapCalcRouteResult获取
        // 可以通过CalcRouteResult获取算路错误码、算路类型以及路线id
    }

    @Override
    public void notifyParallelRoad(int i) {
        //通知当前是否显示平行路切换
        if (i == 0){
            Toast.makeText(this, "当前在主辅路过渡", Toast.LENGTH_SHORT).show();
            return;
        }
        if (i == 1){
            Toast.makeText(this,"当前在主路",Toast.LENGTH_SHORT).show();
            return;
        }
        if (i == 2){
            System.out.println(Toast.makeText(this, "当前在辅路", Toast.LENGTH_SHORT));
            return;

        }
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {

    }
    //---------------------------AMapNaviViewListener接口实现的方法------------------------------------------
    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {

    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviViewLoaded() {

    }
}
