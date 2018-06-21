package com.example.gdei.park.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;


import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.gdei.park.R;
import com.example.gdei.park.data.Injection;
import com.example.gdei.park.home.fragment1.HomeFragment1;
import com.example.gdei.park.home.fragment1.HomeFragment1Presenter;
import com.example.gdei.park.home.fragment2.HomeFragment2;
import com.example.gdei.park.home.fragment3.HomeFragment3;
import com.example.gdei.park.home.fragment4.HomeFragment4;
import com.example.gdei.park.util.ActivityUtil;


/**
 * Created by gdei on 2018/4/17.
 */

public class HomeActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{
    private static final String TAG = "HomeActivity";
    private BottomNavigationBar home_bnb;
    //不同页面的fragment
    private HomeFragment1 fragment1;
    private HomeFragment2 fragment2;
    private HomeFragment3 fragment3;
    private HomeFragment4 fragment4;
    private Fragment nowFragment;   //记录当前显示的Fragment


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        addFirstFragment();
        //创建 P

    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    private void initView(){

        home_bnb = findViewById(R.id.home_bnb);
        home_bnb.addItem(new BottomNavigationItem(R.drawable.car, R.string.seek_park).setActiveColorResource(R.color.home_navigation_bar1))
                .addItem(new BottomNavigationItem(R.drawable.function, R.string.function).setActiveColorResource(R.color.home_navigation_bar2))
                .addItem(new BottomNavigationItem(R.drawable.more, R.string.more).setActiveColorResource(R.color.home_navigation_bar3))
                .addItem(new BottomNavigationItem(R.drawable.me, R.string.my).setActiveColorResource(R.color.home_navigation_bar4))
                .setFirstSelectedPosition(0)
                .initialise();
        home_bnb.setTabSelectedListener(this);

    }
    private void addFirstFragment(){
        //fragment1 = (HomeFragment1) getSupportFragmentManager().findFragmentById(R.id.home_fl_fragment_content);
        if (fragment1 == null){
            fragment1 = new HomeFragment1();
            //ActivityUtil.addFragmentToActivity(getSupportFragmentManager(), fragment1, R.id.boot_background_content);
        }
        if (fragment2 == null){
            fragment2 = new HomeFragment2();
            //ActivityUtil.addFragmentToActivity(getSupportFragmentManager(), fragment1, R.id.boot_background_content);
        }
        nowFragment = fragment2;
        fragment2 = null;
        changeFragment(nowFragment, fragment1);
    }

    @Override
    public void onTabSelected(int position) {
        switch (position){
            case 0:
                if (fragment1 == null){
                    fragment1 = new HomeFragment1();
                }
                changeFragment(nowFragment, fragment1);
                break;
            case 1:
                if (fragment2 == null){
                    fragment2 = new HomeFragment2();
                }
               changeFragment(nowFragment, fragment2);
                break;
            case 2:
                if (fragment3 == null){
                    fragment3 = new HomeFragment3();
                }
                changeFragment(nowFragment, fragment3);
                break;
            case 3:
                if (fragment4 == null){
                    fragment4 = new HomeFragment4();
                }
                changeFragment(nowFragment, fragment4);
                break;
        }
    }
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private void changeFragment(Fragment fromFragment, Fragment toFragment){
        if (nowFragment != toFragment){
            nowFragment = toFragment;
        }

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        if (!toFragment.isAdded()){
            transaction.hide(fromFragment).add(R.id.home_fl_fragment_content,toFragment).commit();
            //Log.i(TAG, "changeFragment: !isAdd");
        }else {
            transaction.hide(fromFragment).show(toFragment).commit();
            //Log.i(TAG, "changeFragment: isAdd");
        }

    }
    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
