package com.example.gdei.park.data;

import android.content.Context;

import com.example.gdei.park.data.homeF1.HomeFragment1Repository;
import com.example.gdei.park.data.homeF1.SeekF1Repository;
import com.example.gdei.park.data.homeF1.SeekF1Source;
import com.example.gdei.park.data.loginModel.LoginModelRepository;

/**
 * Created by gdei on 2018/4/15.
 */

public class Injection {
    public static LoginModelRepository provideLoginRespository(){
        return LoginModelRepository.getInstance();
    }
    public static HomeFragment1Repository providerHomeF1Repository(){
        return HomeFragment1Repository.getInstance();
    }
    public static SeekF1Source providerSeekF1Respository(Context context){
        return SeekF1Repository.getInstance(context);
    }
}
