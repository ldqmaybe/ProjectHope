package com.one.hope.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.one.hope.bluetooth.listener.OnConnectListener;
import com.one.hope.bluetooth.listener.OnReceiveListener;
import com.one.hope.bluetooth.listener.OnSearchDeviceListener;
import com.one.hope.bluetooth.listener.OnSendListener;

import java.util.ArrayList;
import java.util.List;


/**
 * @author LinDingQiang
 * @time 2019/12/10 14:30
 * @email dingqiang.l@verifone.cn
 */
public class BtHelper {
    private static final String TAG = "BtHelper";
    private static Context mContext;
    private static BtHelper btHelper;
    //已配对的蓝牙
    private static List<BluetoothDevice> mBondedList = new ArrayList<>();
    //新增蓝牙
    private static List<BluetoothDevice> mNewList = new ArrayList<>();
    //设备搜索的监听
    private static OnSearchDeviceListener mOnSearchDeviceListener;
    //搜索蓝牙广播
    private volatile SearchReceiver mReceiver = new SearchReceiver();
    //是否需要将广播取消注册
    private boolean mNeed2unRegister;
    //发起连接的线程
    private ConnectThread connectThread;
    //管理连接的线程
    private ConnectedThread connectedThread;
    //当前设备连接状态
    private boolean curConnState = false;
    private Handler handler = new Handler(Looper.getMainLooper());
    //连接失败，重连3次
    private int connectTimes = 3;
    //已经成功连接上的蓝牙
    private BluetoothSocket connectBlueToothSocket;

    public static BtHelper getInstance(Context context) {
        if (btHelper == null) {
            synchronized (BtHelper.class) {
                if (btHelper == null) {
                    btHelper = new BtHelper(context);
                }
            }
        }
        return btHelper;
    }

    private BtHelper(Context context) {
        mContext = context.getApplicationContext();
    }

    public boolean isConnected() {
        return curConnState;
    }

    private BluetoothSocket getConnBluetoothSocket() {
        return connectBlueToothSocket;
    }

    public BluetoothAdapter getBtAdapter() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.i(TAG, "getBtAdapter: 该设备不支持蓝牙");
        }
        return bluetoothAdapter;
    }

    /**
     * 接收数据
     *
     * @param onReceiveListener 接收数据监听
     */
    public void receiveData(OnReceiveListener onReceiveListener) {
        if (connectedThread == null) {
            connectedThread = new ConnectedThread(getConnBluetoothSocket());
            connectedThread.start();
        }
        connectedThread.receive(onReceiveListener);
    }

    /**
     * 发送数据
     *
     * @param sendData       发送的数据
     * @param onSendListener 发送数据监听
     */
    public void sendData(String sendData, OnSendListener onSendListener) {
        if (connectedThread == null) {
            connectedThread = new ConnectedThread(getConnBluetoothSocket());
            connectedThread.start();
        }
        connectedThread.write(sendData.getBytes(), onSendListener);
    }

    /**
     * 连接蓝牙
     *
     * @param bluetoothDevice   要连接的蓝牙
     * @param onConnectListener 连接蓝牙监听
     */
    public void startConnect(final BluetoothDevice bluetoothDevice, final OnConnectListener onConnectListener) {
        if (bluetoothDevice == null) {
            Log.i(TAG, "startConnectDevice-->bluetoothDevice == null");
            return;
        }
        if (getBtAdapter() == null) {
            Log.i(TAG, "startConnectDevice-->bluetooth3Adapter == null");
            return;
        }
        //如果正在扫描，则先取消扫描
        if (getBtAdapter().isDiscovering()) {
            getBtAdapter().cancelDiscovery();
        }
        connectThread = new ConnectThread(getBtAdapter(), bluetoothDevice);
        connectThread.setOnBluetoothConnectListener(new ConnectThread.OnBluetoothConnectListener() {
            @Override
            public void onStartConn() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onConnectListener.onStartConn();
                    }
                });
            }

            @Override
            public void onConnSuccess(BluetoothSocket bluetoothSocket) {
                connectBlueToothSocket = bluetoothSocket;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onConnectListener.onConnSuccess();
                    }
                });
                //标记当前连接状态为true
                curConnState = true;
            }

            @Override
            public void onConnFailure(final String errorMsg) {
                if (connectTimes >= 0) {
                    //标记当前连接状态为false
                    curConnState = false;
                    //断开管理连接
                    clearConnectedThread();
                    startConnect(bluetoothDevice, onConnectListener);
                    connectTimes--;
                    return;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onConnectListener.onConnFailure(errorMsg);
                    }
                });
                //标记当前连接状态为false
                curConnState = false;
                //断开管理连接
                clearConnectedThread();
            }
        });
        connectThread.start();
    }

    /**
     * 搜索设备
     *
     * @param listener 搜索设备监听
     */
    public void search(OnSearchDeviceListener listener) {
        if (mBondedList == null) {
            mBondedList = new ArrayList<>();
        }
        if (mNewList == null) {
            mNewList = new ArrayList<>();
        }

        mBondedList.clear();
        mNewList.clear();

        mOnSearchDeviceListener = listener;

        //开始搜索
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); //开始扫描
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//扫描结束
        filter.addAction(BluetoothDevice.ACTION_FOUND);//搜索到设备
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED); //配对状态监听
        if (mReceiver == null) {
            mReceiver = new BtHelper.SearchReceiver();
        }
        mContext.registerReceiver(mReceiver, filter);
        mNeed2unRegister = true;
        //取消当前正在搜索设备...
        if (getBtAdapter().isDiscovering()) {
            getBtAdapter().cancelDiscovery();
        }
        getBtAdapter().startDiscovery();
    }


    /**
     * 搜索蓝牙广播接收器
     */
    private class SearchReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                mOnSearchDeviceListener.onStartDiscovery();
                //开始搜索
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //发现设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (mOnSearchDeviceListener != null) {
                    mOnSearchDeviceListener.onNewDeviceFound(device);
                }
                if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                    mNewList.add(device);
                } else if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    mBondedList.add(device);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (getBtAdapter() != null) {
                    getBtAdapter().cancelDiscovery();
                }
                //停止搜索
                if (mOnSearchDeviceListener != null) {
                    mOnSearchDeviceListener.onSearchCompleted(mBondedList, mNewList);
                }
            }
        }
    }

    /**
     * 断开正在连接的线程，
     */
    public void clearConnectedThread() {
        Log.d(TAG, "clearConnectedThread-->即将断开");
        if (connectedThread != null) {
            connectedThread.terminalClose(connectThread);
            connectedThread.cancel();  //释放连接
            connectedThread = null;
        }
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
        curConnState = false;

    }

    public void close() {
        if (getBtAdapter() != null) {
            getBtAdapter().cancelDiscovery();
        }
        if (mNeed2unRegister && mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
            mNeed2unRegister = !mNeed2unRegister;
        }
        clearConnectedThread();
        mNewList = null;
        mBondedList = null;
        mReceiver = null;
        btHelper = null;
    }

}
