package com.study.lhw.hxim;

import android.app.Application;

import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.study.lhw.hxim.model.Model;

/**
 * Created by liuhongwu on 17/7/25.
 */

public class IMApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //init EaseUI
        EMOptions options=new EMOptions();
        options.setAcceptInvitationAlways(false); //设置需要同意后接受邀请
        options.setAutoAcceptGroupInvitation(false);//设置需要同意后接受群邀请
        EaseUI.getInstance().init(this,options);

        //初始化模型层类
        Model.getInstance().init(this);


    }
}
