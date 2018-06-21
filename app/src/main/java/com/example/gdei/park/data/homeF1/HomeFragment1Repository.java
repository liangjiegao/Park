package com.example.gdei.park.data.homeF1;

import android.os.Bundle;
import android.util.Log;

import com.example.gdei.park.home.ParkMsg;
import com.example.gdei.park.home.fragment1.ParksList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gdei on 2018/4/19.
 */

public class HomeFragment1Repository implements HomeFragment1Source{
    private static final String TAG = "HomeFragment1Repository";
    private static HomeFragment1Repository INSTANCE;    //作为单例

    private Bundle bundle;              //封装了开始、结束标题的Bundle

    private HomeFragment1Repository(){
        bundle = new Bundle();
    }
    public static HomeFragment1Repository getInstance(){
        if (INSTANCE == null){
            INSTANCE = new HomeFragment1Repository();
        }
        return INSTANCE;
    }
    @Override
    public void updateUI(Bundle bundle, F1Callback callback) {

        if (bundle != null){
            if (bundle.getString("start") != null){
                this.bundle.putString("start", bundle.getString("start"));
            }
            if (bundle.getString("end") != null){
                this.bundle.putString("end", bundle.getString("end"));
            }
        }
        callback.parks(ParksList.historyPark);
        callback.showText(this.bundle);
    }

    @Override
    public void seekNavigationPath(String start, String end) {

    }

    @Override
    public void deletePark(int pos, F1Callback callback) {
        Log.i(TAG, "deletePark: pos = " + pos);
        ParksList.historyPark.remove(pos);
        callback.parks(ParksList.historyPark);
    }

    /**
     * 添加历史停车场
     * @param parkMsg
     */
    @Override
    public void addHistoryPark(ParkMsg parkMsg) {
        boolean isHasPark = false;
        for (ParkMsg park : ParksList.historyPark) {
            //如果已经存在，则不添加
            if (parkMsg.getName().equals(park.getName())){
                isHasPark = true;
            }
        }
        if (!isHasPark){
            ParksList.historyPark.add(parkMsg);
        }

    }

}
