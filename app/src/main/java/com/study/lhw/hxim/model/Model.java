package com.study.lhw.hxim.model;

import android.content.Context;

import com.study.lhw.hxim.model.dao.UserAccountDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liuhongwu on 17/7/25.
 * 数据模型全局类
 */

public class Model {

    private Context mContext;
    //获取线程池
    private ExecutorService executorService= Executors.newCachedThreadPool();

    private static Model model=new Model();
    private UserAccountDao userAccountDao;

    private  Model(){}

    public static Model getInstance(){
        return  model;
    }

    public  void init(Context context){
        mContext=context;
        userAccountDao = new UserAccountDao(mContext);
    }

    public  ExecutorService getGlobalThreadPool(){

        return executorService;
    }

    /**
     * login success
     */
    public void loginSuccess() {
    }
    //获取用户数据库额操作类
    public UserAccountDao getUserAccountDao(){
        return userAccountDao;
    }
}
