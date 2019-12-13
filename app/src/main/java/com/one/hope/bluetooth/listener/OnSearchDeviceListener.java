package com.one.hope.bluetooth.listener;

import android.bluetooth.BluetoothDevice;

import java.util.List;

/**
 * @author LinDingQiang
 * @time 2019/12/10 15:54
 * @email dingqiang.l@verifone.cn
 */
public interface OnSearchDeviceListener {

    /**
     * 开始搜索
     */
    void onStartDiscovery();

    /**
     * 发现新设备
     *
     * @param device 新设备
     */
    void onNewDeviceFound(BluetoothDevice device);

    /**
     * 搜索结束
     *
     * @param bondedList 已绑定的设备列表
     * @param newList    搜索到的所有新设备
     */
    void onSearchCompleted(List<BluetoothDevice> bondedList, List<BluetoothDevice> newList);

}
