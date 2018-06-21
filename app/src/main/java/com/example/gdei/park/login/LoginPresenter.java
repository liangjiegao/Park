package com.example.gdei.park.login;

import com.example.gdei.park.data.loginModel.LoginModelRepository;
import com.example.gdei.park.data.loginModel.LoginModelSource;

/**
 * Created by gdei on 2018/4/16.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private LoginActivity view;     //P 持有 V
    private LoginModelRepository repository;    //P 持有 M

    public LoginPresenter( LoginModelRepository repository, LoginActivity view){
        this.view = view;
        this.repository = repository;
    }
    @Override
    public void start() {

    }

    @Override
    public void login(String username, String password) {
        repository.login(username, password, new LoginModelSource.Callback() {
            @Override
            public void loginSuccess(String str) {
                    view.loginSuccess(str);
            }

            @Override
            public void loginFailed(String str) {
                view.loginFailed(str);
            }
        });
    }

    @Override
    public void register(String regUsername, String regPassword) {
        repository.register(regUsername, regPassword, new LoginModelSource.Callback() {
            @Override
            public void loginSuccess(String str) {
                view.registerSuccess(str);
            }

            @Override
            public void loginFailed(String str) {
                view.registerFailed(str);
            }
        });
    }

    @Override
    public void forgetPassword() {

    }

    @Override
    public void orderUsername() {

    }
}
