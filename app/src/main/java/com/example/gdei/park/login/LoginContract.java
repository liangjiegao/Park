package com.example.gdei.park.login;


import com.example.gdei.park.BasePresenter;
import com.example.gdei.park.BaseView;

/**
 * Created by gdei on 2018/4/16.
 */

public class LoginContract{

    interface Presenter extends BasePresenter{
        /**
         * 登录点击处理
         */
        void login(String username, String password);
        /**
         * 账号注册点击处理
         */
        void register(String regUsername, String regPassword);

        /**
         * 忘记密码点击处理
         */
        void forgetPassword();

        /**
         * 第三方账号登录点击处理
         */
        void orderUsername();
    }

    interface View extends BaseView<Presenter> {
        /**
         * 要持有的Presenter
         * @param presenter
         */
        void setPresenter(LoginContract.Presenter presenter);

        /**
         * 登录失败
         */
        void loginFailed(String message);
        /**
         * 登录成功
         */
        void loginSuccess(String string);
        /**
         * 成功注册
         */
        void registerSuccess(String s);
        /**
         * 注册失败
         */
        void registerFailed(String s);
    }
}
