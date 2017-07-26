package com.study.lhw.hxim.controller.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.study.lhw.hxim.R;
import com.study.lhw.hxim.controller.base.BaseActivity;
import com.study.lhw.hxim.model.Model;
import com.study.lhw.hxim.model.bean.UserInfo;
import com.study.lhw.hxim.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**添加联系人
 * @author liuhongwu
 */
public class AddContactActivity extends BaseActivity {

    @BindView(R.id.tv_add_find)
     TextView tv_add_find;
    @BindView(R.id.et_add_name)
     EditText et_add_name;
    @BindView(R.id.rl_add)
     RelativeLayout rl_add;
    @BindView(R.id.tv_add_name)
     TextView tv_add_name;
    @BindView(R.id.bt_add_add)
    Button bt_add_add;
    private UserInfo userInfo;

    @OnClick({R.id.tv_add_find,R.id.bt_add_add})
    public  void onClick(View view){
     if (view.getId()==R.id.bt_add_add){
         add();

     }else{
         find();
     }

    }

    private void add() {
          Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
              @Override
              public void run() {
                  //环信服务器添加好友
                  try {
                      EMClient.getInstance().contactManager().addContact(userInfo.getName(),"添加好友");
                      runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              ToastUtils.show(AddContactActivity.this,"添加好友成功!!!");

                          }
                      });
                  } catch (HyphenateException e) {
                     // e.printStackTrace();
                      runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              ToastUtils.show(AddContactActivity.this,"添加好友失败!!!");

                          }
                      });
                  }
              }
          });
    }

    private void find() {
        final String name = et_add_name.getText().toString();

        if (TextUtils.isEmpty(name)){
            ToastUtils.show(AddContactActivity.this,"输入名称不能为空!!!");
            return;
        }
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                 userInfo = new UserInfo(name);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rl_add.setVisibility(View.VISIBLE);
                        tv_add_name.setText(userInfo.getName());
                    }
                });
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_add_contact;
    }
}
