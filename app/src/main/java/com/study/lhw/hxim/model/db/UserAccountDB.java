package com.study.lhw.hxim.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.study.lhw.hxim.model.dao.UserAccountTable;

/**
 * Created by liuhongwu on 17/7/26.
 */

public class UserAccountDB extends SQLiteOpenHelper {


    public UserAccountDB(Context context) {
        super(context, "account.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建数据库表的语句
        db.execSQL(UserAccountTable.CREATE_TAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
