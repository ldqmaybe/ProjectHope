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
    private static UserEntityDao userEntityDao;

    public static synchronized void init(Context context) {
        DaoSession daoSession = DaoManager.getInstance(context).getDaoSession();
        userEntityDao = daoSession.getUserEntityDao();
    }

    public static void saveUser(UserEntity entity) {
        userEntityDao.save(entity) ;
    }

    public static  List<UserEntity> loadAllUsers() {
        return  userEntityDao.loadAll();
    }

    public static UserEntity loadUserByPhoneNo(String phoneNo) {
        QueryBuilder<UserEntity> queryBuilder = userEntityDao.queryBuilder();
        queryBuilder.where(UserEntityDao.Properties.Phone.eq(phoneNo));
        return queryBuilder.unique();
    }
    public static UserEntity loadUserId(long userId) {
        QueryBuilder<UserEntity> queryBuilder = userEntityDao.queryBuilder();
        queryBuilder.where(UserEntityDao.Properties.Id.eq(userId));
        return queryBuilder.unique();
    }

    public static void deleteUser(UserEntity entity) {
        userEntityDao.delete(entity);
    }

    public static void updateUser(UserEntity entity) {
        userEntityDao.update(entity);
    }
}
