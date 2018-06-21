package com.example.gdei.park.home.fragment1;

import android.os.Bundle;

import com.example.gdei.park.data.homeF1.HomeFragment1Repository;
import com.example.gdei.park.data.homeF1.HomeFragment1Source;
import com.example.gdei.park.data.homeF1.SeekF1Repository;
import com.example.gdei.park.data.homeF1.SeekF1Source;
import com.example.gdei.park.home.ParkMsg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gdei on 2018/4/19.
 */

public class HomeFragment1Presenter implements HomeFragment1Contract.Presenter ,HomeFragment1Source.F1Callback{

    private HomeFragment1Source repository;     // P 持有 本身的M

    private HomeFragment1Contract.View view;     // P 持有 V
    public HomeFragment1Presenter(HomeFragment1Source repository, HomeFragment1 fragment1){
        this.repository = repository;

        this.view = fragment1;
        view.setPresenter(this);
    }
    @Override
    public void start() {

    }

    //查找历史记录的停车场
    @Override
    public void updateUI(final Bundle bundle) {
        repository.updateUI(bundle,this);

    }

    @Override
    public void seekNavigationPath(String start, String end) {
        repository.seekNavigationPath(start, end);
    }

    @Override
    public void deletePark(int pos) {
        repository.deletePark(pos, this);
    }

    @Override
    public void addHistoryPark(ParkMsg parkMsg) {
        repository.addHistoryPark(parkMsg);

    }

    @Override
    public void parks(List<ParkMsg> parks) {
        view.showHistoryPark((ArrayList<ParkMsg>) parks);
    }

    @Override
    public void showText(Bundle bundle) {
        view.viewSetText(bundle);
    }
}
