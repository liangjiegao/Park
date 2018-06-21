package com.example.gdei.park.home;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by gdei on 2018/4/20.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    private int mOrientation;

    public DividerItemDecoration(int orientation){
        setOrientation(orientation);
    }
    public void setOrientation(int orientation){
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST){
            throw  new IllegalArgumentException();
        }
        mOrientation = orientation;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST){
            outRect.set(0,0,0,18);
        }else if (mOrientation == HORIZONTAL_LIST){
            outRect.set(0,0,18,0);
        }
    }
}
