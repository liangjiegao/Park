package com.example.gdei.park.bootpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.gdei.park.R;
import com.example.gdei.park.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gdei on 2018/4/13.
 */

public class BootPageFragment extends Fragment implements BootPageContract.View , ViewPager.PageTransformer{

    private static final String TAG = "BootPageFragment";
    private static final float MIN_SCALE = 0.75F;
    private  ViewPager VP_background;    //引导页ViewPager
    private List<View> mViewList = new ArrayList<>();   //View集合， 用于存放每一张图片
    private View v;     //作为根布局的View
    private LinearLayout dotLinearLayout;       //用于装 圆点

    private RelativeLayout bootpager_rl_main;   //引导页主布局
    private LinearLayout dots_ll;       //装静态小黑点
    private ImageView bootpage_iv_lightbot;  //跟随页面移动的白点
    private int dotsMargin;     //两个小黑点之间的距离
    private Button enter_mainPage;  //进入主页的按钮

    //View 持有Presenter
    private BootPagePresenter bootPagePresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_bootpage, container, false);

        initView();

        mViewList.add(inflater.inflate(R.layout.bootbackground1, null, false));
        mViewList.add(inflater.inflate(R.layout.bootbackground2, null, false));
        mViewList.add(inflater.inflate(R.layout.bootbackground3, null, false));

        ViewPageAdapter<View> adapter = new ViewPageAdapter<>(mViewList);
        VP_background.setAdapter(adapter);
        VP_background.setPageTransformer(false, this);
        return v;
    }

    /**
     * 初始化界面控件
     */
    private void initView(){

        VP_background = v.findViewById(R.id.boot_backgroundImage);
        bootpager_rl_main = BootPageActivity.getMainLayout();
        dots_ll = bootpager_rl_main.findViewById(R.id.bootpage_ll_dot_line);

        bootpage_iv_lightbot = bootpager_rl_main.findViewById(R.id.bootpage_iv_lightbot);
        RelativeLayout.LayoutParams ligthbot_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bootpage_iv_lightbot.setLayoutParams(ligthbot_params);
        //在布局发送变化， 或者某个视图发生变化时调用该方法， 会被多次调用
        bootpage_iv_lightbot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                dotsMargin  = dots_ll.getChildAt(1).getLeft() - dots_ll.getChildAt(0).getLeft();
                bootpage_iv_lightbot.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        addBreakDots();
        bootPagePresenter = new BootPagePresenter(this);
        bootPagePresenter.lightDotMoveToNextPosition();
        bootPagePresenter.changeViewByClickBreakDot();
        enter_mainPage = bootpager_rl_main.findViewById(R.id.enter_main);
        enter_mainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: ");
                bootPagePresenter.enterMainPage();
                Intent intent1 = new Intent(getContext(), LoginActivity.class);
                startActivity(intent1);
                getActivity().finish();
            }
        });
    }

    public static BootPageFragment getInstance(){
        return new BootPageFragment();
    }
    @Override
    public void setPresenter(BootPageContract.Presenter presenter) {

    }

    @Override
    public void showImage() {

    }

    @Override
    public void lightDotMoveToNextPosition() {
        //为viewPager的切换设置监听
        VP_background.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) bootpage_iv_lightbot.getLayoutParams();
                params.leftMargin = (int) (dotsMargin * (position + positionOffset));
                bootpage_iv_lightbot.setLayoutParams(params);
            }
            @Override
            public void onPageSelected(int position) {
                if (position == 2){
                    enter_mainPage.setVisibility(View.VISIBLE);
                }else {
                    enter_mainPage.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    public void addBreakDots() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView dot1 = new ImageView(getContext());
        dot1.setId(0);
        dot1.setBackground(getResources().getDrawable(R.drawable.bootpage_dot_brack));
        params.setMargins(0,0,40,0);
        dot1.setLayoutParams(params);
        dots_ll.addView(dot1);
        ImageView dot2 = new ImageView(getContext());
        dot2.setId(1);
        dot2.setBackground(getResources().getDrawable(R.drawable.bootpage_dot_brack));
        dot2.setLayoutParams(params);
        dots_ll.addView(dot2);
        ImageView dot3 = new ImageView(getContext());
        dot3.setId(2);
        dot3.setBackground(getResources().getDrawable(R.drawable.bootpage_dot_brack));
        dot3.setLayoutParams(params);
        dots_ll.addView(dot3);
    }

    @Override
    public void changeViewByClickBreakDot() {
        for (int i = 0; i < dots_ll.getChildCount(); i++){
            dots_ll.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VP_background.setCurrentItem(v.getId());

                }
            });
        }
    }

    @Override
    public void enterMainPage() {
        Snackbar.make(bootpager_rl_main, "进入主界面成功", Snackbar.LENGTH_LONG).show();
        //Toast.makeText(this, "进入主界面成功", Toast.LENGTH_LONG).S
    }

    /**
     *
     * @param page
     * @param position
     */
    @Override
    public void transformPage(View page, float position) {
        if (position < -1){
            //页面远离左侧页面
            page.setAlpha(0);
        }else if(position <=0){
            //当前页面
            page.setAlpha(1);
            page.setTranslationX(0);
            page.setScaleX(1);
            page.setScaleY(1);
        }else if (position < 1){
            //右侧页面
            page.setAlpha(1 - position);
            page.setTranslationX(page.getWidth() * -position);
            page.setScaleX(MIN_SCALE + (1 - position)*(1 - MIN_SCALE));
            page.setScaleY(MIN_SCALE + (1 - position)*(1 - MIN_SCALE));
        }else{
            //远离右侧的页面
            page.setAlpha(0);
        }
    }
}
