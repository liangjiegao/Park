package com.example.gdei.park.home.fragment1;

import android.app.Activity;
import android.content.Intent;
import android.drm.DrmStore;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
    private int flags;      //用于标记跳转到的页面是否显示，我的位置，收藏等控件
    private int state = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_list);

        initView();

        Bundle bundle = getIntent().getExtras();

        if (bundle.getInt("state",-1) != -1){
            state = bundle.getInt("state");
        }

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
                presenter.seekPark(state, parkNameInput.getText().toString());
            }
        });

    }
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
            adapter = new HistoryParkAdapter(this, this, parks, flags);
            rVParkList.setLayoutManager(new LinearLayoutManager(this));
            rVParkList.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST));
            rVParkList.setAdapter(adapter);
        }
    }

    @Override
    public void setPresenter(HomeFragment1Contract.SeekParkPresenter presenter) {
        this.presenter = presenter;
    }
    //处理返回键
    @Override
    public void onClick(View v) {
        Intent intent1 = new Intent(this, HomeActivity.class);
        startActivity(intent1);
        finish();
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
            //如果是开始、结束位置定位
        }else if (flags == HistoryParkAdapter.START_LOC){
            Intent intent1 = new Intent(SeekF1ParkList.this, HomeActivity.class);

            if (myParks != null){
                Log.i(TAG, "onClickItem: myParks " + myParks.get(0).getName());
                bundle.putString("start", myParks.get(pos).getName());
            }
            //回到主页
            intent1.putExtras(bundle);
            startActivity(intent1);
            finish();
        }else if (flags == HistoryParkAdapter.END_LOC){
            Intent intent1 = new Intent(SeekF1ParkList.this, HomeActivity.class);
            if (myParks != null){
                bundle.putString("end", myParks.get(pos).getName());
            }
            intent1.putExtras(bundle);
            startActivity(intent1);
            finish();
        }
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
}
