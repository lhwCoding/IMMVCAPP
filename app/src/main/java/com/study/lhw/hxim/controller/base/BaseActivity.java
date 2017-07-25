package com.study.lhw.hxim.controller.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * Created by liuhongwu on 17/7/25.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initWindows();

        if (initArgs(getIntent().getExtras())) {
            getContentLayoutId();
            setContentView(getContentLayoutId());
            initWidget();
            initData();
        }else {
            finish();
        }

    }

    /**
     * 初始化窗口
     */
    protected  void initWindows(){}

    /**
     * init data
     */
    protected void initData(){}

    /**
     * 初始化参数
     * @param bundle
     * @return
     */
    protected  boolean initArgs(Bundle bundle){
        return  true;

    }
    /**
     * get resource id
     * @return id
     */
    protected abstract int getContentLayoutId();

    protected void initWidget(){}

    @Override
    public boolean onSupportNavigateUp() {
        //当点击界面导航返回时,Finish 当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //捕获返回键
        List<Fragment> list=getSupportFragmentManager().getFragments();
        if (list!=null && list.size()>0){
            for (Fragment fragment : list) {
                if (fragment instanceof BaseFragment){
                    if (((BaseFragment) fragment).onBackPressed())
                        return;
                }
            }
        }
        finish();
    }
}
