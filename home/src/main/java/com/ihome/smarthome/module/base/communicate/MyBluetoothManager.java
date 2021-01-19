package com.ihome.smarthome.module.base.communicate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.ihome.smarthome.R;
import com.ihome.smarthome.utils.ClsUtils;
import com.ihome.smarthome.utils.EventBusUtils;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class MyBluetoothManager implements ICommunicate {

    final public int REQUEST_ENABLE_BT = 100;
    final public int REQUEST_DISCOVERABLE = 101;
    final public int MESSAGE_READ = 1;
    final public int MESSAGE_FOUND_DEVICE = 2;
    final public int ON_CONNECTED = 3;
    final public int ON_DISCONNECTED = 4;
    final public int ON_CONNECT_FAILED = 5;

    String currentConnectBTName = "";
    boolean mScanning;
    private BluetoothLeScanner mBluetoothLeScanner;
    private ScanCallback mScanCallback;
    private Handler mHandler;
    boolean isRegisterDiscoveryReceiver = false;
    private AlertListView.MyAdapter mAdapter = new AlertListView.MyAdapter();

    private int times=0;
    HashMap<String  , Timer> retryTimer = new HashMap<>();


    HashMap<String, ConnectedThread> connectedBTThreads = new HashMap<>();
    AcceptThread acceptThread;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            switch (msg.what) {

                case ON_CONNECTED:
                    if (bundle == null) {
                        return;
                    }
                    String name = bundle.getString("name");
                    String type = bundle.getString("type");
                    lisenter.onConnect(name, type);
                    break;
                case ON_DISCONNECTED:
                    if (bundle == null) {
                        return;
                    }
                    lisenter.onDisConnect(bundle.getString("name"), "蓝牙断开连接");
                    break;
                case ON_CONNECT_FAILED:
                    if (bundle == null) {
                        return;
                    }
                    lisenter.onConnectFailed(bundle.getString("name"), "蓝牙未开启或不在通信范围，连接失败");
                    break;

                case MESSAGE_READ:

                    if (bundle == null) {
                        return;
                    }
                    String client_name = "";
                    String content = "";
                    if (!TextUtils.isEmpty(bundle.getString("name"))) {
                        client_name = String.valueOf(bundle.getString("name"));
                    }
                    String str_content = bundle.getString("str_content");
                    if (str_content != null) {
                        if (str_content != null && str_content.length() != 0) {
                            lisenter.onMessage(client_name, str_content);
                        }
                    }
                    break;
                case MESSAGE_FOUND_DEVICE:
                    BluetoothDevice device = (BluetoothDevice) msg.obj;
                    if (alertListView == null) {
                        alertListView = new AlertListView(context, devices, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ToastUtils.showShort(devices.get(which).getName());
                            }
                        });
                        alertListView.show();
                    }
                    if (device.getName() != null) {
                        devices.add(device);
                        alertListView.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };


    String str_uuid = "00001101-0000-1000-8000-00805F9B34FB";
    public String NAME = "ai_car_android";
    public UUID MY_UUID = UUID.fromString(str_uuid);
    public final ParcelUuid Service_UUID = ParcelUuid.fromString(str_uuid);


    private static BluetoothManager btManager;
    private static MyBluetoothManager bluetoothManager;

    private static BluetoothAdapter mBluetoothAdapter;
    private BroadcastReceiver mFoundDeviceReceiver = null;


    private AlertListView alertListView;
    private ArrayList<BluetoothDevice> devices = new ArrayList<>();
    public onMessageLisenter lisenter;
    private Context context;


    public static MyBluetoothManager Instance(Context context) {
        if (bluetoothManager == null) {
            bluetoothManager = new MyBluetoothManager(context);
            btManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = btManager.getAdapter();
            bluetoothManager.registerDiscoveryReceiver();

        }
        return bluetoothManager;
    }

    public MyBluetoothManager(Context context) {
        this.context = context;
    }


    public void connectCurrentBT() {
        enableBluetooth();//如果蓝牙没开启，开启蓝牙
        String bluetooth = currentConnectBTName;
        BluetoothDevice bluetoothDevice = findPairedBlueToothDeviceByName(bluetooth);
        if (bluetoothDevice == null) {//如果没有连接过蓝牙
            //  requestDiscoverable();
            registerDiscoveryReceiver();//注册发现蓝牙广播接收器
            startScan();//开始发现蓝牙
            return;
        }
        connect(bluetoothDevice);//如果有蓝牙就连接
    }

    public boolean isBluetoothOpen() {
        if (!isAvailable()) {
            return false;
        }
        return mBluetoothAdapter.isEnabled();
    }

    public void connect(String BluetoothName) {

        currentConnectBTName = BluetoothName;
        if (!isBluetoothOpen()) {
            enableBluetooth();
            return;
        }
        BluetoothDevice bluetoothDevice = findPairedBlueToothDeviceByName(BluetoothName);
        if (bluetoothDevice == null) {//如果没有连接过蓝牙
            ToastUtil.toast("没有名称为[" + BluetoothName + "]的配对设备");
            EventBusUtils.sendFailLog("没有名称为[" + BluetoothName + "]的配对设备");
            return;
        }
        connect(bluetoothDevice);//如果有蓝牙就连接
    }



    public void  cancelReTryTimer(String name){
        if(retryTimer.containsKey(name)){
            Timer timer = retryTimer.get(name);
            if(timer!=null){
                timer.cancel();
            }
            retryTimer.remove(name);
        }

    }



    public void addRetryTimer(String name){
            if(retryTimer.containsKey(name)){
                        return;
                    }
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            MyBluetoothManager.Instance(context).connect(name);
                            times++;
                            if(times==3){
                                timer.cancel();
                                EventBusUtils.sendFailLog("尝试重连失败");
                            }
                        }
                    };
                    timer.schedule(task, 0,10000);//2秒后执行TimeTask的run方法
                    retryTimer.put(name,timer);
    }



    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_DISCOVERABLE) {
            ToastUtils.showShort("蓝牙可见设置成功");
            startBluetoothServer();
            return;
        }
    }

    @Override
    public void disConnect(String name) {
       ConnectedThread connectedThread =   connectedBTThreads.get(name);
       connectedThread.close();
       sendEventMessage(ON_DISCONNECTED,name);
    }

    @Override
    public void destroy() {

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        unRegisterDiscoveryReceiver(context);
    }


    public boolean isConnected(ConnectedThread connectedThread) {
        if (connectedThread != null && connectedThread.isConnected()) {
            return true;
        }
        return false;
    }


    public void sendMessage(String deviceName, String msg) {
        ConnectedThread thread = connectedBTThreads.get(deviceName);
        if (thread == null) {
            EventBusUtils.sendFailLog(deviceName + "#不存在此线程");
            return;
        }
        if (!thread.isConnected()) {
            EventBusUtils.sendFailLog(deviceName + "#已断开连接");
            return;
        }
        thread.write(msg);
    }

    @Override
    public void setOnMessageLisenter(onMessageLisenter onMessageLisenter) {
        this.lisenter = onMessageLisenter;
    }


    private BluetoothDevice findPairedBlueToothDeviceByName(String name) {
        Set<BluetoothDevice> devices = getPairedDevices();
        for (BluetoothDevice device : devices) {
            if (device.getName().equals(name)) {
                return device;
            }
        }
        return null;
    }


    private static class AlertListView {
        private AlertDialog alertDialog1;

        private MyAdapter simpleAdapter;
        private AlertDialog.OnClickListener onClickListener;
        private List list;

        public AlertListView(Context context, List list, AlertDialog.OnClickListener onClickListener) {
            this.list = list;
            simpleAdapter = new MyAdapter(list, context);
            if (alertDialog1 == null) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setTitle("已发现的蓝牙");
                alertBuilder.setAdapter(simpleAdapter, onClickListener);
                alertDialog1 = alertBuilder.create();
                alertDialog1.getListView();
            }
        }


        public void show() {
            if (alertDialog1 == null) {
                return;
            }
            alertDialog1.show();
        }


        public void notifyDataSetChanged() {
            simpleAdapter.notifyDataSetChanged();
        }


        public static class MyAdapter extends BaseAdapter {
            private List<BluetoothDevice> Datas;
            private Context mContext;

            public MyAdapter() {
            }

            public MyAdapter(List<BluetoothDevice> datas, Context mContext) {
                Datas = datas;
                this.mContext = mContext;
            }

            @Override
            public int getCount() {
                return Datas.size();
            }

            @Override
            public Object getItem(int i) {
                return Datas.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(final int i, View view, ViewGroup viewGroup) {
                view = LayoutInflater.from(mContext).inflate(R.layout.list_item, viewGroup, false);
                TextView nameView = view.findViewById(R.id.text);
                TextView pairView = view.findViewById(R.id.pair);

                if (Datas.get(i).getBondState() == BluetoothDevice.BOND_BONDED) {
                    nameView.setText(Datas.get(i).getName() + "(已配对)");
                    pairView.setText("");
                } else {
                    nameView.setText(Datas.get(i).getName() + "(未配对)");
                    pairView.setText("配对");
                    pairView.setTextColor(mContext.getResources().getColor(android.R.color.holo_blue_bright));
                    pairView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtils.showShort("配对");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                                try {
                                    if (ClsUtils.createBond(Datas.get(i).getClass(), Datas.get(i))) {
                                        ToastUtil.toast("发起配对");
                                    }
                                } catch (Exception e) {
                                    ToastUtils.showLong("createBond 反射失败" + e.getMessage());
                                }
                              /*  if (Datas.get(i).createBond()) {
                                    ToastUtils.showLong("正在配对，请稍等...");
                                } else {
                                    ToastUtils.showLong("配对失败");
                                }*/

                            }
                        }
                    });
                }

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showShort("正在连接，请稍候...");
                        // connect(Datas.get(i));
                    }
                });

                // 此处需要返回view 不能是view中某一个
                return view;
            }
        }
    }

    //检查蓝牙是否可用
    public boolean isAvailable() {
        if (mBluetoothAdapter == null) {
            return false;
        }
        return true;
    }

    /*   系统将显示对话框，请求用户允许启用蓝牙。如果用户响应“Yes”，系统将开始启用蓝牙，
        并在该过程完成（或失败）后将焦点返回到您的应用。
        传递给 startActivityForResult() 的 REQUEST_ENABLE_BT必须大于 0，
        系统会将其作为 requestCode 参数传递回您的 onActivityResult() 实现。
        如果成功启用蓝牙，您的 Activity 将会在 onActivityResult() 回调中收到 RESULT_OK 结果代码。
        如果由于某个错误（或用户响应“No”）而没有启用蓝牙，则结果代码为 RESULT_CANCELED。*/
    public void enableBluetooth() {
        if (!isAvailable()) {
            ToastUtils.showShort("蓝牙不可用");
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (context instanceof Activity) {
                Activity act = (Activity) context;
                act.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
      /*  if (!mBluetoothAdapter.isEnabled()) {
            //若没打开则打开蓝牙
            mBluetoothAdapter.enable();

        }*/
    }

    public void connect(BluetoothDevice device) {

        new ConnectThread(device).start();
    }

    public boolean isConnected(String BluetoothName) {
        if (connectedBTThreads.containsKey(BluetoothName)) {
            ConnectedThread connectedThread = connectedBTThreads.get(BluetoothName);
            if (connectedThread == null) {
                return false;
            }
            if (connectedThread.isConnected() && connectedThread.isAlive()) {
                return true;
            }
        }
        return false;
    }

    //查询配对的设备
    public Set<BluetoothDevice> getPairedDevices() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        return pairedDevices;
    }


    public void registerDiscoveryReceiver() {
        if(isRegisterDiscoveryReceiver){
            return;
        }
        mFoundDeviceReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                if (device == null || device.getName() == null) {
                    return;
                }

                Log.e("xiong", device.getName() + "->rssi:" + rssi);
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    Message message = handler.obtainMessage();
                    message.obj = device;
                    message.what = MESSAGE_FOUND_DEVICE;
                    handler.sendMessage(message);
                }
                if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                    ToastUtils.showShort("ACTION_BOND_STATE_CHANGED");
                    Log.e("xiong", "ACTION_BOND_STATE_CHANGED");
                    ToastUtils.showLong("配对成功");
                    for (int i = 0; i < devices.size(); i++) {
                        if (devices.get(i).getAddress().equals(device.getAddress())) {
                            devices.set(i, device);
                            alertListView.notifyDataSetChanged();
                        }
                    }
                }
                if (BluetoothDevice.ACTION_PAIRING_REQUEST.equals(action)) {


                    ToastUtils.showLong("ACTION_PAIRING_REQUEST");
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(mFoundDeviceReceiver, filter); // Don't forget to unregister during onDestroy
        isRegisterDiscoveryReceiver = true;
    }

    public void startScan() {
        if (!isAvailable()) {
            ToastUtils.showShort("蓝牙不可用");
            return;
        }

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        if (mBluetoothAdapter.startDiscovery()) {
            ToastUtils.showShort("startDiscovery success!");
        } else {
            ToastUtils.showShort("startDiscovery failed!");
        }
    }

    public void startScanning() {
        if (mScanCallback == null) {
            Log.d("xiong", "Starting Scanning");

        /*    // Will stop the scanning after a set time.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopScanning();
                }
            }, SCAN_PERIOD);*/

            // Kick off a new scan.
            mScanCallback = new SampleScanCallback();
            mBluetoothAdapter.getBluetoothLeScanner().startScan(buildScanFilters(), buildScanSettings(), mScanCallback);

        } else {
            Toast.makeText(context, "正在扫描", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Stop scanning for BLE Advertisements.
     */
    public void stopScanning() {
        Log.d("xiong", "Stopping Scanning");

        // Stop the scan, wipe the callback.
        mBluetoothLeScanner.stopScan(mScanCallback);
        mScanCallback = null;

        // Even if no new results, update 'last seen' times.
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Return a List of {@link ScanFilter} objects to filter by Service UUID.
     */
    private List<ScanFilter> buildScanFilters() {
        List<ScanFilter> scanFilters = new ArrayList<>();

        ScanFilter.Builder builder = new ScanFilter.Builder();
        // Comment out the below line to see all BLE devices around you
        // builder.setServiceUuid(Service_UUID);
        scanFilters.add(builder.build());

        return scanFilters;
    }

    /**
     * Return a {@link ScanSettings} object set to use low power (to preserve battery life).
     */
    private ScanSettings buildScanSettings() {
        ScanSettings.Builder builder = new ScanSettings.Builder();
        builder.setScanMode(ScanSettings.SCAN_MODE_LOW_POWER);
        return builder.build();
    }

    /**
     * Custom ScanCallback object - adds to adapter on success, displays error on failure.
     */
    private class SampleScanCallback extends ScanCallback {

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);


            for (ScanResult result : results) {
                //  mAdapter.add(result);
                MyLog.e(result.getDevice().getName());
            }
            //  mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            if (result != null && result.getDevice() != null && result.getDevice().getName() != null) {
                MyLog.e(result.getDevice().getName());
                Log.e("xiong", result.getDevice().getName() + ":" + result.getDevice().getAddress() + "-->rssi:" + result.getRssi());
            }


            // mAdapter.add(result);
            // mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Toast.makeText(context, "Scan failed with error: " + errorCode, Toast.LENGTH_LONG)
                    .show();
        }
    }


    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.e("xiong", device.getName());
        }
    };

    public void startLeScan(boolean enable) {

        if (enable) {
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(leScanCallback);

                }
            }, 1000);

            mScanning = true;
            mBluetoothAdapter.startLeScan(leScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(leScanCallback);
        }

    }


    public void unRegisterDiscoveryReceiver(Context context) {
        if(!isRegisterDiscoveryReceiver){
            return;
        }
        if (mFoundDeviceReceiver != null) {
            context.unregisterReceiver(mFoundDeviceReceiver);
        }
    }

    /*   如果您希望将本地设备设为可被其他设备检测到，请使用 ACTION_REQUEST_DISCOVERABLE 操作 Intent
       调用 startActivityForResult(Intent, int)。 这将通过系统设置发出启用可检测到模式的请求（无需停止您的应用）
       默认情况下，设备将变为可检测到并持续 120 秒钟。
       您可以通过添加 EXTRA_DISCOVERABLE_DURATION Intent Extra 来定义不同的持续时间。
       应用可以设置的最大持续时间为 3600 秒，值为 0 则表示设备始终可检测到。
       任何小于 0 或大于 3600 的值都会自动设为 120 秒*/
    public void requestDiscoverable() {
        if (!isAvailable()) {
            ToastUtils.showShort("蓝牙不可用");
            return;
        }

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        if (context instanceof Activity) {
            Activity act = (Activity) context;
            act.startActivityForResult(discoverableIntent, REQUEST_DISCOVERABLE);
        }
    }

    public void startBluetoothServer() {
        if (!isAvailable()) {
            ToastUtils.showShort("蓝牙不可用");
            return;
        }
        if (acceptThread == null) {
            acceptThread = new AcceptThread();
        }
        if (acceptThread.isInterrupted() || acceptThread.isAlive()) {
            return;
        }
        acceptThread.start();
    }

    public void setDiscoverableTimeout() {

        try {
            Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod("setDiscoverableTimeout", int.class);
            setDiscoverableTimeout.setAccessible(true);
            Method setScanMode = BluetoothAdapter.class.getMethod("setScanMode", int.class, int.class);
            setScanMode.setAccessible(true);
            setDiscoverableTimeout.invoke(mBluetoothAdapter, 0);
            setScanMode.invoke(mBluetoothAdapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, 0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Bluetooth", "setDiscoverableTimeout failure:" + e.getMessage());
            ToastUtil.toast("设置蓝牙可见失败！");
        }
    }

    public void closeDiscoverableTimeout() {

        try {
            Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod("setDiscoverableTimeout", int.class);
            setDiscoverableTimeout.setAccessible(true);
            Method setScanMode = BluetoothAdapter.class.getMethod("setScanMode", int.class, int.class);
            setScanMode.setAccessible(true);
            setDiscoverableTimeout.invoke(mBluetoothAdapter, 1);
            setScanMode.invoke(mBluetoothAdapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void manageConnectedSocket(BluetoothSocket mmSocket) {

        ConnectedThread connectedThread = new ConnectedThread(mmSocket);
        connectedThread.start();
        connectedBTThreads.put(mmSocket.getRemoteDevice().getName(), connectedThread);

    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                EventBusUtils.sendFailLog("create ServerSocket failed" + "#:" + e.getMessage());
                interrupted();
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (!interrupted()) {
                try {
                    if (mmServerSocket != null) {
                        socket = mmServerSocket.accept();
                    }

                } catch (IOException e) {

                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
                    Log.e("xiong", "accept   bluetooth socket:" + socket.getRemoteDevice().getName());
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    msg.what = ON_CONNECTED;
                    bundle.putString("name", socket.getRemoteDevice().getName());
                    bundle.putString("type", onMessageLisenter.BT_CONNECTED);
                    handler.sendMessage(msg);
                    manageConnectedSocket(socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        /**
         * Will cancel the listening socket, and cause the thread to finish
         */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
            }
        }
    }


    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final

            BluetoothSocket tmp = null;
            mmDevice = device;
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                //  // 这里的 UUID 需要和服务器的一致
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);

            } catch (IOException e) {
                EventBusUtils.sendFailLog(e.getMessage() + "#蓝牙未开启或不在通信范围");
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                if (!mmSocket.isConnected()) {
                    mmSocket.connect();
                }
                Message msg = handler.obtainMessage();
                msg.what = ON_CONNECTED;
                Bundle bundle = new Bundle();
                bundle.putString("name", mmDevice.getName());
                bundle.putString("type", onMessageLisenter.BT_CONNECTED);
                msg.setData(bundle);
                MyLog.e("name->" + mmDevice.getName() + "连接成功");
                handler.sendMessage(msg);

            } catch (IOException connectException) {
                EventBusUtils.sendFailLog(connectException.getMessage() + "  #蓝牙未开启或不在通信范围);");
                sendEventMessage(ON_CONNECT_FAILED, mmDevice.getName());
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                }
                return;
            }
            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket);
        }
    }


    private void sendEventMessage(int what, String name) {
        Message msg = handler.obtainMessage();
        msg.what = what;
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public void close() {

            try {
                mmInStream.close();

            } catch (IOException e) {
                e.printStackTrace();
                EventBusUtils.sendFailLog(e.getMessage() + "#" + "关闭输入流失败");
            }
            try {
                mmOutStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                EventBusUtils.sendFailLog(e.getMessage() + "#" + "关闭输出流失败");
            }
            try {
                mmSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
                EventBusUtils.sendFailLog(e.getMessage() + "#" + "关闭socket失败");
            }
            interrupt();
        }


        public boolean isConnected() {
            if (mmSocket != null && mmSocket.isConnected()) {
                return true;
            }
            return false;
        }

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;

        }

        public void run() {


            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (!isInterrupted()) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    Message msg = handler.obtainMessage();
                    msg.what = MESSAGE_READ;
                    Bundle bundle = new Bundle();
                    bundle.putString("name", mmSocket.getRemoteDevice().getName());
                    bundle.putString("str_content", new String(getValidBytes(buffer, bytes), "UTF-8"));
                    //  bundle.putByteArray("content", buffer);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                    buffer = new byte[1024];
                } catch (IOException e) {
                    EventBusUtils.sendFailLog(e.getMessage() + "####ConnetThread 读取消息失败,关闭io流，关闭socket,结束线程");
                    close();
                }
            }
            EventBusUtils.sendFailLog(mmSocket.getRemoteDevice().getName() + "读消息线程结束");
        }


        private byte[] getValidBytes(byte[] bytes, int num) {
            byte[] tb = new byte[num];
            for (int i = 0; i < num; i++) {
                tb[i] = bytes[i];
            }
            return tb;
        }

        public void write(String msg) {
            try {
                mmOutStream.write(msg.getBytes("UTF-8"));
            } catch (IOException e) {
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }


}
