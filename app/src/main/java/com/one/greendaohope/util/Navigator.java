package com.one.greendaohope.util;

import android.content.Context;
import android.content.Intent;

import com.one.greendaohope.ui.AddActivity;
import com.one.greendaohope.ui.UserDetailActivity;
import com.one.greendaohope.ui.UserListActivity;

/**
 * @author LinDingQiang
 * @time 2019/8/19 15:01
 * @email dingqiang.l@verifone.cn
 */
public class Navigator {
    private static final String TAG = Navigator.class.getSimpleName();

    private Navigator() {
    }


    private static class Holder {
        static final Navigator NAVIGATOR = new Navigator();
    }

    public static Navigator getInstance() {
        return Holder.NAVIGATOR;
    }

    public void navigate2Add(Context context) {
        LogUtils.i(TAG, "navigate2Add: executed.");
        if (context != null) {
            Intent intentToAdd = AddActivity.getCallingIntent(context);
            context.startActivity(intentToAdd);
        }
    }
    public void navigate2UserList(Context context) {
        LogUtils.i(TAG, "navigate2UserList: executed.");
        if (context != null) {
            Intent intentToLogin = UserListActivity.getCallingIntent(context);
            context.startActivity(intentToLogin);
        }
    }
    public void navigate2UserDetail(Context context) {
        LogUtils.i(TAG, "navigate2UserDetail: executed.");
        if (context != null) {
            Intent intentToUserDetail = UserDetailActivity.getCallingIntent(context);
            context.startActivity(intentToUserDetail);
        }
    }
}
