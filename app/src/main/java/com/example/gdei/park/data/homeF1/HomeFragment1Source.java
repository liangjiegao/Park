package com.example.gdei.park.data.homeF1;

import android.os.Bundle;

import com.example.gdei.park.home.ParkMsg;

import java.util.List;
import java.util.Map;

/**
 * Created by gdei on 2018/4/19.
 */

public interface HomeFragment1Source {
    /**
     * 更新显示的ui界面
     * @param bundle    //显示的标题信息
     * @param callback  //回调的信息
     */
    void updateUI(Bundle bundle, F1Callback callback);
    /**
     * 起点，终点的输入
     */
    void seekNavigationPath(String start, String end);

    /**
     * 删除停车场记录
     */
    void deletePark(int pos, F1Callback callback);

    void addHistoryPark(ParkMsg parkMsg);

    interface F1Callback{
        void parks(List<ParkMsg> parks);
        void showText(Bundle bundle);
    }
}
