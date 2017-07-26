package com.study.lhw.hxim.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.study.lhw.hxim.R;
import com.study.lhw.hxim.controller.base.BaseActivity;
import com.study.lhw.hxim.model.Model;
import com.study.lhw.hxim.model.bean.UserInfo;
import com.study.lhw.hxim.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuhongwu on 17/7/25.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_login_name)
    EditText et_login_name;
    @BindView(R.id.et_login_pwd)
    EditText et_login_pwd;
    @BindView(R.id.bt_login_regist)
    Button bt_login_regist;
    @BindView(R.id.bt_login_login)
    Button bt_login_login;
    private String loggin_name;
    private String login_pwd;


    @OnClick({R.id.bt_login_login, R.id.bt_login_regist})
    public void onClick(View view) {

        loggin_name = et_login_name.getText().toString();
        login_pwd = et_login_pwd.getText().toString();

        if (view.getId() == R.id.bt_login_login) {
            login();
        } else {
            regist();
        }
    }

    private void login() {

        if (TextUtils.isEmpty(loggin_name) || TextUtils.isEmpty(login_pwd)) {
            ToastUtils.show(LoginActivity.this, "输入的用户名或密码不能为空!!!");
            return;
        }
        //环信服务器登录验证
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {


                EMClient.getInstance().login(loggin_name, login_pwd, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        //对模型数据进行处理
                        Model.getInstance().loginSuccess();
                        //保存账户到本地数据库
                        Model.getInstance().getUserAccountDao().addAccount(new UserInfo(loggin_name));



                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.show(LoginActivity.this, "登录成功!!!");

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });


                    }

                    @Override
                    public void onError(int i, final String s) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.show(LoginActivity.this, "登录失败!!!"+s);

                            }
                        });

                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });

    }

    private void regist() {

        if (TextUtils.isEmpty(loggin_name) || TextUtils.isEmpty(login_pwd)) {
            ToastUtils.show(LoginActivity.this, "输入的用户名或密码不能为空!!!");
            return;
        }


        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(loggin_name, login_pwd);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            ToastUtils.show(LoginActivity.this, "注册成功!!!");
                        }
                    });
                } catch (final HyphenateException e) {
                    //  e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.show(LoginActivity.this, "注册失败!!!" + e.toString());

                        }
                    });

                }
            }
        });


    }


    @Override
    protected void initData() {
        super.initData();


    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_login_acitivity;
    }
}
