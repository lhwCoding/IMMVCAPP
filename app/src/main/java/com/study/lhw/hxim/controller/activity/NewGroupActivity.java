package com.study.lhw.hxim.controller.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.study.lhw.hxim.R;
import com.study.lhw.hxim.controller.base.BaseActivity;
import com.study.lhw.hxim.model.Model;
import com.study.lhw.hxim.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by liuhongwu on 17/7/27.
 */

public class NewGroupActivity extends BaseActivity {

    @BindView(R.id.et_newgroup_name)
    EditText et_newgroup_name;
    @BindView(R.id.et_newgroup_desc)
    EditText et_newgroup_desc;
    @BindView(R.id.cb_newgroup_public)
    CheckBox cb_newgroup_public;
    @BindView(R.id.cb_newgroup_invite)
    CheckBox cb_newgroup_invite;
    @BindView(R.id.bt_newgroup_create)
    Button bt_newgroup_create;


    @Override
    protected void initData() {
        super.initData();

        //注册事件
        EventBus.getDefault().register(this);

    }


    @OnClick(R.id.bt_newgroup_create)
    public void onCLick() {
        // 跳转到选择联系人页面
        Intent intent = new Intent(NewGroupActivity.this, PickContactActivity.class);

        startActivityForResult(intent, 1);


    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RESULT_OK) {
//
//            ToastUtils.show(NewGroupActivity.this, "开始创建群!");
//            // 创建群
//            createGroup(data.getStringArrayExtra("members"));
//
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(String[] memberses){

        ToastUtils.show(NewGroupActivity.this, "开始创建群!!!");
        // 创建群
       createGroup(memberses);
    }

    private void createGroup(final String[] memberses) {

        // 群名称
        final String groupName = et_newgroup_name.getText().toString();
        // 群描述
        final String groupDesc = et_newgroup_desc.getText().toString();

        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //去环信服务器创建群
                // 参数一：群名称；参数二：群描述；参数三：群成员；参数四：原因；参数五：参数设置

                EMGroupOptions options=new EMGroupOptions();
                EMGroupManager.EMGroupStyle groupStyle = null;


                options.maxUsers=200;//群最多容纳多少人

                if (cb_newgroup_public.isChecked()) {//公开
                    if (cb_newgroup_invite.isChecked()) {// 开放群邀请
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                    } else {
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                    }
                } else {
                    if (cb_newgroup_invite.isChecked()) {// 开放群邀请
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                    } else {
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                    }
                }
                options.style = groupStyle; // 创建群的类型

                try {

                    EMClient.getInstance().groupManager().createGroup(groupName, groupDesc, memberses, "申请加入群", options);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.show(NewGroupActivity.this, "创建群成功!");
                            finish();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.show(NewGroupActivity.this, "创建群失败!");
                        }
                    });
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册事件
        EventBus.getDefault().unregister(this);

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_new_group;
    }
}
