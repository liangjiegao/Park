package com.example.gdei.park.home.fragment1.Navi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;

import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.example.gdei.park.R;

/**
 * Created by gdei on 2018/6/22.
 */

public class GPSNaviActivity extends BaseActivity {

    //继承BaseActivity
    private double sj;
    private double sw;
    private double ej;
    private double ew;
    private String ssj;
    private String ssw;
    private String sej;
    private String sew;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("导航", "进入导航界面");
        Intent intent = getIntent();
        /*经纬度 纬度在前*/
        ssj = intent.getStringExtra("sj");
        sj = Double.parseDouble(ssj);
        ssw = intent.getStringExtra("sw");
        sw = Double.parseDouble(ssw);
        sej = intent.getStringExtra("ej");
        ej = Double.parseDouble(sej);
        sew = intent.getStringExtra("ew");
        ew = Double.parseDouble(sew);

        Log.e("导航", "获取数据完毕");
        setContentView(R.layout.activity_basic_navi);
        mAMapNaviView = findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);

    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        /**
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion,
         * avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         *                说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，
         *                如果为true则此策略会算出多条路线。 注意: 不走高速与高速优先不能同时为true
         *                高速优先与避免收费不能同时为true
         */
        //重置起点
        this.sList.clear();
        this.mStartLatLng = new NaviLatLng(sw,sj);
        this.sList.add(mStartLatLng);
        //重置终点
        this.eList.clear();
        this.mEndLatLng = new NaviLatLng(ew,ej);
        this.eList.add(mEndLatLng);
        //导航策略
        int strategy = 0;
        try{
            //最后一个参数为true时代表多路径，否则代表单路径
            strategy = mAMapNavi.strategyConvert(true,false,false,false,false);
        }catch (Exception e){
            e.printStackTrace();
        }
       mAMapNavi.calculateDriveRoute(sList,eList,mWatPointList, strategy);  //驾车

    }

    /**
     * 计算路径成功时回调
     * @param ints
     */
    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        super.onCalculateRouteSuccess(ints);
        mAMapNavi.startNavi(NaviType.GPS);  //开始导航
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确认退出导航？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

        return super.onKeyDown(keyCode, event);
    }
}
