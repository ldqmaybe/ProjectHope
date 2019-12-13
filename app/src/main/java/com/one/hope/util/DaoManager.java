package com.one.hope.util;


import android.content.Context;

import com.one.hope.entity.greendao.DaoMaster;
import com.one.hope.entity.greendao.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * @author LinDingQiang
 * @time 2019/8/8 15:45
 * @email dingqiang.l@verifone.cn
 */
public class DaoManager {
    private static final String DB_NAME = "greendao_db.db";
    private static DaoManager daoManager;
    private DaoMaster daoMaster;
    private DaoMaster.DevOpenHelper devOpenHelper;
    private DaoSession daoSession;
    private Context context;

    private DaoManager(Context context) {
        this.context = context;
    }

    /**
     * 使用单例模式获得操作数据库的对象
     *
     * @return DaoManager
     */
    static DaoManager getInstance(Context context) {
        if (daoManager == null) {
            synchronized (DaoManager.class) {
                if (daoManager == null) {
                    daoManager = new DaoManager(context);
                }
            }
        }
        return daoManager;
    }

    private DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            devOpenHelper = new MyDevOpenHelper(context, DB_NAME, null);
            daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        }
        return daoMaster;
    }

    synchronized DaoSession getDaoSession() {
        if (daoSession == null) {
            daoSession = getDaoMaster().newSession();
        }
        return daoSession;
    }

    public void setDebug(boolean flag) {
        QueryBuilder.LOG_SQL = flag;
        QueryBuilder.LOG_VALUES = flag;
    }

    private void closeDaoSession() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
    }

    private void closeHelper() {
        if (devOpenHelper != null) {
            devOpenHelper.close();
            devOpenHelper = null;
        }
    }

    public synchronized void close() {
        closeDaoSession();
        closeHelper();
    }
}
