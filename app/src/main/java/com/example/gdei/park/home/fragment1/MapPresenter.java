package com.example.gdei.park.home.fragment1;

import android.os.Bundle;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import com.example.gdei.park.data.homeF1.SeekF1Source;
import com.example.gdei.park.home.ParkMsg;

import java.util.List;

/**
 * Created by gdei on 2018/4/24.
 */

public class MapPresenter implements HomeFragment1Contract.SeekParkPresenter.IMapPresenter {
    private static final String TAG = "MapPresenter";
    private SeekF1Source seekF1Repository;  //P 持有  SeekF1Repository， 用于搜索导航路线
    private HomeFragment1Contract.SeekParkPresenter.MapView showMap;                //要显示的Map  V

    public MapPresenter(SeekF1Source seekF1Repository, HomeFragment1Contract.SeekParkPresenter.MapView showMap){
        this.seekF1Repository = seekF1Repository;
        this.showMap = showMap;
    }
    @Override
    public void start() {

    }

    @Override
    public void seekPath(boolean isExchange) {
        Log.i(TAG, "seekPath: ");
        seekF1Repository.seekPath(isExchange,new SeekF1Source.SeekF1Callback() {
            @Override
            public void callback(List<ParkMsg> parks) {

            }

            @Override
            public void callbackBundle(Bundle bundle) {

            }

            @Override
            public void callbackPolylineOption(List<PolylineOptions> polylineOptionses) {
    
                LatLng startLatLng = polylineOptionses.get(0).getPoints().get(0);
                int lastPointNum = polylineOptionses.get(polylineOptionses.size() -1).getPoints().size() -1;
                LatLng endLatLng = polylineOptionses.get(polylineOptionses.size() -1).getPoints().get(lastPointNum);
                Log.i(TAG, "seekPath: startPoint" + startLatLng + "endPoint = " + endLatLng);
                showMap.updatePosition(null, startLatLng, endLatLng);
                showMap.seekPath(polylineOptionses);
            }
        });
    }


}
