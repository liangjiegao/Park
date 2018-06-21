package com.example.gdei.park.home.fragment1;

import android.os.Bundle;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import com.example.gdei.park.BasePresenter;
import com.example.gdei.park.BaseView;
import com.example.gdei.park.data.homeF1.HomeFragment1Source;
import com.example.gdei.park.data.homeF1.SeekF1Source;
import com.example.gdei.park.home.ParkMsg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gdei on 2018/4/17.
 */

public class HomeFragment1Contract {
    /**
     * fragment1 的 Presenter
     */
    interface Presenter extends BasePresenter{
        /**
         * 更新界面显示， 历史停车场、显示标题
         */
        void updateUI(Bundle bundle);
        /**
         * 搜索导航路线
         */
        void seekNavigationPath(String start, String end);
        /**
         * 删除停车场信息
         */
        void deletePark(int pos);
        /**
         * 添加历史停车场
         * @param parkMsg
         */
        void addHistoryPark(ParkMsg parkMsg);

    }

    /**
     * Fragment1 的 View
     */
    interface View extends BaseView<Presenter>{
        /**
         * 显示历史停车场
         */
        void showHistoryPark(ArrayList<ParkMsg> parks);
        /**
         * 更新显示文字
         */
        void viewSetText(Bundle bundle);

    }

    /**
     * 停车场查询的View
     */
    interface SeekPark extends BaseView<SeekParkPresenter>{
        /**
         * 显示找到的停车场
         */
        void showFindPark(List<ParkMsg> parks);

    }
    /**
     * 停车场查询的Presenter
     */
    interface SeekParkPresenter extends BasePresenter{
        /**
         * 解析输入的停车场
         */
        void seekPark(int state, String parkName);
        /**
         * 删除停车场
         */
        void deletePark(int pos);
        /**
         * 查看停车场定位
         */
        void enterPark(int pos,int state, SeekParkCallback callback);


    interface IMapPresenter extends BasePresenter{

        /**
         * 查询导航路线
         */
        void seekPath(boolean isExchange);

    }

    interface MapView extends BaseView<IMapPresenter>{
        /**
         * 更新定位
         */
        void updatePosition(LatLng latLng, LatLng startL, LatLng endL);

        /**
         * 查询导航路径
         */
        void seekPath(List<PolylineOptions> polylineOptions);


    }


        interface SeekParkCallback{
            /**
             * 回调一个bundle， 封装经纬度
             */
            void callbackBundle(Bundle bundle);
        }
    }
}
