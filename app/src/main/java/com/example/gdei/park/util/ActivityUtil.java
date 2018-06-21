package com.example.gdei.park.util;



import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;

/**
 * Created by gdei on 2018/4/15.
 * 将Fragment添加到Activity中的工具类
 */

public class ActivityUtil {

    public static void addFragmentToActivity(FragmentManager supportFragmentManager, Fragment fragment, int fragmentId){
        FragmentTransaction transaction =   supportFragmentManager.beginTransaction();
        transaction.replace(fragmentId,fragment);
        transaction.commit();
    }
}
