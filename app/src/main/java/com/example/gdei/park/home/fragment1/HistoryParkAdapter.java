package com.example.gdei.park.home.fragment1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gdei.park.R;
import com.example.gdei.park.home.ParkMsg;

import java.util.List;

/**
 * Created by gdei on 2018/4/19.
 */

public class HistoryParkAdapter extends RecyclerView.Adapter<HistoryParkAdapter.HistoryParkHolder>{

    public static final int SEEK_PARK = 0;
    public static final int START_LOC = 1;
    public static final int END_LOC = 2;

    private List<ParkMsg> parks;    //加载进来的停车场列表
    private Context mContext;       //上下文
    private int state;              //用于记录当前显示的类型，0 表示查询停车场， 1 表示 查询定位

    private OnItemClickListener listener;   //用于回调信息的接口
    public HistoryParkAdapter(Context mContext, OnItemClickListener listener, List<ParkMsg> parks, int state){
        this.parks = parks;
        this.mContext = mContext;
       // System.out.println("Adapter构造器" + parks.size());
        this.listener = listener;
        this.state = state;
    }
    @Override
    public int getItemCount(){
       // System.out.println("getItemCount "+ parks.size());
        return parks.size();
    }
    @Override
    public HistoryParkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HistoryParkHolder holder = new HistoryParkHolder
                (LayoutInflater.from(mContext).inflate(R.layout.park_list_item,parent, false));
        //System.out.print("onCreateViewHolder "+viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(HistoryParkHolder holder, int position) {

        //System.out.print("onBindViewHolder "+position);
        holder.parkName.setText(parks.get(position).getName()+"");
        holder.totalPark.setText("总车位："+parks.get(position).getTotalPark());
        holder.canPark.setText("可停车位："+parks.get(position).getCanPark());
        holder.distance.setText(parks.get(position).getDistance()+"km");
        initListener(holder, position);
    }
    private void initListener(HistoryParkHolder hph, final int pos){
        hph.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickItem(pos);
            }
        });
        hph.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickDelete(pos);
            }
        });
    }

    class HistoryParkHolder extends RecyclerView.ViewHolder{

        private TextView parkName, distance, totalPark, canPark;
        private ImageButton delete;

        public HistoryParkHolder(View itemView) {
            super (itemView);

            parkName = itemView.findViewById(R.id.park_name);
            delete = itemView.findViewById(R.id.delete_park);
            distance = itemView.findViewById(R.id.distance);
            totalPark = itemView.findViewById(R.id.totalPark);
            canPark = itemView.findViewById(R.id.canPark);

            if (state == START_LOC || state == END_LOC){
                distance.setVisibility(View.INVISIBLE);
                totalPark.setVisibility(View.INVISIBLE);
                canPark.setVisibility(View.INVISIBLE);
            }
        }

        public TextView getParkName() {
            return parkName;
        }

        public TextView getDistance() {
            return distance;
        }

        public TextView getTotalPark() {
            return totalPark;
        }

        public TextView getCanPark() {
            return canPark;
        }
    }
    interface OnItemClickListener{
        /**
         * 点击该Item时（不包括删除按钮）
         */
        void onClickItem(int pos);
        /**
         * 点击删除按钮时
         */
        void onClickDelete(int pos);

    }
}
