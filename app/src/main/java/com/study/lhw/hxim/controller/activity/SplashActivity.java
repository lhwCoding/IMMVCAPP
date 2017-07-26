package com.study.lhw.hxim.controller.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.hyphenate.chat.EMClient;
import com.study.lhw.hxim.R;
import com.study.lhw.hxim.controller.base.BaseActivity;
import com.study.lhw.hxim.model.Model;
import com.study.lhw.hxim.model.bean.UserInfo;

/**
 * Created by liuhongwu on 17/7/25.
 */

public class SplashActivity extends BaseActivity {

    Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (isFinishing()) {
                return;
            }

            toMainOrLogin();


        }


    };

    private void toMainOrLogin() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //
                if (EMClient.getInstance().isLoggedInBefore()) {//登录过

                    //获取当前用户登录信息
                    UserInfo account = Model.getInstance().getUserAccountDao().getAccountByHxId(EMClient.getInstance().getCurrentUser());
                    if (account!=null){
                        // 登录成功后的方法
                        Model.getInstance().loginSuccess(account);

                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{

                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {//未登录过

                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
        super.initData();
        mHander.sendMessageDelayed(Message.obtain(), 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHander.removeCallbacksAndMessages(null);
    }
}
