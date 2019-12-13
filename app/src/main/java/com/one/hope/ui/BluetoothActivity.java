package com.one.hope.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.one.hope.R;
import com.one.hope.bluetooth.BtHelper;
import com.one.hope.bluetooth.listener.OnConnectListener;
import com.one.hope.bluetooth.listener.OnReceiveListener;
import com.one.hope.bluetooth.listener.OnSearchDeviceListener;
import com.one.hope.bluetooth.listener.OnSendListener;
import com.one.hope.util.ToastUtils;
import com.one.hope.widget.ArticleItemDecortion;

import java.util.ArrayList;
import java.util.List;

public class BluetoothActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = BluetoothActivity.class.getSimpleName();
    private TextView tvStatu, tvName, tvMac, tvSendResult, tvReceive;
    private RecyclerView rv;
    private LinearLayout llDataSendReceive;
    private EditText etSendMsg;
    private BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_ENABLE_BLUETOOTH = 1000;
    private BaseQuickAdapter<BluetoothDevice, BaseViewHolder> adapter;
    private List<BluetoothDevice> deviceList;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, BluetoothActivity.class);
    }

    private void setTopbar() {
        setTopBarTitle("列表");
        showTopBarLeftIcon();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_bluetooth);
        setTopbar();
        tvStatu = findView(R.id.tv_states);
        tvName = findView(R.id.tv_name);
        tvMac = findView(R.id.tv_address);
        findViewById(R.id.bt_disconnect).setOnClickListener(this);
        findViewById(R.id.bt_to_send).setOnClickListener(this);
        llDataSendReceive = findViewById(R.id.ll_data_send_receive);
        etSendMsg = findViewById(R.id.et_send_msg);
        tvSendResult = findViewById(R.id.tv_send_result);
        tvReceive = findViewById(R.id.tv_receive_result);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new ArticleItemDecortion(this));
        initAdapter();
        initBluetooth();
        showDataSendReceiveView(false);
    }

    private void showDataSendReceiveView(boolean isConnected) {
        llDataSendReceive.setVisibility(isConnected ? View.VISIBLE : View.GONE);
        rv.setVisibility(isConnected ? View.GONE : View.VISIBLE);
    }

    private void initAdapter() {
        deviceList = new ArrayList<>();
        adapter = new BaseQuickAdapter<BluetoothDevice, BaseViewHolder>(R.layout.devices_item, deviceList) {
            @Override
            protected void convert(BaseViewHolder helper, BluetoothDevice device) {
                helper.setText(R.id.tv_device_name, device.getName())
                        .setText(R.id.tv_device_address, device.getAddress());
            }
        };
        adapter.setOnItemClickListener((adapter, view, position) -> {
            connect(deviceList.get(position));
        });
        rv.setAdapter(adapter);
    }

    private void setBluetoothStatesView(String msg, BluetoothDevice device) {
        if (device == null) {
            tvName.setText("");
            tvMac.setText("");
        } else {
            tvName.setText(device.getName());
            tvMac.setText(device.getAddress());
        }
        tvStatu.setText(msg);

    }

    private void connect(BluetoothDevice device) {
        if (BtHelper.getInstance(this).isConnected()) {
            ToastUtils.showShortToast("已连接" + device.getName() + "," + device.getAddress());
            setBluetoothStatesView("蓝牙已连接", device);
        } else {
            BtHelper.getInstance(this).startConnect(device, new OnConnectListener() {
                @Override
                public void onStartConn() {
                    Log.i(TAG, "onStartConn: 开始连接蓝牙：" + device.getName() + "," + device.getAddress());
                    setBluetoothStatesView("蓝牙连接中...", null);
                }

                @Override
                public void onConnSuccess() {
                    Log.i(TAG, "onConnSuccess: 开始连接蓝牙：" + device.getName() + "," + device.getAddress());
                    ToastUtils.showShortToast("连接成功");
                    setBluetoothStatesView("蓝牙已连接", device);
                    showDataSendReceiveView(true);
                    receiveData();
                }

                @Override
                public void onConnFailure(String errorMsg) {
                    Log.i(TAG, "onConnSuccess: 开始连接蓝牙：" + device.getName() + "," + device.getAddress());
                    ToastUtils.showShortToast("连接失败：" + errorMsg);
                    setBluetoothStatesView("连接失败", null);
                }
            });
        }
    }

    private void receiveData() {
        BtHelper.getInstance(this).receiveData(new OnReceiveListener() {
            @Override
            public void onReceiveDataSuccess(byte[] buffer) {
                Log.i(TAG, "onReceiveDataSuccess: " + new String(buffer));
                tvReceive.setText(new String(buffer));
            }

            @Override
            public void onReceiveDataError(String errorMsg) {
                Log.i(TAG, "onReceiveDataError: " + errorMsg);
                tvReceive.setText(errorMsg);
            }
        });
    }

    /**
     * 初始化蓝牙
     */
    private void initBluetooth() {
        bluetoothAdapter = BtHelper.getInstance(this).getBtAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "当前设备不支持蓝牙", Toast.LENGTH_SHORT).show();
        } else {
            //手机设备支持蓝牙，判断蓝牙是否已开启
            if (bluetoothAdapter.isEnabled()) {
                Toast.makeText(this, "蓝牙已开启", Toast.LENGTH_SHORT).show();
                tvStatu.setText("蓝牙已开启");
                searchBtDevice();
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (R.id.bt_disconnect == v.getId()) {
            BtHelper.getInstance(this).clearConnectedThread();
            setBluetoothStatesView("蓝牙已断开连接", null);
            showDataSendReceiveView(false);
        } else if (R.id.bt_to_send == v.getId()) {
            sendData();
        }
    }

    private void sendData() {
        String sendMsg = etSendMsg.getText().toString();
        if (TextUtils.isEmpty(sendMsg)) {
            ToastUtils.showShortToast("发送数据为空");
            return;
        }
        if (!BtHelper.getInstance(this).isConnected()) {
            ToastUtils.showShortToast("请先连接蓝牙");
            showDataSendReceiveView(false);
            return;
        }
        BtHelper.getInstance(this).sendData(sendMsg, new OnSendListener() {
            @Override
            public void onSendSuccess(byte[] data) {
                ToastUtils.showShortToast("发送成功");
                tvSendResult.setText(new String(data));
            }

            @Override
            public void onSendError(byte[] data, String errorMsg) {
                ToastUtils.showShortToast("发送失败：" + errorMsg);
            }
        });
    }

    private void searchBtDevice() {
        BtHelper.getInstance(this).search(new OnSearchDeviceListener() {
            @Override
            public void onStartDiscovery() {
                Log.i(TAG, "onStartDiscovery: ");
            }

            @Override
            public void onNewDeviceFound(BluetoothDevice device) {
                Log.i(TAG, "onNewDeviceFound: 蓝牙名称：" + device.getName() + ",MAC地址：" + device.getAddress());
                if (!deviceList.contains(device)) {
                    adapter.addData(device);
                }
            }

            @Override
            public void onSearchCompleted(List<BluetoothDevice> bondedList, List<BluetoothDevice> newList) {
                Log.i(TAG, "onSearchCompleted: ");
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(BluetoothActivity.this, "打开蓝牙成功", Toast.LENGTH_SHORT).show();
                tvStatu.setText("蓝牙已开启");
                searchBtDevice();
            } else {
                Toast.makeText(BluetoothActivity.this, "打开蓝牙失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BtHelper.getInstance(this).close();
    }
}
