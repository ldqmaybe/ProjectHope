package com.one.hope.constant;

/**
 * @author LinDingQiang
 * @time 2019/12/5 15:08
 * @email dingqiang.l@verifone.cn
 */
public enum BtState {
    /**
     * 蓝牙未打开..未知
     */
    UNKNOWN(-1, "未知异常..."),
    /**
     * 蓝牙未打开..
     */
    NOT_OPEN(0, "蓝牙未打开..."),
    /**
     * 蓝牙已经打开..
     */
    ALREADY_OPENED(1, "蓝牙已打开..."),
    /**
     * 蓝牙未配对..
     */
    NOT_PAIR(2, "请先用蓝牙与PC配对..."),
    /**
     * 请保证只同时连接一台蓝牙设备..
     */
    NOT_UNIQUE(3, "请保证只同时连接一台蓝牙设备..."),
    /**
     * 请保证只同时连接一台蓝牙设备..
     */
    CONNECTED(4, "蓝牙已连接..."),
    ;

    int state;
    String msg;

    BtState(int state, String msg) {
        this.state = state;
        this.msg = msg;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
