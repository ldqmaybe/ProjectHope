package com.one.hope.bluetooth.listener;

/**
 * @author LinDingQiang
 * @time 2019/12/10 15:54
 * @email dingqiang.l@verifone.cn
 */
public interface OnReceiveListener {
    /**
     * 接收到数据
     *
     * @param buffer 接收的的数据，字节数组
     */
    void onReceiveDataSuccess(byte[] buffer);

    /**
     * 接收数据出错
     *
     * @param errorMsg 接收数据出错描述
     */
    void onReceiveDataError(String errorMsg);
}
