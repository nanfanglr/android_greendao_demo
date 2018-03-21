package com.mvp.rui.android_greendao_demo.database;

import android.content.Context;
import android.util.Log;

import com.mvp.rui.android_greendao_demo.table_model.DaoMaster;

import org.greenrobot.greendao.database.Database;

import static com.mvp.rui.android_greendao_demo.database.DataBaseManager.DB_NAME;
import static com.mvp.rui.android_greendao_demo.database.DataBaseManager.ENCRYPTED;

/**
 * Created by rui on 2018/3/20.
 */
public class DevOpenHelper extends DaoMaster.OpenHelper {

    public Database mDatabase;

    public DevOpenHelper(Context context) {
        this(context, DB_NAME);
    }

    public DevOpenHelper(Context context, String name) {
        super(context, name);
        mDatabase = ENCRYPTED ? getEncryptedWritableDb("super-secret") : getWritableDb();
    }


    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
//            super.onUpgrade(db, oldVersion, newVersion);
        Log.i("greenDAO", "migrating schema from version " + oldVersion + " to " + newVersion);
        for (int migrateVersion = oldVersion + 1; migrateVersion <= newVersion; migrateVersion++) {
            switch (migrateVersion) {
              /*      case 2:
                        db.execSQL("ALTER TABLE INHABITANT ADD COLUMN 'GENDER' INTEGER NOT NULL DEFAULT '0';");
                        break;*/
                case 2:
                    db.execSQL("ALTER TABLE REPORT_INFO ADD COLUMN 'PATROL_IN_TIME' TEXT;");
                    db.execSQL("ALTER TABLE REPORT_INFO ADD COLUMN 'PATROL_OUT_TIME' TEXT;");
                    db.execSQL("ALTER TABLE REPORT_INFO ADD COLUMN 'STAR' INTEGER NOT NULL DEFAULT '0';");
                    break;
            }
        }
    }
    //                sql语句示例
//                db.execSQL("ALTER TABLE INHABITANT ADD COLUMN 'GENDER' INTEGER NOT NULL DEFAULT '0';");
//                db.execSQL("ALTER TABLE INHABITANT ADD COLUMN 'SPECIES' TEXT;");
//                db.execSQL("ALTER TABLE INVERTEBRATE ADD COLUMN 'SPECIES' TEXT;");
//                db.execSQL("ALTER TABLE PLANT ADD COLUMN 'SPECIES' TEXT;");
//                db.execSQL("ALTER TABLE CORAL ADD COLUMN 'SPECIES' TEXT;");
}
