package com.study.lhw.hxim.controller.activity;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.study.lhw.hxim.R;
import com.study.lhw.hxim.controller.adapter.GroupDetailAdapter;
import com.study.lhw.hxim.controller.base.BaseActivity;
import com.study.lhw.hxim.model.Model;
import com.study.lhw.hxim.model.bean.UserInfo;
import com.study.lhw.hxim.utils.Constant;
import com.study.lhw.hxim.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by liuhongwu on 17/7/27.
 */

public class GroupDetailActivity extends BaseActivity {
    @BindView(R.id.gv_groupdetail)
     GridView gv_groupdetail;
    @BindView(R.id.bt_groupdetail_out)
     Button bt_groupdetail_out;
    private EMGroup mGroup;
    private List<UserInfo> mUsers;
    private GroupDetailAdapter groupDetailAdapter;
    private GroupDetailAdapter.OnGroupDetailListener mOnGroupDetailListener = new GroupDetailAdapter.OnGroupDetailListener() {
        @Override
        public void onAddMembers() {// 添加群成员
            // 跳转到选择联系人页面
            Intent intent = new Intent(GroupDetailActivity.this, PickContactActivity.class);

            // 传递群id
            intent.putExtra(Constant.GROUP_ID, mGroup.getGroupId());

            startActivityForResult(intent, 2);
        }

        @Override
        public void onDeleteMember(final UserInfo user) {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 从环信服务器中删除此人
                        EMClient.getInstance().groupManager().removeUserFromGroup(mGroup.getGroupId(), user.getHxid());

                        // 更新页面
                        getMembersFromHxServer();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.show(GroupDetailActivity.this, "删除群成员成功!");
                            }
                        });

                    } catch (final HyphenateException e) {
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.show(GroupDetailActivity.this, "删除群成员失败!");
                            }
                        });
                    }
                }

            });
        }
    };


    private void getMembersFromHxServer() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 从环信服务器获取所有的群成员信息
                    EMGroup emGroup = EMClient.getInstance().groupManager().getGroupFromServer(mGroup.getGroupId());
                    List<String> members = emGroup.getMembers();

                    if (members != null && members.size() >= 0) {
                        mUsers = new ArrayList<UserInfo>();

                        // 转换
                        for (String member : members) {
                            UserInfo userInfo = new UserInfo(member);
                            mUsers.add(userInfo);
                        }
                    }

                    // 更新页面
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 刷新适配器
                            groupDetailAdapter.refresh(mUsers);
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.show(GroupDetailActivity.this, "获取群信息失败" + e.toString());
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // 获取返回的准备邀请的群成员信息
            final String[] memberses = data.getStringArrayExtra("members");

            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 去环信服务器，发送邀请信息
                        EMClient.getInstance().groupManager().addUsersToGroup(mGroup.getGroupId(), memberses);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailActivity.this, "发送群邀请成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailActivity.this, "发送群邀请失败" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }
    @Override
    protected void initWidget() {
        super.initWidget();

        getData();

        initListener();
    }


    private void initListener() {
        gv_groupdetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:

                        // 判断当前是否是删除模式,如果是删除模式
                        if(groupDetailAdapter.ismIsDeleteModel()) {
                            // 切换为非删除模式
                            groupDetailAdapter.setmIsDeleteModel(false);

                            // 刷新页面
                            groupDetailAdapter.notifyDataSetChanged();
                        }
                        break;
                }

                return false;
            }
        });
    }
    @Override
    protected void initData() {
        super.initData();

        // 初始化button显示
        initButtonDisplay();

        // 初始化gridview
        initGridview();

        // 从环信服务器获取所有的群成员
        getMembersFromHxServer();
    }

    private void initGridview() {
        // 当前用户是群组 || 群公开了
        boolean isCanModify = EMClient.getInstance().getCurrentUser().equals(mGroup.getOwner()) || mGroup.isPublic();

        groupDetailAdapter = new GroupDetailAdapter(this, isCanModify, mOnGroupDetailListener);

        gv_groupdetail.setAdapter(groupDetailAdapter);
    }


        private void initButtonDisplay() {

            // 判断当前用户是否是群主
            if (EMClient.getInstance().getCurrentUser().equals(mGroup.getOwner())) {// 群主

                bt_groupdetail_out.setText("解散群");

            } else {// 群成员
                bt_groupdetail_out.setText("退群");



            }
        }

    @OnClick(R.id.bt_groupdetail_out)
    public  void  onClick(){
        // 判断当前用户是否是群主
        if (EMClient.getInstance().getCurrentUser().equals(mGroup.getOwner())) {// 群主
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 去环信服务器解散群
                        EMClient.getInstance().groupManager().destroyGroup(mGroup.getGroupId());

                        // 发送退群的广播
                        exitGroupBroatCast();

                        // 更新页面
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.show(GroupDetailActivity.this, "解散群成功");

                                // 结束当前页面
                                finish();
                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.show(GroupDetailActivity.this, "解散群失败" + e.toString());
                            }
                        });
                    }
                }
            });

        }else{
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 告诉环信服务器退群
                        EMClient.getInstance().groupManager().leaveGroup(mGroup.getGroupId());

                        // 发送退群广播
                        exitGroupBroatCast();

                        // 更新页面
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.show(GroupDetailActivity.this, "退群成功");


                                finish();
                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.show(GroupDetailActivity.this, "退群失败" + e.toString());

                            }
                        });
                    }
                }
            });

        }


        }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_group_detail;
    }

    // 发送退群和解散群广播
    private void exitGroupBroatCast() {
        LocalBroadcastManager mLBM = LocalBroadcastManager.getInstance(GroupDetailActivity.this);

        Intent intent = new Intent(Constant.EXIT_GROUP);

        intent.putExtra(Constant.GROUP_ID, mGroup.getGroupId());

        mLBM.sendBroadcast(intent);
    }

    // 获取传递过来的数据
    private void getData() {
        Intent intent = getIntent();
        String groupId = intent.getStringExtra(Constant.GROUP_ID);

        if (groupId == null) {
            return;
        } else {
            mGroup = EMClient.getInstance().groupManager().getGroup(groupId);
        }
    }
}
