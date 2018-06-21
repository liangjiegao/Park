package com.example.gdei.park.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gdei.park.R;
import com.example.gdei.park.data.Injection;
import com.example.gdei.park.home.HomeActivity;

import java.util.Map;
import java.util.Set;

/**
 * Created by gdei on 2018/4/16.
 */

public class LoginActivity extends Activity implements LoginContract.View, View.OnClickListener{
    private static final String TAG = "LoginActivity";

    /**
     *相关界面控件
     */
    private RelativeLayout login_main_layout;

    private RelativeLayout loginbox_topbackground;
    private AutoCompleteTextView username , password;
    private Button login_bt;
    private TextView register, forget_password, order_username;
    private TextView welcome_text, welcome_text_china;

    private RelativeLayout registerbox_topbackground;
    private AutoCompleteTextView register_username , register_password;
    private Button register_bt;
    private TextView register_welcome_text, register_welcome_text_china, register_to_login;

    private RelativeLayout box_anim_background;

    /**
     *Presenter V 持有 P
     */
    private LoginContract.Presenter presenter;

    private String usernameText, passwordText;

    public static SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    /**
     * 动画相关信息
     */
    private MyAnimation animation;
    private int duration = 200;
    private int animPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginbox);

        //初始化登陆界面
        initView();
        preferences = getSharedPreferences("USER", MODE_PRIVATE);

        //创建presenter
        presenter = new LoginPresenter(Injection.provideLoginRespository(), this);

    }

    /**
     * 初始化控件， 并设置对应监听器
     */
    public void initView(){

        login_main_layout = findViewById(R.id.login_main_layout);
        loginbox_topbackground = findViewById(R.id.loginbox_topbackground);
        registerbox_topbackground = findViewById(R.id.registerbox_topbackground);
        username = findViewById(R.id.login_input01);
        password = findViewById(R.id.login_input02);
        register_username = findViewById(R.id.register_input01);
        register_password = findViewById(R.id.register_input02);
        login_bt = findViewById(R.id.login_button);
        register_bt = findViewById(R.id.register_button);

        box_anim_background = findViewById(R.id.box_anim_background);

        //设置字体
        welcome_text = findViewById(R.id.welcome_text);
        Typeface welcome = Typeface.createFromAsset(getAssets(), "fonts/welcome.TTF");
        welcome_text.setTypeface(welcome);
        welcome_text_china = findViewById(R.id.welcome_text_china);
        Typeface welcome_china = Typeface.createFromAsset(getAssets(), "fonts/welcome_china.otf");
        welcome_text_china.setTypeface(welcome_china);

        register_welcome_text = findViewById(R.id.register_welcome_text);
        Typeface welcome1 = Typeface.createFromAsset(getAssets(), "fonts/welcome.TTF");
        register_welcome_text.setTypeface(welcome1);
        register_welcome_text_china = findViewById(R.id.register_welcome_text_china);
        Typeface welcome_china1 = Typeface.createFromAsset(getAssets(), "fonts/welcome_china.otf");
        register_welcome_text_china.setTypeface(welcome_china1);

        register_to_login = findViewById(R.id.register_to_login);
        register_to_login.setOnClickListener(this);
        login_bt.setOnClickListener(this);
        register_bt.setOnClickListener(this);
        register = findViewById(R.id.register);
        register.setOnClickListener(this);
        forget_password = findViewById(R.id.forget_password);
        forget_password.setOnClickListener(this);
        order_username = findViewById(R.id.order_username);
        order_username.setOnClickListener(this);
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void loginFailed(String message) {
        Snackbar.make(login_main_layout, message, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void loginSuccess(String string) {
        clearText(username, password);
        Snackbar.make(login_main_layout, string, Snackbar.LENGTH_LONG).show();
        Intent intent1 = new Intent(this, HomeActivity.class);
        startActivity(intent1);
        finish();
    }

    @Override
    public void registerSuccess(String s) {
        clearText(register_username, register_password);
        Snackbar.make(login_main_layout, s, Snackbar.LENGTH_LONG).show();
        startAnimBackground();
    }

    @Override
    public void registerFailed(String s) {
        Snackbar.make(login_main_layout, s, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_button:
                usernameText = username.getText().toString();
                passwordText = password.getText().toString();
                presenter.login(usernameText, passwordText);
                break;
            case R.id.register:
                startAnimBackground();
                clearText(username, password);
                break;
            case R.id.forget_password:
                presenter.forgetPassword();
                break;
            case R.id.order_username:
                presenter.orderUsername();
                break;
            case R.id.register_button:
                usernameText = register_username.getText().toString();
                passwordText =  register_password.getText().toString();
                presenter.register(usernameText, passwordText);

                break;
            case R.id.register_to_login:
                startAnimBackground();
                break;
        }
    }
    private void clearText(TextView username, TextView password){
        username.setText("");
        password.setText("");
    }

    private void startAnimBackground(){
        Log.i(TAG, "startAnimBackground: ");
        animation = new MyAnimation(duration, -10);
        handler.sendEmptyMessage(0x123);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                if (animPosition == 0) {
                    box_anim_background.startAnimation(animation);
                    animPosition = 1;
                    handler.sendEmptyMessageDelayed(0x123, duration);
                } else if (animPosition == 1) {
                    animation.rotate = 190;
                    box_anim_background.startAnimation(animation);
                    handler.sendEmptyMessageDelayed(0x456, duration / 2);
                    animPosition = 2;
                    handler.sendEmptyMessageDelayed(0x123, duration);
                } else if (animPosition == 2) {
                    animation.rotate = 0;
                    box_anim_background.startAnimation(animation);
                    animPosition = 0;
                }
            }
            if (msg.what == 0x456){
                if (loginbox_topbackground.getVisibility() == View.VISIBLE) {
                    loginbox_topbackground.setVisibility(View.INVISIBLE);
                    registerbox_topbackground.setVisibility(View.VISIBLE);
                }else {
                    loginbox_topbackground.setVisibility(View.VISIBLE);
                    registerbox_topbackground.setVisibility(View.INVISIBLE);
                }
            }
        }
    };
}
