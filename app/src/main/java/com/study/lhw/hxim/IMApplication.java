package com.study.lhw.hxim;

import android.app.Application;
import android.content.Context;

import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.study.lhw.hxim.model.Model;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by liuhongwu on 17/7/25.
 */

public class IMApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        //init EaseUI
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false); //设置需要同意后接受邀请
        options.setAutoAcceptGroupInvitation(false);//设置需要同意后接受群邀请
        EaseUI.getInstance().init(this, options);

        //初始化模型层类
        Model.getInstance().init(this);
        mContext = this;

        EventBus eventBus = new EventBus();
        //下面这一条的效果是完全一样的
       // EventBus eventBus = EventBus.builder().build();
        //修改默认实现的配置，记住，必须在第一次EventBus.getDefault()之前配置，且只能设置一次。建议在application.onCreate()调用
        EventBus.builder().throwSubscriberException(BuildConfig.DEBUG).installDefaultEventBus();



    }

    public static Context getGlobalApplication() {
        return mContext;
    }
}
