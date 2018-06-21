package com.example.gdei.park.bootpage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.gdei.park.R;
import com.example.gdei.park.login.LoginActivity;
import com.example.gdei.park.util.ActivityUtil;

/**
 * Created by gdei on 2018/4/13.
 */

public class BootPageActivity extends AppCompatActivity {

    private static final String TAG = "BootPageActivity";
    private static RelativeLayout bootpager_rl_main;   //引导页主布局

    private BootPageFragment bootPageFragment;  //viewPager的Fragment
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bootpage);

        initView();

        //创建Fragment（V）
        bootPageFragment = (BootPageFragment) getSupportFragmentManager().findFragmentById(R.id.boot_background_content);
        if (bootPageFragment == null){
            bootPageFragment = BootPageFragment.getInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(), bootPageFragment, R.id.boot_background_content);
        }

        //创建Presenter

    }
    /**
     * 初始化界面组件
     */
    public void initView(){
        Log.i(TAG, "initView: ");
        bootpager_rl_main = findViewById(R.id.bootpager_rl_main);

    }

    public static RelativeLayout getMainLayout(){
        return bootpager_rl_main;
    }

}
