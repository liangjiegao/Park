package com.example.gdei.park.home.fragment1;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.gdei.park.R;
import com.example.gdei.park.data.Injection;
import com.example.gdei.park.home.HomeActivity;
import com.example.gdei.park.home.ParkMsg;

import java.util.ArrayList;

/**
 * Created by gdei on 2018/4/17.
 */

public class HomeFragment1 extends Fragment implements HomeFragment1Contract.View, View.OnClickListener, HistoryParkAdapter.OnItemClickListener {
    private static final String TAG = "HomeFragment1";
    //不同的操作按钮
    private Button home_frag1_et_seek_park, home_frag_et_start, home_frag_et_end;
    private ImageButton home_frag_ib_seek_path, home_frag_ib_exchange;
    private View rootView;  //fragment的主界面
    private RecyclerView rvHistoryPark;
    private HistoryParkAdapter adapter;
    public HomeFragment1Contract.Presenter presenter;       // V 持有 P


    private Intent backIntent;      //用于获取SeekF1ParkList返回Intent
    private Bundle backBundle;      //用于获取SeekF1ParkList返回Bundle
    private String backStart;       //用于获取输入的其实位置
    private String backEnd;         //用于获取输入的终点位置


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_fragment1, container, false);
        //创建 P
        if (presenter == null){
            presenter = new HomeFragment1Presenter(Injection.providerHomeF1Repository(), this);
        }
        //初始化界面
        initView();

        return rootView;

    }

    private void initView(){

        home_frag1_et_seek_park = rootView.findViewById(R.id.home_frag1_et_seek_park);
        home_frag1_et_seek_park.setOnClickListener(this);
        home_frag_et_start = rootView.findViewById(R.id.home_frag_et_start);
        home_frag_et_start.setOnClickListener(this);
        home_frag_et_end = rootView.findViewById(R.id.home_frag_et_end);
        home_frag_et_end.setOnClickListener(this);
        home_frag_ib_seek_path = rootView.findViewById(R.id.home_frag_ib_seek_path);
        home_frag_ib_seek_path.setOnClickListener(this);
        home_frag_ib_exchange = rootView.findViewById(R.id.home_frag_ib_exchange);
        home_frag_ib_exchange.setOnClickListener(this);

        rvHistoryPark = rootView.findViewById(R.id.home_frag1_rv_historyPark);
        rvHistoryPark.setOnClickListener(this);
        //更新UI界面
        presenter.updateUI(null);
    }

    /**
     * 设置界面要显示的标题文字
     * @param bundle    //SeekF1List传回来的bundle， 封装了开始和或结束位置的字符串
     */
    @Override
    public void viewSetText(Bundle bundle){

        if (bundle != null){
            backStart = bundle.getString("start");
            backEnd = bundle.getString("end");
        }

            if (isExchange){
                String z = backStart;
                backStart = backEnd;
                backEnd = z;
                isExchange = false;
            }

        if (backStart != null){
            home_frag_et_start.setText(backStart);
        }
        if (backEnd != null) {
            home_frag_et_end.setText(backEnd);
        }
    }

    /**
     * 获取SeekF1List传回来的Bundle， 在updateUI时使用到
     * @return
     */
    public Bundle getViewTextBundle(){

        backIntent = getActivity().getIntent();
        if (backIntent != null){
            backBundle = backIntent.getExtras();
            if (backBundle != null){
                Log.i(TAG, "getViewTextBundle: backBundle" + backBundle);
                return backBundle;
            }
        }
        return null;
    }

    /**
     * 当Activity再次回到前台时， 添加一次历史记录
     */
    @Override
    public void onResume() {
        /*
        ParkMsg parkMsg = null;
        Intent comeIntent = getActivity().getIntent();
        Bundle bundle = null;
        //如果是停车场查询的记录，才会被添加进来
        if (comeIntent != null && comeIntent.getAction() != null && comeIntent.getAction().equals("SeekF1ParkList")){
            bundle = comeIntent.getExtras();
            if (bundle != null){

                parkMsg = new ParkMsg(bundle.getString("parkName"),
                        bundle.getDouble("parkLongitude"),bundle.getDouble("parkLatitude"),
                        bundle.getInt("parkCanPark"),bundle.getInt("parkTotalPark"),
                        bundle.getFloat("distance"));

            }
        }
        Log.i(TAG, "onResume: parkMsg = " + parkMsg);
        if (parkMsg != null){
            presenter.addHistoryPark(parkMsg);
        }
        */
        Log.i(TAG, "onResume: getViewTextBundle()"+getViewTextBundle());
        presenter.updateUI(getViewTextBundle());
        super.onResume();
    }

    @Override
    public void setPresenter(HomeFragment1Contract.Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * 点击时间的监听， 每个地址查询（停车场、开始位置， 结束位置）的监听器
     * @param v
     */
    private boolean isExchange;
    @Override
    public void onClick(View v) {
        //用于启动搜索列表Activity或地图的Intent
        Intent intent1 = null;
        Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.home_frag1_et_seek_park:
                //创建跳转Intent
                intent1 = new Intent(this.getActivity(), SeekF1ParkList.class);
                bundle.putInt("state", SeekF1ParkPresenter.SEEK_PARK);
                intent1.setFlags(HistoryParkAdapter.SEEK_PARK);
                break;
            case R.id.home_frag_et_start:
                //创建跳转Intent
                intent1 = new Intent(this.getActivity(), SeekF1ParkList.class);
                bundle.putInt("state", SeekF1ParkPresenter.SEEK_PATH);
                intent1.setFlags(HistoryParkAdapter.START_LOC);
                break;
            case R.id.home_frag_et_end:
                //创建跳转Intent
                intent1 = new Intent(this.getActivity(), SeekF1ParkList.class);
                bundle.putInt("state", SeekF1ParkPresenter.SEEK_PATH);
                intent1.setFlags(HistoryParkAdapter.END_LOC);
                break;
            case R.id.home_frag_ib_exchange:
                Log.i(TAG, "onClick: ");
                isExchange = true;
                viewSetText(null);
                break;
            case R.id.home_frag_ib_seek_path:
                Log.i(TAG, "onClick: ");
                if (!TextUtils.isEmpty(backStart) && !TextUtils.isEmpty(backEnd)){
                    //创建跳转Intent
                    intent1 = new Intent(this.getActivity(), ShowMap.class);
                    intent1.setAction("HomeFragment1SeekPath");
                    intent1.putExtra("isExchange", isExchange);
                    startActivity(intent1);
                    //this.getActivity().finish();
                    return;
                }
                break;

        }
        if (intent1 != null){
            intent1.putExtras(bundle);
            startActivity(intent1);
            this.getActivity().finish();
        }
    }

    /**
     * 显示历史记录的方法
     * @param parks
     */
    @Override
    public void showHistoryPark(ArrayList<ParkMsg> parks) {
        //自定义RecycleView的适配器
        adapter = new HistoryParkAdapter(this.getContext(), this, parks, 0);//必须
        rvHistoryPark.setLayoutManager(new LinearLayoutManager(this.getContext()));//必须
        //rvHistoryPark.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST));
        rvHistoryPark.setAdapter(adapter);
    }

    @Override
    public void onClickItem(int pos) {
        Log.i(TAG, "onClickItem: pos = " + pos);
        Intent intent = new Intent(this.getActivity(), ShowMap.class);
        intent.setAction("HomeFragment1HistoryPark");
        Bundle bundle = new Bundle();
        bundle.putInt("pos", pos);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    /**
     * 删除记录
     * @param pos
     */
    @Override
    public void onClickDelete(int pos) {
        presenter.deletePark(pos);
    }
}
