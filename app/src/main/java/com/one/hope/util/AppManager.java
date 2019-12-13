package com.one.hope.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * Activity管理类（单例模式） 用于管理Activity的添加和退出的操作
 *
 * @author Administrator
 */
public class AppManager {

    // Activity任务栈集合
    private static Stack<Activity> sActivityStack;
    // 静态该类对象
    private static AppManager sInstance;
    private static final long WAITTIME = 2000;
    private long touchTime = 0;
    /**
     * 将其构造私有化，确保对象唯一
     */
    private AppManager() {
    }

    /**
     * 获取该类唯一的对象（采用单一模式）
     *
     * @return 返回ActivityManager对象
     */
    public static AppManager getInstance() {
        if (sInstance == null) {
            // 此处为了确保对象是唯一，同是也解决使用同步带来的性能降低的问题
            // 因此采用双重检验
            synchronized (AppManager.class) {
                if (sInstance == null) {
                    sInstance = new AppManager();

                }

            }

        }
        return sInstance;

    }

    /**
     * 将activity添加到任务栈中
     *
     * @param activity
     */
    public void addActivityTOStack(Activity activity) {
        if (sActivityStack == null) {
            sActivityStack = new Stack<Activity>();
        }
        sActivityStack.add(activity);

    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            sActivityStack.remove(activity);
            activity.finish();
            activity = null;

        }
    }

    public void finishActivity(Class clas) {
        int size = sActivityStack.size();
        for (int i = 0; i < size; i++) {
            if (null != sActivityStack.get(i) && sActivityStack.get(i).getClass() == clas) {
                sActivityStack.get(i).finish();
                return;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {

        int size = sActivityStack.size();
        for (int i = 0; i < size; i++) {
            if (null != sActivityStack.get(i)) {
                sActivityStack.get(i).finish();

            }
        }
        sActivityStack.clear();
        System.gc();
    }

    /**
     * 关闭除该activity的其他activity
     *
     * @param activity
     */
    public void finishOthersActivity(Activity activity) {
        int size = sActivityStack.size();
        for (int i = 0; i < size; i++) {
            if (null != sActivityStack.get(i) && sActivityStack.get(i) != activity) {
                sActivityStack.get(i).finish();
            }
        }
    }

    /**
     * 退出程序
     *
     * @param context 上下文环境
     */
    public void exitApp(Context context) {

        try {
            finishAllActivity();
            ActivityManager activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);

            activityManager.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onKeyDown() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - touchTime) >= WAITTIME) {
            ToastUtils.showShortToast("再按一次退出");
            touchTime = currentTime;
        } else {
            AppManager.getInstance().finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
