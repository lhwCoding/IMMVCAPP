package com.study.lhw.hxim.utils;

import android.util.Log;

/**
 * Created by liuhongwu on 17/7/26.
 * LEVEL = VERBOSE时打印所有调试信息
 * 当项目上线时，改为 LEVEL = NOTHING
 * 关闭所有打印信息
 */

public class LogUtils {

    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    public static final int LEVEL = VERBOSE;//打印所有等级的信息
//  public static final int LEVEL = NOTHING;//关闭所有等级的信息

    public static void v(String tag,String msg){
        if(LEVEL <= VERBOSE){
            Log.v(tag,msg);
        }
    }
    public static void d(String tag,String msg){
        if(LEVEL <= DEBUG){
            Log.d(tag,msg);
        }
    }
    public static void i(String tag,String msg){
        if(LEVEL <= INFO){
            Log.i(tag,msg);
        }
    }
    public static void w(String tag,String msg){
        if(LEVEL <= WARN){
            Log.w(tag,msg);
        }
    }
    public static void e(String tag,String msg){
        if(LEVEL <= ERROR){
            Log.e(tag,msg);
        }
    }
}
