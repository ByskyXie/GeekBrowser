package com.jxnu.cure.geekbrowser;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by asus on 2017/10/29.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private Context context;
    private final String SYS_WEBSITE = "CREATE TABLE SYS_WEBSITE(" +
            "URI TEXT PRIMARY KEY," +
            "TITLE TEXT NOT NULL," +
            "IMG_ID INTEGER" +
            ");";

    private final String USER_WEBSITE = "CREATE TABLE USER_WEBSITE(" +
            "URI TEXT PRIMARY KEY," +
            "TITLE TEXT NOT NULL," +
            "IMG_ID INTEGER );";

    public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SYS_WEBSITE);
        db.execSQL(USER_WEBSITE);
        addSystemWebsite(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO:将来要记录版本号
    }
    public void addSystemWebsite(SQLiteDatabase db){
        ArrayList<BaseWebsite> list = BaseWebsite.getBaseWebsite();
        for(int i=0,num=list.size() ; i<num ; i++){
            BaseWebsite bw = list.get(i);
            ContentValues values = new ContentValues();
            values.put("URI",bw.getUri());
            values.put("TITLE",bw.getTitle());
            values.put("IMG_ID",bw.getImg_id());
            db.insert("SYS_WEBSITE","IMG_ID",values);
        }
    }
}
