package com.study.lhw.hxim.controller.fragment;

import android.content.Intent;
import android.view.View;

import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.study.lhw.hxim.R;
import com.study.lhw.hxim.controller.activity.AddContactActivity;

/**
 * Created by liuhongwu on 17/7/25.
 */

public class ContactListFragment extends EaseContactListFragment {

    @Override
    protected void initView() {
        super.initView();
        //添加加号
        titleBar.setRightImageResource(R.drawable.em_add);
        //添加头布局
        View inflate = View.inflate(getActivity(), R.layout.header_fragment_contact, null);
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
    }
}
