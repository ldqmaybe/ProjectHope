package com.one.hope.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;


/**
 * 发起蓝牙连接
 *
 * @author LinDingQiang
 * @time 2019/12/10 15:54
 * @email dingqiang.l@verifone.cn
 */

public class ConnectThread extends Thread {
    private static final String TAG = "ConnectThread";
    private final BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket mmSocket;
    private final String BLUETOOTH_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    public ConnectThread(BluetoothAdapter bluetoothAdapter, BluetoothDevice bluetoothDevice) {
        this.mBluetoothAdapter = bluetoothAdapter;
        BluetoothSocket tmpBluetoothSocket = null;
        if (mmSocket != null) {
            Log.i(TAG, "ConnectThread-->mmSocket != null先去释放");
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG, "ConnectThread-->mmSocket != null已释放");
        //1、获取BluetoothSocket
        try {
            //建立安全的蓝牙连接，会弹出配对框
            tmpBluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(BLUETOOTH_UUID));
        } catch (IOException e) {
            Log.i(TAG, "ConnectThread-->获取BluetoothSocket异常!" + e.getMessage());
        }
        mmSocket = tmpBluetoothSocket;
        if (mmSocket != null) {
            Log.i(TAG, "ConnectThread-->已获取BluetoothSocket");
        }

    }

    @Override
    public void run() {
        //连接之前先取消发现设备，否则会大幅降低连接尝试的速度，并增加连接失败的可能性
        if (mBluetoothAdapter == null) {
            Log.i(TAG, "ConnectThread:run-->mBluetoothAdapter == null");
            return;
        }
        //取消发现设备
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        if (mmSocket == null) {
            Log.i(TAG, "ConnectThread:run-->mmSocket == null");
            return;
        }

        //2、通过socket去连接设备
        try {
            Log.i(TAG, "ConnectThread:run-->去连接...");
            if (onBluetoothConnectListener != null) {
                onBluetoothConnectListener.onStartConn();  //开始去连接回调
            }
            mmSocket.connect();  //connect()为阻塞调用，连接失败或 connect() 方法超时（大约 12 秒之后），它将会引发异常

            if (onBluetoothConnectListener != null) {
                onBluetoothConnectListener.onConnSuccess(mmSocket);  //连接成功回调
                Log.i(TAG, "ConnectThread:run-->连接成功");
            }

        } catch (IOException e) {
            Log.i(TAG, "ConnectThread:run-->连接异常！" + e.getMessage());

            if (onBluetoothConnectListener != null) {
                onBluetoothConnectListener.onConnFailure("连接异常：" + e.getMessage());
            }
            //释放
            cancel();
        }

    }

    /**
     * 释放
     */
    public void cancel() {
        try {
            if (mmSocket != null && mmSocket.isConnected()) {
                Log.i(TAG, "ConnectThread:cancel-->mmSocket.isConnected() = " + mmSocket.isConnected());
                mmSocket.close();
                mmSocket = null;
                return;
            }

            if (mmSocket != null) {
                mmSocket.close();
                mmSocket = null;
            }

            Log.i(TAG, "ConnectThread:cancel-->关闭已连接的套接字释放资源");

        } catch (IOException e) {
            Log.i(TAG, "ConnectThread:cancel-->关闭已连接的套接字释放资源异常!" + e.getMessage());
        }
    }

    private OnBluetoothConnectListener onBluetoothConnectListener;

    public void setOnBluetoothConnectListener(OnBluetoothConnectListener onBluetoothConnectListener) {
        this.onBluetoothConnectListener = onBluetoothConnectListener;
    }
    //连接状态监听者
    public interface OnBluetoothConnectListener {
        void onStartConn();  //开始连接
        void onConnSuccess(BluetoothSocket bluetoothSocket);  //连接成功
        void onConnFailure(String errorMsg);  //连接失败
    }
}
