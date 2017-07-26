package com.study.lhw.hxim.controller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.study.lhw.hxim.R;
import com.study.lhw.hxim.controller.adapter.InviteAdapter;
import com.study.lhw.hxim.controller.base.BaseActivity;
import com.study.lhw.hxim.model.Model;
import com.study.lhw.hxim.model.bean.InvationInfo;
import com.study.lhw.hxim.utils.Constant;
import com.study.lhw.hxim.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;

/**
 * Created by liuhongwu on 17/7/26.
 */

public class InviteActivity extends BaseActivity{

    @BindView(R.id.lv_invite)
    ListView lv_invite;
    private InviteAdapter inviteAdapter;
    private LocalBroadcastManager mLBM;


    private BroadcastReceiver InviteChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // 刷新页面
            refresh();
        }
    };



    private InviteAdapter.OnInviteListener mOnInviteListener = new InviteAdapter.OnInviteListener(){

        //联系人邀请
        @Override
        public void onAccept(final InvationInfo invationInfo) {
            //通知环信服务器点击接受按钮
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().contactManager().acceptInvitation(invationInfo.getUser().getHxid());
                        //数据库更新
                        Model.getInstance().getDbManager().getInviteTableDao().updateInvitationStatus(InvationInfo.InvitationStatus.INVITE_ACCEPT,invationInfo.getUser().getHxid());

//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                //刷新页面
//                                ToastUtils.show(InviteActivity.this,"接受了邀请!");
//
//                                refresh();
//                            }
//                        });
                        toastShow("接受了邀请");

                    } catch (HyphenateException e) {
                       // e.printStackTrace();
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                //刷新页面
//                                ToastUtils.show(InviteActivity.this,"接受邀请失败!");
//
//                               // refresh();
//                            }
//                        });
                        toastShow("接受邀请失败!");

                    }
                }
            });
        }

        @Override
        public void onReject(final InvationInfo invationInfo) {

            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().contactManager().declineInvitation(invationInfo.getUser().getHxid());
                        //invationInfo.setStatus(InvationInfo.InvitationStatus.INVITE_REJECT);
                        //更新数据库
                        Model.getInstance().getDbManager().getInviteTableDao().removeInvitation(invationInfo.getUser().getHxid());

                        toastShow("拒绝了邀请!");
                    } catch (HyphenateException e) {
                        //e.printStackTrace();
                        toastShow("拒绝失败!");
                    }
                }
            });

        }
       //群邀请
        @Override
        public void onInviteAccept(final InvationInfo invationInfo) {
              Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                  @Override
                  public void run() {
                      try {
                          EMClient.getInstance().groupManager().acceptInvitation(invationInfo.getGroup().getGroupId(),invationInfo.getGroup().getInvatePerson());
                          //更新本地数据库
                          invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_ACCEPT_INVITE);
                          Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);

                          toastShow("接受群邀请成功!");

                      } catch (HyphenateException e) {
                          e.printStackTrace();
                          toastShow("接受群邀请失败!");
                      }

                  }
              });
        }

        @Override
        public void onInviteReject(final InvationInfo invationInfo) {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().declineApplication(invationInfo.getGroup().getGroupId(),invationInfo.getGroup().getInvatePerson(),"拒绝群邀请");
                        //更新本地数据库
                        invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_REJECT_INVITE);
                        Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);

                        toastShow("接受群邀请成功!");

                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        toastShow("接受群邀请失败!");
                    }

                }
            });
        }
        //接受申请
        @Override
        public void onApplicationAccept(final InvationInfo invationInfo) {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().acceptApplication(invationInfo.getGroup().getGroupId(),invationInfo.getGroup().getInvatePerson());
                        //更新本地数据库
                        invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_ACCEPT_APPLICATION);
                        Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);

                        toastShow("接受申请成功!");

                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        toastShow("接受申请失败!");
                    }

                }
            });

        }

        @Override
        public void onApplicationReject(final InvationInfo invationInfo) {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().declineApplication(invationInfo.getGroup().getGroupId(),invationInfo.getGroup().getInvatePerson(),"拒绝群邀请");
                        //更新本地数据库
                        invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_REJECT_APPLICATION);
                        Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);

                        toastShow("接受群邀请成功!");

                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        toastShow("接受群邀请失败!");
                    }

                }
            });

        }
    };

    private void toastShow(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //刷新页面
                ToastUtils.show(InviteActivity.this,msg);

                refresh();
            }
        });
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_invite;
    }

    @Override
    protected void initData() {
        super.initData();
        // 初始化listview
        inviteAdapter = new InviteAdapter(this, mOnInviteListener);

        lv_invite.setAdapter(inviteAdapter);

        // 刷新方法
        refresh();

        // 注册邀请信息变化的广播
        mLBM = LocalBroadcastManager.getInstance(this);
        mLBM.registerReceiver(InviteChangedReceiver, new IntentFilter(Constant.CONTACT_INVITE_CHANGED));
        mLBM.registerReceiver(InviteChangedReceiver, new IntentFilter(Constant.GROUP_INVITE_CHANGED));
    }

    private void refresh() {
        // 获取数据库中的所有邀请信息
        List<InvationInfo> invitations = Model.getInstance().getDbManager().getInviteTableDao().getInvitations();

        // 刷新适配器
        inviteAdapter.refresh(invitations);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLBM.unregisterReceiver(InviteChangedReceiver);

    }
}
