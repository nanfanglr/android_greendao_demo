package com.mvp.rui.android_greendao_demo;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.mvp.rui.android_greendao_demo.database.DataBaseManager;

/**
 * Created by 0200030 on 2018/3/20.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据库
        DataBaseManager.create(this);
        //初始化调试工具
        Stetho.initializeWithDefaults(this);
    }
}
