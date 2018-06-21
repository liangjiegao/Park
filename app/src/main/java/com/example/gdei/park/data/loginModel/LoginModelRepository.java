package com.example.gdei.park.data.loginModel;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.gdei.park.login.LoginActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by gdei on 2018/4/16.
 */

public class LoginModelRepository implements LoginModelSource {

    private static LoginModelRepository INSTANCE = null;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private String preUsername, prePassword;

    public LoginModelRepository(){
        preferences = LoginActivity.preferences;
        editor = preferences.edit();
    }

    public static LoginModelRepository getInstance(){

        if (INSTANCE == null){
            INSTANCE = new LoginModelRepository();
        }
        return INSTANCE;
    }

    @Override
    public void login(final String username, final String password, final Callback callback) {

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            callback.loginFailed("密码和账号都不能为空");
            return;
        }
        System.out.println("点击");
        /*
        prePassword = preferences.getString(username, "");
        if (prePassword.equals("")){
            callback.loginFailed("账号不存在，请重新输入！");
        }*/
        final String[] response = {""};
        new Thread(){
            @Override
            public void run() {
                try {
                    System.out.println("发送请求");
                    Socket socket = new Socket("10.2.244.114", 3000);
                    socket.setSoTimeout(10000);

                    String loginMsg =  username+ " " + password;
                    socket.getOutputStream().write(loginMsg.getBytes());
                    BufferedReader bw = new BufferedReader(new InputStreamReader(socket.getInputStream(), "gbk"));


                    response[0] += bw.readLine();
                    System.out.println(response[0]);

                    bw.close();
                    socket.close();
                    //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                } catch (Exception e) {
                    System.out.println("异常");
                    e.printStackTrace();

                }

            }
        }.start();
        //String string = new String(response[0], utf-8);
        if(response[0].equals("是"+username)){
            callback.loginSuccess(response[0]);
        }


        /*else if (!prePassword.equals(password)){

            callback.loginFailed("密码错误！");

        } else if (prePassword.equals(password)) {

            callback.loginSuccess("登录成功！");
        }*/
    }

    @Override
    public void register(String regUsername, String regPassword, LoginModelSource.Callback callback) {
        if (editor == null){
            editor = preferences.edit();
        }
        if (TextUtils.isEmpty(regPassword) || TextUtils.isEmpty(regPassword)){
            callback.loginFailed("用户名和密码必须填写哦！");
            return;
        }else if (preferences.getString(regUsername, "") != ""){
            callback.loginFailed("用户名已存在！");
            return;
        } else if (preferences.getString(regUsername, "") == "") {
            editor.putString(regUsername, regPassword);
            editor.commit();
            callback.loginSuccess("注册成功");
        }
    }

    @Override
    public void forgetPassword() {

    }

    @Override
    public void orderUsername() {

    }
}
