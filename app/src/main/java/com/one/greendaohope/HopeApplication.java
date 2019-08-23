package com.one.greendaohope;

import android.app.Application;

import com.one.greendaohope.util.DBUtils;

/**
 * @author LinDingQiang
 * @time 2019/8/19 14:43
 * @email dingqiang.l@verifone.cn
 */
public class HopeApplication extends Application {
    private static HopeApplication application;

    public static HopeApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        DBUtils.init(this);
    }
}
