package com.study.lhw.hxim.controller.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.study.lhw.hxim.R;
import com.study.lhw.hxim.controller.adapter.PickContactAdapter;
import com.study.lhw.hxim.controller.base.BaseActivity;
import com.study.lhw.hxim.model.Model;
import com.study.lhw.hxim.model.bean.PickContactInfo;
import com.study.lhw.hxim.model.bean.UserInfo;
import com.study.lhw.hxim.utils.Constant;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by liuhongwu on 17/7/27.
 */

public class PickContactActivity extends BaseActivity {
    @BindView(R.id.tv_pick_save)
    TextView tv_pick_save;
    @BindView(R.id.lv_pick)
    ListView lv_pick;

    private List<PickContactInfo> mPicks;
    private PickContactAdapter pickContactAdapter;
    private List<String> mExistMembers;

    @Override
    protected void initWidget() {
        super.initWidget();

        getData();

        initListener();
    }


    private void getData() {
        String groupId = getIntent().getStringExtra(Constant.GROUP_ID);

        if (groupId != null) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
            // 获取群众已经存在的所有群成员
            mExistMembers = group.getMembers();
        }

        if (mExistMembers == null) {
            mExistMembers = new ArrayList<>();
        }
    }

    private void initListener() {
        // listview条目点击事件
        lv_pick.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // checkbox的切换
                CheckBox cb_pick = (CheckBox) view.findViewById(R.id.cb_pick);
                cb_pick.setChecked(!cb_pick.isChecked());

                // 修改数据
                PickContactInfo pickContactInfo = mPicks.get(position);
                pickContactInfo.setIsChecked(cb_pick.isChecked());

                // 刷新页面
                pickContactAdapter.notifyDataSetChanged();
            }
        });

        // 保存按钮的点击事件
        tv_pick_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取到已经选择的联系人
                List<String> names = pickContactAdapter.getPickContacts();

                // 给启动页面返回数据
               // Intent intent = new Intent(PickContactActivity.this,NewGroupActivity.class);

              //  intent.putExtra("members", names.toArray(new String[0]));

                // 设置返回的结果码
                //setResult(RESULT_OK, intent);
                //发送事件
               EventBus.getDefault().post(names.toArray(new String[0]));
                // 结束当前页面
                finish();
            }
        });
    }
    @Override
    protected void initData() {
        super.initData();
        // 从本地数据库中获取所有的联系人信息
        List<UserInfo> contacts = Model.getInstance().getDbManager().getContactTableDao().getContacts();

        mPicks = new ArrayList<>();

        if (contacts != null && contacts.size() >= 0) {
            // 转换
            for (UserInfo contact : contacts) {
                PickContactInfo pickContactInfo = new PickContactInfo(contact, false);
                mPicks.add(pickContactInfo);
            }
        }

        // 初始化listview
        pickContactAdapter = new PickContactAdapter(this, mPicks, mExistMembers);

        lv_pick.setAdapter(pickContactAdapter);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_pick_contact;
    }
}
