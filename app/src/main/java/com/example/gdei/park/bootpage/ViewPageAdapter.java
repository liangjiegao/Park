package com.example.gdei.park.bootpage;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by gdei on 2018/4/13.
 */

public class ViewPageAdapter<T> extends PagerAdapter {

    private List<T> viewList;
    public ViewPageAdapter(List<T> viewList){
        this.viewList = viewList;
    }
    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView((View) viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) viewList.get(position));
    }
}
