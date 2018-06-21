package com.example.gdei.park.data.homeF1;

import android.os.Bundle;

import com.amap.api.maps.model.PolylineOptions;
import com.example.gdei.park.home.ParkMsg;

import java.util.List;

/**
 * Created by gdei on 2018/4/20.
 */

public interface SeekF1Source {
    /**
     * 解析输入的地址，查出停车场
     */
    void seekParks(int state, String parkName, SeekF1Callback callback, int flags);

    /**
     * 删除停车场记录
     * @param pos
     * @param callback
     */
    void deletePark(int pos, HomeFragment1Source.F1Callback callback);

    void findParkLocation(int pos, SeekF1Callback callback);

    void seekPath(boolean isExchange, SeekF1Callback seekF1Callback);

    void findHistoryParkLocation(int pos, SeekF1Callback callback);

    /**
     * 用于向presenter返回数据的接口
     */
    interface SeekF1Callback{

        void callback(List<ParkMsg> parks);

        void callbackBundle(Bundle bundle);     //查询指定位置时， 返回一个封装了经纬度的Bundle

        void callbackPolylineOption(List<PolylineOptions> polylineOptionses);
    }
}
