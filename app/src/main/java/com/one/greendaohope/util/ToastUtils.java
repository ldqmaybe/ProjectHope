package com.one.greendaohope.util;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.one.greendaohope.HopeApplication;

/**
 * author: LinDingQiang <br/>
 * created on: 2017/6/29 10:19 <br/>
 * description:Toast工具类
 */
public class ToastUtils {

    /**
     * 获取当前application对象
     *
     * @return application对象
     */
    public static Context getContext() {
        return HopeApplication.getInstance();
    }

    /**
     * 获取资源
     *
     * @return Resources
     */
    public static Resources getResource() {
        return getContext().getResources();
    }

    /**
     * 显示时间为short的toast
     *
     * @param msg 需要显示的内容
     */
    public static void showShortToast(final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 显示时间为short的toast
     *
     * @param resid 需要显示的内容
     */
    public static void showShortToast(int resid) {
        showShortToast(getContext().getResources().getString(resid));
    }

    /**
     * 显示时间为Long的toast
     *
     * @param msg 需要显示的内容
     */
    public static void showLongToast(final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 显示时间为Long的toast
     *
     * @param resid 需要显示的内容
     */
    public static void showLongToast(int resid) {
        showLongToast(getContext().getResources().getString(resid));
    }
}
