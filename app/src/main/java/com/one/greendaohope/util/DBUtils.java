package com.one.greendaohope.util;


import android.content.Context;

import com.one.greendaohope.entity.UserEntity;
import com.one.greendaohope.entity.greendao.DaoSession;
import com.one.greendaohope.entity.greendao.UserEntityDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @author LinDingQiang
 * @time 2019/8/8 16:28
 * @email dingqiang.l@verifone.cn
 */
public class DBUtils {
    private static DaoSession daoSession;

    public static synchronized void init(Context context) {
        daoSession = DaoManager.getInstance(context).getDaoSession();
    }

    public static boolean insert(Object object) {
        return daoSession.insert(object) != -1;
    }

    public static <T> List<T> loadAll(Class<T> entityClass) {
        return daoSession.loadAll(entityClass);
    }

    public static UserEntity loadUserByPhoneNo(String phoneNo) {
        QueryBuilder queryBuilder = daoSession.queryBuilder(UserEntity.class);
        queryBuilder.where(UserEntityDao.Properties.Phone.eq(phoneNo));
        return (UserEntity) queryBuilder.unique();
    }

    public static void delete(UserEntity entity) {
        daoSession.delete(entity);
    }

    public static void update(UserEntity entity) {
        daoSession.update(entity);
    }
}
