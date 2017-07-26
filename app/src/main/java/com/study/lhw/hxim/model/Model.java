package com.study.lhw.hxim.model;

import android.content.Context;

import com.study.lhw.hxim.model.bean.UserInfo;
import com.study.lhw.hxim.model.dao.UserAccountDao;
import com.study.lhw.hxim.model.db.DBManager;

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
    private DBManager dbManager;

    private  Model(){}

    public static Model getInstance(){
        return  model;
    }

    public  void init(Context context){
        mContext=context;
        userAccountDao = new UserAccountDao(mContext);

        //全局监听
        EventListener eventListener = new EventListener(mContext);

    }

    public  ExecutorService getGlobalThreadPool(){

        return executorService;
    }
    // 用户登录成功后的处理方法
    public void loginSuccess(UserInfo account) {

        // 校验
        if(account == null) {
            return;
        }


        if(dbManager != null) {
            dbManager.close();
        }

        dbManager = new DBManager(mContext, account.getName());
    }

    public DBManager getDbManager(){
        return dbManager;
    }
    //获取用户数据库额操作类
    public UserAccountDao getUserAccountDao(){
        return userAccountDao;
    }


}
