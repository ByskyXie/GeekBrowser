package com.jxnu.cure.geekbrowser;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by asus on 2017/10/29.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private Context context;
    private final String DEFTABLE = "CREATE TABLE USER_WEBSITE(" +
            "URI TEXT PRIMARY KEY," +
            "TITLE TEXT NOT NULL," +
            "IMG_ID INTEGER );";

    public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DEFTABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
