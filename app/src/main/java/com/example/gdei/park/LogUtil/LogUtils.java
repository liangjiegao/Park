package com.example.gdei.park.LogUtil;

import android.util.Log;

/**
 * Created by gdei on 2018/4/29.
 */

public class LogUtils {

    public static final boolean IS_LOG = false;
    public static void e(String tag, String msg){
        if (IS_LOG)
        Log.e(tag, msg );
    }

}
