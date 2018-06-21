package com.example.gdei.park.home.fragment1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.gdei.park.R;

import java.util.ArrayList;

/**
 * Created by gdei on 2018/6/21.
 */

public class IndoorParkActivity extends Activity {

    private ArrayList<Integer> carArray;
    private RelativeLayout indoorContent;
    private RecyclerView carShow;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_park);
        initView();
        initData();
        buildArray("0010010111001100010010101001010011010100000101010101010");
        MyCarShowRVAdapter myCarShowRVAdapter = new MyCarShowRVAdapter(this,carArray);
        carShow.setLayoutManager(new GridLayoutManager(this,8));
        carShow.addItemDecoration(new SpaceItemDecoration(65));
        carShow.setAdapter(myCarShowRVAdapter);

    }
    private void initView(){

        indoorContent = findViewById(R.id.rl_indoor);
        carShow = findViewById(R.id.rv_car_show);

    }
    private void initData(){
        carArray = new ArrayList<>();
    }
    /**
     * 建立车位数组， 用于提供显示依据
     * @param carMsg
     */
    private void buildArray(String carMsg){
        int isCar;
        for (int i = 0; i < carMsg.length(); i++) {
            isCar = Integer.parseInt(carMsg.substring(i,i+1));
            carArray.add(i,isCar);
        }
    }
    class MyCarShowRVAdapter extends RecyclerView.Adapter<MyCarShowRVAdapter.MyViewHolder>{

        private Context mContext;
        private ArrayList<Integer> mCarStates;

        public MyCarShowRVAdapter(Context context, ArrayList<Integer> carStates){
            mContext = context;
            mCarStates = carStates;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_car_loc,parent,false);
            MyViewHolder myViewHolder = new MyViewHolder(rootView);

            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            if (mCarStates.get(position) == 0){
                holder.carLoc.setBackgroundResource(R.color.indoor_no_car);
            }else {
                holder.carLoc.setBackgroundResource(R.color.indoor_has_car);
            }

        }

        @Override
        public int getItemCount() {
            return mCarStates.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            private LinearLayout carLoc;

            public MyViewHolder(View itemView) {
                super(itemView);

                carLoc = itemView.findViewById(R.id.ll_car_loc);
            }
        }
    }
}
