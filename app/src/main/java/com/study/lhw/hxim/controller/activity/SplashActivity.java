package com.study.lhw.hxim.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.study.lhw.hxim.R;
import com.study.lhw.hxim.controller.base.BaseActivity;

/**
 * Created by liuhongwu on 17/7/25.
 */

public class SplashActivity  extends BaseActivity{

    private int MSG_RAG=0;
    Handler mHander=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
         if (msg.what==0){
             Intent intent=new Intent(SplashActivity.this,MainActivity.class);
             startActivity(intent);
             finish();
         }

        }
    };



    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
        super.initData();
       mHander.sendEmptyMessageDelayed(MSG_RAG,2000);
    }
}
