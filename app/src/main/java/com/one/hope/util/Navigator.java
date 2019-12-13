package com.one.hope.util;

import android.content.Context;
import android.content.Intent;

import com.one.hope.entity.UserEntity;
import com.one.hope.ui.BluetoothActivity;
import com.one.hope.ui.UserDetailActivity;
import com.one.hope.ui.UserListActivity;

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

    public void navigate2UserList(Context context) {
        LogUtils.i(TAG, "navigate2UserList: executed.");
        if (context != null) {
            Intent intentToLogin = UserListActivity.getCallingIntent(context);
            context.startActivity(intentToLogin);
        }
    }
    public void navigate2Bluetooth(Context context) {
        LogUtils.i(TAG, "navigate2Bluetooth: executed.");
        if (context != null) {
            Intent intent2Bluetooth = BluetoothActivity.getCallingIntent(context);
            context.startActivity(intent2Bluetooth);
        }
    }
    public void navigate2UserDetail(Context context, UserEntity userEntity) {
        LogUtils.i(TAG, "navigate2UserDetail: executed.");
        if (context != null) {
            Intent intentToUserDetail = UserDetailActivity.getCallingIntent(context);
            intentToUserDetail.putExtra("UserEntity",userEntity);
            context.startActivity(intentToUserDetail);
        }
    }
}
