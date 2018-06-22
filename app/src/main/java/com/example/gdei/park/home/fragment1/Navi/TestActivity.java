package com.example.gdei.park.home.fragment1.Navi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.example.gdei.park.R;

/**
 * Created by gdei on 2018/6/22.
 */

public class TestActivity extends Activity {

    private Button startNavi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_test);

        startNavi = findViewById(R.id.bt_start_navi);
        startNavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String xx = "39.825934";
                String yy = "116.342972";
                try {
                    if(!xx.equals("")&&!yy.equals("")){
                        toNavigation(TestActivity.this,xx,yy,"39.113656","116.401063");
                    }
                } catch (Exception e) {
                }
            }
        });

    }
    private void toNavigation(Context context, String xx, String yy, String jxx, String jyy){
        //1、判断手机是否安装高德地图APP
        boolean isInstalled = isPkgInstalled("com.autonavi.minimap", context);
        //2、首选使用高德地图
        if (isInstalled){

        }else {
            //使用高德地图导航sdk完成导航
            System.out.println("====未安装地图=>>>>");

            Intent intent = new Intent(context, GPSNaviActivity.class);
            intent.putExtra("sj",yy);
            intent.putExtra("sw",xx);
            intent.putExtra("ej",jyy);
            intent.putExtra("ew",jxx);
            context.startActivity(intent);

        }

    }

    /**
     * 判断手机上是否安装高德地图的方法
     * @param packagename
     * @param context
     * @return
     */
    private static boolean isPkgInstalled(String packagename, Context context){
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
