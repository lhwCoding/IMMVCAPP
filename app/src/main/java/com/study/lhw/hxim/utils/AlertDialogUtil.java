package com.study.lhw.hxim.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by liuhongwu on 17/7/26.
 *  使用观察者模式来实现确定结果回调
 *
 * 调用方式：
 *
 * AlertDialogUtil.showDialog(this,"确定删除已上传的图片？");
 * dialogUtil.setDialogPositiveButtonListener(new AlertDialogUtil.DialogPositiveButtonListener() {
 *
 *      @Override
 *     public void setDialogPositiveButtonListener() {
 *
 *    }
 * });
 */

public class AlertDialogUtil {

    private static  DialogPositiveButtonListener listener;


    public static  void showDialog(Context context,String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(message);
        dialog.setCancelable(false);//点击框外取消
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (listener != null) {
                    listener.setDialogPositiveButtonListener();
                }
            }

        });
        dialog.setNegativeButton("取消", null);
        dialog.show();
    }

    public void setDialogPositiveButtonListener(DialogPositiveButtonListener listener) {
        this.listener = listener;
    }

    public interface DialogPositiveButtonListener {
        void setDialogPositiveButtonListener();
    }
}
