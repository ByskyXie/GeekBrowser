package com.jxnu.cure.geekbrowser;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by asus on 2017/10/29.
 */

public class BaseActivity extends AppCompatActivity {
    protected static SQLiteDatabase database;
    protected static DatabaseOpenHelper openHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(openHelper == null){
            openHelper = new DatabaseOpenHelper(this,"Browser.db",null,1);
            database = openHelper.getWritableDatabase();

        }
    }
    public DatabaseOpenHelper getOpenHelper(){ return openHelper;}

    protected void initialUI(){

    }
}

