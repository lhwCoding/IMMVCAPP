package com.study.lhw.hxim.controller.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by liuhongwu on 17/7/25.
 */

public abstract class BaseFragment extends Fragment {

    protected  View mRoot;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //初始化参数
     initArgs(getArguments());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int id=getContentLayoutId();
        //初始化界面的时候
        if (mRoot==null){
            View root = inflater.inflate(id, container, false);
            initWidget(root);
            mRoot=root;

        }else{
            if (mRoot.getParent()!=null){
                ( (ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }


        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //view 创建完成后初始数据
        initData();
    }

    /**
     * 初始化窗口
     */
    protected  void initWindows(){}

    /**
     * init data
     */
    protected void initData(){}


    protected  void initArgs(Bundle bundle){


    }
    /**
     * get resource id
     * @return id
     */
    protected abstract int getContentLayoutId();

    protected void initWidget(View  root){}

    /**
     * 返回按键时触发
     * @return 返回true fragment自己处理 activity不用自己处理,返回false反之
     */
    public  boolean onBackPressed(){
        return  false;
    }
}

