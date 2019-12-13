package com.one.hope.util;

import android.support.annotation.NonNull;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author LinDingQiang
 * @time 2019/4/23 10:32
 * @email dingqiang.l@verifone.cn
 */
public class ThreadPoolManager {
    //核心线程数
    private int corePoolSize;
    //最大线程池数量
    private int maxinumPoolSize;
    //存活时间
    private long keepAliveTime = 1;
    private TimeUnit unit = TimeUnit.HOURS;
    private ThreadPoolExecutor executor;

    private static class InstanceHolder {
        private static final ThreadPoolManager INSTANCE = new ThreadPoolManager();
    }

    public static ThreadPoolManager getInstance() {
        return InstanceHolder.INSTANCE;

    }

    private ThreadPoolManager() {
        init();
    }

    private void init() {
        corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
        maxinumPoolSize = corePoolSize;
        executor = new ThreadPoolExecutor(
                corePoolSize,
                maxinumPoolSize,
                keepAliveTime,
                unit,
                new LinkedBlockingDeque<>(),
                new DefaultThreadFactory(Thread.NORM_PRIORITY, "ccb-pool-"),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    public void execute(Runnable runnable) {
        if (executor == null) {
            init();
        }
        if (runnable != null) {
            executor.execute(runnable);
        }
    }

    public void remove(Runnable runnable) {
        if (runnable != null) {
            executor.remove(runnable);
        }
    }

    private static class DefaultThreadFactory implements ThreadFactory {
        /**
         * 线程池的计数
         */
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        /**
         * 线程的计数
         */
        private static final AtomicInteger THREAD_NUMBER = new AtomicInteger(1);

        private final ThreadGroup group;
        private final String namePrefix;
        private final int threadPriority;

        DefaultThreadFactory(int threadPriority, String threadNamePrefix) {
            this.threadPriority = threadPriority;
            this.group = Thread.currentThread().getThreadGroup();
            namePrefix = threadNamePrefix + POOL_NUMBER.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread thread = new Thread(group, r, namePrefix + THREAD_NUMBER.getAndIncrement(), 0);
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            thread.setPriority(threadPriority);
            return thread;
        }
    }

}
