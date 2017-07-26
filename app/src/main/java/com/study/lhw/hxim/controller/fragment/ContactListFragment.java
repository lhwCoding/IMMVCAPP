package com.study.lhw.hxim.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.study.lhw.hxim.R;
import com.study.lhw.hxim.controller.activity.AddContactActivity;
import com.study.lhw.hxim.controller.activity.InviteActivity;
import com.study.lhw.hxim.utils.Constant;
import com.study.lhw.hxim.utils.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuhongwu on 17/7/25.
 */

public class ContactListFragment extends EaseContactListFragment {

    @BindView(R.id.iv_contact_red)
    ImageView iv_contact_red;
    @BindView(R.id.ll_contact_invite)
    LinearLayout ll_contact_invite;
    private LocalBroadcastManager mLBM;
    private String mHxid;


    private BroadcastReceiver ContactInviteChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // 更新红点显示
            iv_contact_red.setVisibility(View.VISIBLE);
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

        }
    };

    private BroadcastReceiver ContactChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 刷新页面
            refreshContact();
        }


    };

    private void refreshContact() {

    }

    private BroadcastReceiver GroupChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 显示红点
            iv_contact_red.setVisibility(View.VISIBLE);
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);
        }
    };

    @Override
    protected void initView() {
        super.initView();
        //添加加号
        titleBar.setRightImageResource(R.drawable.em_add);
        //添加头布局
        View inflate = View.inflate(getActivity(), R.layout.header_fragment_contact, null);
        ButterKnife.bind(this, inflate);
        listView.addHeaderView(inflate);
    }

    @Override
    protected void setUpView() {
        super.setUpView();

        // 添加按钮的点击事件处理
        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddContactActivity.class);
                startActivity(intent);
            }
        });

        //初始化红点
        boolean isNewInvite = SpUtils.getInstance().getBoolean(SpUtils.IS_NEW_INVITE, false);
        iv_contact_red.setVisibility(isNewInvite ? View.VISIBLE : View.GONE);



        mLBM = LocalBroadcastManager.getInstance(getActivity());
        mLBM.registerReceiver(ContactInviteChangeReceiver, new IntentFilter(Constant.CONTACT_INVITE_CHANGED));
        mLBM.registerReceiver(ContactChangeReceiver, new IntentFilter(Constant.CONTACT_CHANGED));
        mLBM.registerReceiver(GroupChangeReceiver, new IntentFilter(Constant.GROUP_INVITE_CHANGED));
    }


    @OnClick(R.id.ll_contact_invite)
    public void onClick(){
        // 红点处理
        iv_contact_red.setVisibility(View.GONE);
        SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, false);

        // 跳转到邀请信息列表页面
        Intent intent = new Intent(getActivity(), InviteActivity.class);

        startActivity(intent);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mLBM.unregisterReceiver(ContactInviteChangeReceiver);
        mLBM.unregisterReceiver(ContactChangeReceiver);
        mLBM.unregisterReceiver(GroupChangeReceiver);
    }
}
