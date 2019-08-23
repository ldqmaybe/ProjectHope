package com.one.greendaohope.util;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.one.greendaohope.entity.greendao.DaoMaster;
import com.one.greendaohope.entity.greendao.UserEntityDao;

import org.greenrobot.greendao.database.Database;

/**
 * @author LinDingQiang
 * @time 2019/8/8 17:40
 * @email dingqiang.l@verifone.cn
 */
public class MyDevOpenHelper extends DaoMaster.DevOpenHelper {

    public MyDevOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, UserEntityDao.class);
    }
}
