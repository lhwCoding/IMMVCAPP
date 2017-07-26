package com.study.lhw.hxim.controller.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Button;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.study.lhw.hxim.R;
import com.study.lhw.hxim.controller.activity.LoginActivity;
import com.study.lhw.hxim.controller.base.BaseFragment;
import com.study.lhw.hxim.model.Model;
import com.study.lhw.hxim.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by liuhongwu on 17/7/25.
 */

public class SettingFragment extends BaseFragment {

    @BindView(R.id.bt_setting_out) Button bt_setting_out;



    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initData() {
        super.initData();

        // 在button上显示当前用户名称
        bt_setting_out.setText("退出登录（" + EMClient.getInstance().getCurrentUser() + ")");
    }

    @OnClick(R.id.bt_setting_out)
    public void onClick(){
        ToastUtils.show(getActivity(),"退出");

        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.show(getActivity(),"退出成功!");

                                //返回登录页面
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                getActivity().startActivity(intent);
                                getActivity().finish();
                            }
                        });

                    }

                    @Override
                    public void onError(int i, final String s) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.show(getActivity(),"退出失败!"+s);


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
}
