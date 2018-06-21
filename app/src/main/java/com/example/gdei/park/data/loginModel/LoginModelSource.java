package com.example.gdei.park.data.loginModel;

/**
 * Created by gdei on 2018/4/16.
 */

public interface LoginModelSource {
    /**
     * 登录点击处理
     */
    void login(String username, String password,Callback callback);
    /**
     * 账号注册点击处理
     */
    void register(String regUsername, String regPassword, LoginModelSource.Callback callback);

    /**
     * 忘记密码点击处理
     */
    void forgetPassword();

    /**
     * 第三方账号登录点击处理
     */
    void orderUsername();

    interface Callback{
        void loginSuccess(String str);
        void loginFailed(String str);
    }
}
