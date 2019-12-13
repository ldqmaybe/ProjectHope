package com.one.hope.bluetooth.listener;

/**
 * @author LinDingQiang
 * @time 2019/12/10 15:54
 * @email dingqiang.l@verifone.cn
 */
public interface OnConnectListener {
    /**
     * 开始连接
     */
    void onStartConn();

    /**
     * 连接成功
     */
    void onConnSuccess();

    /**
     * 连接失败
     *
     * @param errorMsg 错误描述
     */
    void onConnFailure(String errorMsg);
}
