package com.mvp.rui.android_greendao_demo.database;

import android.app.Application;
import android.content.Context;

import com.mvp.rui.android_greendao_demo.table_model.DaoMaster;
import com.mvp.rui.android_greendao_demo.table_model.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by 0200030 on 2018/3/20.
 */
public class DataBaseManager {
    /**
     * A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
     */
    public static final String DB_NAME = "greendao.db";
    public static final boolean ENCRYPTED = false;
    private static DataBaseManager mDataBaseManager;

    private DaoSession daoSession;


    public static DataBaseManager create(Application application) {
        if (mDataBaseManager == null) {
            mDataBaseManager = new DataBaseManager();
            mDataBaseManager.init(application);
        }
        return mDataBaseManager;
    }


//    public static DataBaseManager get(Context context) {
//        return get(context.getApplicationContext());
//    }
//
//    public static DataBaseManager get(Application application) {
//        if (mDataBaseManager == null) {
//            return create(application);
//        }
//        return get();
//    }
//
//    private static DataBaseManager get() {
//        return mDataBaseManager;
//    }


    public static DaoSession getDaoSession() {
        if (mDataBaseManager != null)
            return mDataBaseManager.daoSession;
        return null;
    }


    public static DaoSession getDaoSession(Context context) {
        if (mDataBaseManager == null || mDataBaseManager.daoSession == null) {
            mDataBaseManager = null;
            create((Application) context.getApplicationContext());
        }
        return getDaoSession();
    }

    public void init(Application application) {

        DevOpenHelper helper = new DevOpenHelper(application, DB_NAME);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        
    }


}
