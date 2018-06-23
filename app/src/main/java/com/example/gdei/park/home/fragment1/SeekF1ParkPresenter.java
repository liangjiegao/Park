package com.example.gdei.park.home.fragment1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import com.example.gdei.park.data.homeF1.HomeFragment1Source;
import com.example.gdei.park.data.homeF1.SeekF1Source;
import com.example.gdei.park.home.ParkMsg;

import java.util.List;

/**
 * Created by gdei on 2018/4/20.
 */

public class SeekF1ParkPresenter implements HomeFragment1Contract.SeekParkPresenter {

    private static final String TAG = "SeekF1ParkPresenter";
    
    public static final int SEEK_PATH = 0;
    public static final int SEEK_PARK = 1;
    public static final int HISTORY_PARK = 2;
    private SeekF1Source repository;    //P 持有  M
    private SeekF1ParkList view;        //P 持有 V
    private MapView mapView;            //P 持有 mapView
    private int pos;                    //当前点击历史记录的位置

    private int flags;                  //用于标志是搜索开始位置， 还是结束位置

    public SeekF1ParkPresenter(SeekF1Source repository, MapView mapView, int pos){
        this.repository = repository;
        this.mapView = mapView;
        this.pos = pos;
    }

    public SeekF1ParkPresenter(SeekF1Source repository, SeekF1ParkList view, int flags){
        this.repository = repository;
        this.view = view;
        view.setPresenter(this);
        this.flags = flags;
    }

    @Override
    public void start() {

    }

    @Override
    public void seekPark(int state, String parkName) {
        repository.seekParks(state,parkName, new MyCallback(null), flags);

    }

    @Override
    public void deletePark(int pos) {
        repository.deletePark(pos, new HomeFragment1Source.F1Callback() {
            @Override
            public void parks(List<ParkMsg> parks) {
                view.showFindPark(parks);
            }

            @Override
            public void showText(Bundle bundle) {

            }
        });
    }

    @Override
    public void enterPark(int pos,int state,  final SeekParkCallback callback) {
        if (state == SEEK_PARK) {
            repository.findParkLocation(pos, new MyCallback(callback));
        }else if (state == HISTORY_PARK){
            repository.findHistoryParkLocation(pos, new MyCallback(callback));
        }
    }



    class MyCallback implements SeekF1Source.SeekF1Callback{

        SeekParkCallback callback;
        public MyCallback(SeekParkCallback callback){
            this.callback = callback;
        }

        /**
         * 更新显示停车场的条目
         * @param parks
         */
        @Override
        public void callback(List<ParkMsg> parks) {
            Log.i(TAG, "callback: "+view);
            if (view != null){

                view.showFindPark(parks);
            }
        }

        @Override
        public void callbackBundle(Bundle bundle) {
            this.callback.callbackBundle(bundle);
        }

        @Override
        public void callbackPolylineOption(List<PolylineOptions> polylineOptionses) {

        }
    }
}
