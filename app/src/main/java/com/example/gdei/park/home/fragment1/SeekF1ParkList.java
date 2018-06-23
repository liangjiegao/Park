package com.example.gdei.park.home.fragment1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.gdei.park.R;
import com.example.gdei.park.data.Injection;
import com.example.gdei.park.home.DividerItemDecoration;
import com.example.gdei.park.home.HomeActivity;
import com.example.gdei.park.home.ParkMsg;

import java.util.List;

/**
 * Created by gdei on 2018/4/20.
 */

public class SeekF1ParkList extends Activity implements HomeFragment1Contract.SeekPark, View.OnClickListener, HistoryParkAdapter.OnItemClickListener{
    private static final String TAG = "SeekF1ParkList";
    private HomeFragment1Contract.SeekParkPresenter presenter;      //View 持有 P
    //界面控件
    private ImageButton ibBack;
    private EditText parkNameInput;
    private RecyclerView rVParkList;
    private RelativeLayout place_list_rl;
    private RadioButton navigation, collection, location;
    private List<ParkMsg> myParks;
    private int pos;        //当前点击myParks的位置
    private HistoryParkAdapter adapter; //复用适配器
    private int flags;      //用于标识点击进入的入口， 是查找停车场（SEEK_PARK）还是 查找路线 START_LOC  END_LOC
    //private int state = -1; //用于标识是寻找停车场的点（Park），还是查找路线（Path）
    private LocationManager locationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_list);

        initView();

        Bundle bundle = getIntent().getExtras();
        /*
        if (bundle.getInt("state",-1) != -1){
            state = bundle.getInt("state");
        }
        */
        if (presenter == null) {
            //Log.i(TAG, "onCreate: presenter");
            presenter = new SeekF1ParkPresenter(Injection.providerSeekF1Respository(this), this, flags);
        }

    }

    private void initView(){
        ibBack = findViewById(R.id.back);
        ibBack.setOnClickListener(this);
        parkNameInput = findViewById(R.id.place_input);
        rVParkList = findViewById(R.id.parkList);
        place_list_rl = findViewById(R.id.place_list_rl);
        navigation = findViewById(R.id.navigation);
        collection = findViewById(R.id.collection);
        location = findViewById(R.id.location);

        navigation.setOnClickListener(this);
        collection.setOnClickListener(this);
        location.setOnClickListener(this);

        flags = getIntent().getFlags();
        if (flags == HistoryParkAdapter.SEEK_PARK){
            place_list_rl.setVisibility(View.GONE);
        }else if (flags == HistoryParkAdapter.START_LOC || flags == HistoryParkAdapter.END_LOC){
            place_list_rl.setVisibility(View.VISIBLE);

        }
        //设置RadioButton的图标
        setRadioButtonImage();
        //输入框的监听， 当输入完成是查询该位置
        parkNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                //如果是手动输入[我的位置]，不会获取定位
                if (!parkNameInput.getText().toString().equals("[我的位置]")){
                    presenter.seekPark(flags, parkNameInput.getText().toString());
                }

            }
        });

    }

    /**
     * 设置RadioButton UI
     */
    private void setRadioButtonImage(){
        //定义底部图片的大小和位置
        Drawable drawable_nav = getResources().getDrawable(R.drawable.navigation);
        //当这个图片被绘制时，绑定一个矩形  第一0是距左右边距离，第二0是距上下边距离，第三35 长度,第四宽度
        drawable_nav.setBounds(0,0,35,35);
        //图片的放置位置
        navigation.setCompoundDrawables(drawable_nav, null,null, null);

        //定义底部图片的大小和位置
        Drawable drawable_coll = getResources().getDrawable(R.drawable.collection);
        //当这个图片被绘制时，绑定一个矩形  第一0是距左右边距离，第二0是距上下边距离，第三35 长度,第四宽度
        drawable_coll.setBounds(0,0,30,30);
        //图片的放置位置
        collection.setCompoundDrawables(drawable_coll, null,null, null);

        //定义底部图片的大小和位置
        Drawable drawable_loc = getResources().getDrawable(R.drawable.location);
        //当这个图片被绘制时，绑定一个矩形  第一0是距左右边距离，第二0是距上下边距离，第三35 长度,第四宽度
        drawable_loc.setBounds(0,0,35,35);
        //图片的放置位置
        location.setCompoundDrawables(drawable_loc, null,null, null);

    }

    /**
     * 显示查询到的Park
     * @param parks
     */
    @Override
    public void showFindPark(List<ParkMsg> parks) {
        myParks = parks;
        if (myParks != null){
            Log.i(TAG, "showFindPark: ");
            //如果是根据GPS回调回来的[我的位置]，则不显示列表
            if (myParks.get(myParks.size() - 1).getName().equals("[我的位置]")){
                Log.i(TAG, "返回park");
                handler.sendEmptyMessage(0x001);
            }else {
                adapter = new HistoryParkAdapter(this, this, parks, flags);
                rVParkList.setLayoutManager(new LinearLayoutManager(this));
                rVParkList.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST));
                rVParkList.setAdapter(adapter);
            }
        }
        myParks = parks;
    }

    @Override
    public void setPresenter(HomeFragment1Contract.SeekParkPresenter presenter) {
        this.presenter = presenter;
    }
    //处理返回键
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                Intent intent1 = new Intent(this, HomeActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.navigation:
                presenter.seekPark(flags, "[我的位置]");
                //getLngAndLatWithNetwork();
                //Log.i(TAG, "onClick: getLngAndLatWithNetwork() = " + getLngAndLatWithNetwork());
        }

    }



    /**
     * 当点击查询到的停车场时的处理
     * @param pos
     */
    @Override
    public void onClickItem(int pos) {
        this.pos = pos;
        Bundle bundle = new Bundle();
        //如果是查询停车场
        if (flags == HistoryParkAdapter.SEEK_PARK){
            presenter.enterPark(pos,SeekF1ParkPresenter.SEEK_PARK, new HomeFragment1Contract.SeekParkPresenter.SeekParkCallback() {
                @Override
                public void callbackBundle(Bundle bundle) {
                    //启动地图
                    Intent intent1 = new Intent(SeekF1ParkList.this, ShowMap.class);
                    intent1.setAction("SeekF1ParkList");
                    intent1.putExtras(bundle);
                    startActivity(intent1);
                    return;
                }
            });
            //如果是查询线路
        }else if (flags == HistoryParkAdapter.START_LOC){
            if (myParks != null){
                //调用记录选择停车场的信息并且返回主页的方法
                recordPos(bundle,"start", myParks.get(pos).getName());
            }
        }else if (flags == HistoryParkAdapter.END_LOC){
            if (myParks != null){
                recordPos(bundle,"end", myParks.get(pos).getName());
            }
        }
    }

    /**
     * 记录选择的位置， 返回主页
     * @param bundle    要传回主页的Bundle
     * @param parkNameKey   停车场 key
     * @param parkNameValue 停车场名称 value
     */
    private void recordPos(Bundle bundle, String parkNameKey, String parkNameValue){
        Intent intent1 = new Intent(SeekF1ParkList.this, HomeActivity.class);
        bundle.putString(parkNameKey,parkNameValue);
        //回到主页
        intent1.putExtras(bundle);
        startActivity(intent1);
        finish();
    }


    //处理删除查询结果的按钮
    @Override
    public void onClickDelete(int pos) {
       presenter.deletePark(pos);
    }
    //处理按返回键、 将选择过的停车场信息记录下来， 并传回主页
    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(this, HomeActivity.class);

        if (myParks != null && !myParks.isEmpty()){
            Bundle bundle = new Bundle();
            bundle.putString("parkName", myParks.get(pos).getName());
            bundle.putDouble("parkLongitude", myParks.get(pos).getLongitude());
            bundle.putDouble("parkLatitude", myParks.get(pos).getLatitude());
            bundle.putInt("parkCanPark", myParks.get(pos).getCanPark());
            bundle.putInt("parkTotalPark", myParks.get(pos).getTotalPark());
            bundle.putFloat("distance", myParks.get(pos).getDistance());
            intent1.putExtras(bundle);
        }

        intent1.setAction("SeekF1ParkList");
        startActivity(intent1);
        finish();
        super.onBackPressed();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x001:
                    //根据GPS获取位置结束
                    parkNameInput.setText("[我的位置]");
                    parkNameInput.setTextColor(Color.BLUE);
                    //presenter.seekPark(flags, parkNameInput.getText().toString());
                    recordPos(new Bundle(),"start", "[我的位置]");
                    break;
            }
        }
    };

}
