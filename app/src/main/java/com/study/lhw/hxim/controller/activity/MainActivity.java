package com.study.lhw.hxim.controller.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.RadioGroup;

import com.study.lhw.hxim.R;
import com.study.lhw.hxim.controller.fragment.ChatFragment;
import com.study.lhw.hxim.controller.fragment.ContactListFragment;
import com.study.lhw.hxim.controller.fragment.SettingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {


@BindView(R.id.rg_main)
public RadioGroup rg_main;

    private ChatFragment chatFragment;
    private ContactListFragment contactListFragment;
    private SettingFragment settingFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();

        initListener();
    }

    private void initListener() {

        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                Fragment fragment = null;

                switch (checkedId) {
                    // 会话列表页面
                    case R.id.rb_main_chat:
                        fragment = chatFragment;
                        break;

                    // 联系人列表页面
                    case R.id.rb_main_contact:
                        fragment = contactListFragment;
                        break;

                    // 设置页面
                    case R.id.rb_main_setting:
                        fragment = settingFragment;
                        break;
                }

                // 实现fragment切换的方法
                switchFragment(fragment);

            }
        });
        //默认界面
        rg_main.check(R.id.rb_main_chat);
    }

    private void initData() {
        // 创建三个fragment对象
        chatFragment = new ChatFragment();
        contactListFragment = new ContactListFragment();
        settingFragment = new SettingFragment();
    }
    private void switchFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_main, fragment).commit();
    }


}
