package com.one.hope.bluetooth.listener;

/**
 * @author LinDingQiang
 * @time 2019/12/10 15:54
 * @email dingqiang.l@verifone.cn
 */
public interface OnSendListener {
    /**
     * 发送数据成功
     *
     * @param data 要发送的数据
     */
    void onSendSuccess(byte[] data);

    /**
     * 发送数据出错
     *
     * @param data     要发送的数据
     * @param errorMsg 错误描述
     */
    void onSendError(byte[] data, String errorMsg);
}
