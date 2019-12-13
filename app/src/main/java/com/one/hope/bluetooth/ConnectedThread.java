package com.one.hope.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.one.hope.bluetooth.listener.OnReceiveListener;
import com.one.hope.bluetooth.listener.OnSendListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * 管理连接
 * 1、发送数据
 * 2、接收数据
 *
 * @author LinDingQiang
 * @time 2019/12/10 15:54
 * @email dingqiang.l@verifone.cn
 */
public class ConnectedThread extends Thread {
    private static final String TAG = "ConnectedThread";
    private BluetoothSocket mmSocket;
    private InputStream mmInStream;
    private OutputStream mmOutStream;
    //是否是主动断开
    private boolean isStop = false;
    //发起蓝牙连接的线程
    private ConnectThread connectThread;
    private OnReceiveListener onReceiveListener;
    private Handler handler = new Handler(Looper.getMainLooper());

    public void terminalClose(ConnectThread connectThread) {
        isStop = true;
        this.connectThread = connectThread;
    }

    public ConnectedThread(BluetoothSocket socket) {
        mmSocket = socket;

        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        //使用临时对象获取输入和输出流，因为成员流是静态类型
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();

        } catch (IOException e) {
            Log.i(TAG, "ConnectedThread-->获取InputStream 和 OutputStream异常!");
        }
        mmInStream = tmpIn;
        mmOutStream = tmpOut;

        if (mmInStream != null) {
            Log.i(TAG, "ConnectedThread-->已获取InputStream");
        }

        if (mmOutStream != null) {
            Log.i(TAG, "ConnectedThread-->已获取OutputStream");
        }
    }

    @Override
    public void run() {
        //最大缓存区 存放流
        byte[] buffer = new byte[1024 * 2];
        //持续监听输入流直到发生异常
        while (!isStop) {
            try {
                if (mmInStream == null) {
                    Log.i(TAG, "ConnectedThread:run-->输入流mmInStream == null");
                    break;
                }
                //先判断是否有数据，有数据再读取
                if (mmInStream.available() != 0) {
                    //2、接收数据
                    /*
                    为了demo正常运行，假设接收的数据长度为10；
                    实际上我们可以自定义数据格式，如：STX + LENGTH(2hex bytes)+ CONTENT + ETX + LRC
                     */
                    int msgLen = 10;
                    int readTotal = 0;
                    int readCount;
                    while (readTotal < msgLen) {
                        readCount = mmInStream.read(buffer, readTotal, msgLen - readTotal);
                        readTotal += readCount;
                    }
                    final byte[] respData = new byte[readTotal];
                    System.arraycopy(buffer, 0, respData, 0, readTotal);
                    if (onReceiveListener != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onReceiveListener.onReceiveDataSuccess(respData);  //成功收到消息
                            }
                        });
                    }
                }
            } catch (final IOException e) {
                Log.i(TAG, "ConnectedThread:run-->接收消息异常！" + e.getMessage());
                if (onReceiveListener != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onReceiveListener.onReceiveDataError("接收消息异常:" + e.getMessage());  //接收消息异常
                        }
                    });
                }
                //关闭流和socket
                boolean isClose = cancel();
                if (isClose) {
                    Log.i(TAG, "ConnectedThread:run-->接收消息异常,成功断开连接！");
                }
                break;
            }
        }
        //关闭流和socket
        boolean isClose = cancel();
        if (isClose) {
            Log.i(TAG, "ConnectedThread:run-->接收消息结束,断开连接！");
        }
    }

    public void receive(OnReceiveListener onReceiveListener) {
        this.onReceiveListener = onReceiveListener;
    }

    //发送数据
    public void write(final byte[] bytes, final OnSendListener onSendListener) {
        try {
            if (mmOutStream == null) {
                Log.i(TAG, "mmOutStream == null");
                return;
            }
            mmOutStream.write(bytes);
            Log.i(TAG, "写入成功：" + new String(bytes));
            if (onSendListener != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onSendListener.onSendSuccess(bytes);  //发送数据成功回调
                    }
                });
            }

        } catch (IOException e) {
            Log.i(TAG, "写入失败：" + new String(bytes));
            if (onSendListener != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onSendListener.onSendError(bytes, "写入失败");  //发送数据失败回调
                    }
                });
            }
        }
    }

    /**
     * 释放
     *
     * @return true 断开成功  false 断开失败
     */
    public boolean cancel() {
        try {
            if (mmInStream != null) {
                mmInStream.close();  //关闭输入流
            }
            if (mmOutStream != null) {
                mmOutStream.close();  //关闭输出流
            }
            if (mmSocket != null) {
                mmSocket.close();   //关闭socket
            }
            if (connectThread != null) {
                connectThread.cancel();
            }
            connectThread = null;
            mmInStream = null;
            mmOutStream = null;
            mmSocket = null;
            Log.i(TAG, "ConnectedThread:cancel-->成功断开连接");
            return true;
        } catch (IOException e) {
            // 任何一部分报错，都将强制关闭socket连接
            mmInStream = null;
            mmOutStream = null;
            mmSocket = null;
            Log.i(TAG, "ConnectedThread:cancel-->断开连接异常！" + e.getMessage());
            return false;
        }
    }
}
