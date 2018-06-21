package com.example.gdei.park;

/**
 * Created by gdei on 2018/4/13.
 */

public interface BaseView<T> {
    /**
     * 获取一个presenter 每一View的操作都需要一个Presenter来连接Model
     * @param presenter
     */
    void setPresenter(T presenter);
}
