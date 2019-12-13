package com.one.hope.adapter;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author LinDingQiang
 * @time 2019/12/11 16:53
 * @email dingqiang.l@verifone.cn
 */
public class BtAdapter extends BaseQuickAdapter<BluetoothDevice, BaseViewHolder> {
    private List<BluetoothDevice> deviceList;

    public BtAdapter(int layoutResId, @Nullable List<BluetoothDevice> deviceList) {
        super(layoutResId, deviceList);
        this.deviceList = deviceList;
    }

    @Override
    protected void convert(BaseViewHolder helper, BluetoothDevice item) {
    }

}
