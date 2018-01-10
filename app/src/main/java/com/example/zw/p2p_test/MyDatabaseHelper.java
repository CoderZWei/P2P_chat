package com.example.zw.p2p_test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zw on 2016/10/26.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_Database="create table message_data("
            +"id integer primary key autoincrement,"
            +"content text ,"
            +"type integer "
            +")";
    private Context mcontext;
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mcontext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL(CREATE_Database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

      }
}
