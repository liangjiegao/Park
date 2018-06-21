package com.example.gdei.park.bootpage;

import com.example.gdei.park.BasePresenter;
import com.example.gdei.park.BaseView;

/**
 * Created by gdei on 2018/4/13.
 */

public class BootPageContract {

    interface Presenter extends BasePresenter{
        /**
         * 进入主页
         */
        void enterHomePage();
        /**
         * 图片切换到下一页
         */
        void nextPage();
        /**
         * 亮圆点移动到下一个位置
         */
        void lightDotMoveToNextPosition();
        /**
         * 小黑点事件处理
         */
        void changeViewByClickBreakDot();
        /**
         * 进入主界面按钮点击事件处理
         */
        void enterMainPage();
    }
    interface View extends BaseView<Presenter>{
        /**
         * 显示图片
         */
        void showImage();
        /**
         * 改变亮点的位置
         */
        void lightDotMoveToNextPosition();
        /**
         * 添加小黑点
         */
        void addBreakDots();
        /**
         * 点击小黑点改变页面
         */
        void changeViewByClickBreakDot();
        /**
         * 进入主界面按钮点击事件处理
         */
        void enterMainPage();
    }
}
