package com.ihome.smarthome.module.base;

import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.GsonUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ActivityManager;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.github.rubensousa.floatingtoolbar.FloatingToolbarMenuBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ihome.base.base.BaseActivity;
import com.ihome.base.utils.DialogUtils;
import com.ihome.base.utils.ScenceUtils;
import com.ihome.smarthome.applockscreen.service.LockScreenService;
import com.ihome.smarthome.module.adapter.DeviceListAdapter;
import com.ihome.smarthome.module.base.communicate.MyBluetoothManager;
import com.ihome.smarthome.module.base.communicate.MySocketManager;
import com.ihome.smarthome.module.base.eventbusmodel.BTMessageEvent;
import com.ihome.smarthome.module.base.eventbusmodel.BaseMessageEvent;
import com.ihome.smarthome.module.base.eventbusmodel.LogEvent;
import com.ihome.smarthome.receiver.AdminReceiver;
import com.ihome.smarthome.service.AlarmService;
import com.ihome.smarthome.utils.CrashHandlerUtils;
import com.ihome.smarthome.utils.EventBusUtils;
import com.ihome.smarthome.utils.SystemTTSUtils;
import com.ihome.smarthome.R;
import com.ihome.smarthome.module.base.communicate.ICommunicate;
import com.ihome.smarthome.receiver.BluetoothMonitorReceiver;
import com.ihome.smarthome.service.ConnectionService;
import com.ihome.smarthome.service.FloatingService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class HomeActivity extends BaseActivity {

    private final static int REQUEST_OVERLAY_PERMISSION = 1001;
    private final static int REQUEST_PERMISSION_CODE = 1002;

    private FloatingToolbar mFloatingToolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private HomeKeyEventBroadCastReceiver homeReceiver;
    private BluetoothMonitorReceiver bleListenerReceiver = null;
    private ConnectionService connectionService;
    private FloatingService floatingService;

    private List<DeviceItem> deviceList = new ArrayList<>();
    DeviceListAdapter adapter = new DeviceListAdapter(deviceList);

    class HomeKeyEventBroadCastReceiver extends BroadcastReceiver {
        static final String SYSTEM_REASON = "reason";
        static final String SYSTEM_HOME_KEY = "homekey";// home key
        static final String SYSTEM_RECENT_APPS = "recentapps";// long home key

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (reason != null) {
                    if (reason.equals(SYSTEM_HOME_KEY)) {
                        EventBusUtils.sendLog(getTAG(), "SYSTEM_HOME_KEY", LogEvent.LOG_IMPORTANT, true);
                    } else if (reason.equals(SYSTEM_RECENT_APPS)) {
                        //   stopFloatingService();
                        EventBusUtils.sendLog(getTAG(), "SYSTEM_RECENT_APPS", LogEvent.LOG_IMPORTANT, true);
                    }
                }
            }
        }
    }

   /* ServiceConnection btServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bluetoothService = ((BluetoothService.MyBinder) service).getService();
            bluetoothService.startBluetoothService();
            bluetoothService.getCommunicateDevice().connect("cooker");
            EventBusUtils.sendSucessLog("onServiceConnected");


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            EventBusUtils.sendSucessLog("onServiceDisconnected");
        }

        @Override
        public void onBindingDied(ComponentName name) {
            EventBusUtils.sendSucessLog("onBindingDied");
        }

        @Override
        public void onNullBinding(ComponentName name) {
            EventBusUtils.sendSucessLog("onNullBinding");
        }
    };
    ServiceConnection floatingServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyLog.e("floatingServiceConnection onServiceConnected");
            floatingService = ((FloatingService.MyBinder) service).getService();
            init();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            MyLog.e("floatingServiceConnection onServiceDisconnected");
        }

        @Override
        public void onBindingDied(ComponentName name) {
            MyLog.e("floatingServiceConnection onBindingDied");
        }

        @Override
        public void onNullBinding(ComponentName name) {
            MyLog.e("floatingServiceConnection onNullBinding");
        }
    };*/


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BTMessageEvent event) {
        switch (event.getMessageType()) {
            case BTMessageEvent.ON_CONNECT_FAILED:
                adapter.refreshItem(event.getMacAddress(), 2);
                ToastUtil.toast(event.getMessage());
                ScenceUtils.closeCutScence(this);
                break;
            default:
                break;

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void bindView() {

        EventBusUtils.sendLog(getTAG(), "LoginActivity bindView", LogEvent.LOG_IMPORTANT, true);
        setContentView(R.layout.fragment_home);
        EventBus.getDefault().register(this);
        initView();
        initFloatBar();
        initRecyclerview();
        startFloatingService();
        startBluetoothService();
        startLockScreenService();
        startAlarmService();
        init();
       // getAdminOwner();
        enableDeviceAdmin();
      //  requestIgnoreBatteryOptimizationsSETTINGS();
       // ActivityManager.startActivity(ProfileActivity.class);

    }



    private void getAdminOwner(){
        Intent intent = new Intent(
                DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                getPackageName());
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "提示文字");
        startActivityForResult(intent, 1);

    }



    private void active(){

    }




    private void enableDeviceAdmin() {

        ComponentName deviceAdmin = new ComponentName(getApplication(), AdminReceiver.class);
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
   /*     if (!devicePolicyManager.isAdminActive(deviceAdmin)) {
            Intent intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdmin);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "激活此设备管理员后可免root停用应用")
            startActivityForResult(intent, 1)
        } else {
            Toast.makeText(this, "此App已激活设备管理器", Toast.LENGTH_SHORT).show()
        }
        */



        // First of all, to access anything you must be device owner
        if (devicePolicyManager.isDeviceOwnerApp(getPackageName())) {
            // If not device admin, ask to become one
            if (!devicePolicyManager.isAdminActive(deviceAdmin) &&
                    devicePolicyManager.isDeviceOwnerApp(getPackageName())) {

                Log.v(getTAG(), "Not device admin. Asking device owner to become one.");

                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdmin);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "You need to be a device admin to enable device admin.");

                startActivity(intent);
            }

            // Device owner and admin : enter device admin
            else {
                devicePolicyManager.setLockTaskPackages(deviceAdmin, new String[]{
                        getPackageName(), /* PUT OTHER PACKAGE NAMES HERE! */
            });
            startLockTask();
        }
    }
    // Not device owner - can't access anything
         else {
        Log.v(getTAG(), "Not device owner");
        Toast.makeText(this, "Not device owner", Toast.LENGTH_SHORT).show();
    }
}


    private void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        fab = findViewById(R.id.fab);
        mFloatingToolbar = findViewById(R.id.floatingToolbar);

    }

    private void test(){
        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED //锁屏状态下显示
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD //解锁

                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); //打开屏幕
    }


    private void initRecyclerview() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(adapter);
        adapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                int type = deviceList.get(position).getDeviceType();
                if (type == DeviceConstant.BT_COOKER) {
                    return 2;
                }
                return 4;
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                //  DeviceItem deviceItem = (DeviceItem) adapter.getData().get(position);
                //  connectDevice(deviceItem);
                if (position == 0) {
                    ActivityManager.startActivity(LogActivity.class);
                }
            }
        });
        setDeviceList();
        // addListener();
        // connectAllDevices();
    }

    private void setDeviceList() {
        Set<BluetoothDevice> deviceSet = MyBluetoothManager.getInstance().getPairedDevices();
        for (BluetoothDevice device : deviceSet) {
            deviceList.add(new BluetoothDeviceItem(device.getName(), DeviceConstant.BT_COOKER, device.getAddress()));
        }

    }

    private void connectAllDevices() {
        Set<BluetoothDevice> deviceSet = MyBluetoothManager.getInstance().getPairedDevices();
        for (BluetoothDevice device : deviceSet) {
            MyBluetoothManager.getInstance().connect(device.getAddress());
        }
    }

    Pair<String, ICommunicate.Listener> listenerPair;

    private void addListener() {
        BluetoothDevice device = MyBluetoothManager.getInstance().findPairedBlueToothDeviceByName("cooker");
        listenerPair = new Pair<>(device.getAddress(), new ICommunicate.Listener() {
            @Override
            public void onMessage(BaseMessageEvent event) {
                if (event == null) {
                    return;
                }
                if (event instanceof BTMessageEvent) {
                    BTMessageEvent btMessageEvent = (BTMessageEvent) event;
                    if (btMessageEvent.getData() == null) {
                        return;
                    }
                    String json_data = btMessageEvent.getData().getJson_msg();
                    if (json_data == null) {
                        return;
                    }
                    DHT dht = GsonUtils.fromJson(json_data, DHT.class);
                    ToastUtil.toast(dht.getTemp() + "/" + dht.getHumi());
                }
            }
        });
        MyBluetoothManager.getInstance().addListener(listenerPair);
    }

    private void removeListener() {
        MyBluetoothManager.getInstance().removeListener(listenerPair);
    }


    private void init() {
        if (!isHasNeededPermission()) {
            requestPermission();
            return;
        }
        checkBatteryOptimizations();
        // registerHomeKeyReceiver();
        registerMonitorReceiver();
        startBluetoothService();
    }

/*
    private void bindBluetoothService() {
        Intent intent = new Intent(this, BluetoothService.class);
        bindService(intent, btServiceConnection, Service.BIND_AUTO_CREATE);
    }

    private void bindFloatingService() {
        Intent intent = new Intent(this, FloatingService.class);
        bindService(intent, floatingServiceConnection, Service.BIND_AUTO_CREATE);
    }
*/

    private void initSystemTTSUtils() {
        SystemTTSUtils.getInstance(this);
    }

    private void startAlarmService() {
        AlarmService.startService(this);
    }

    private void startBluetoothService() {
        ConnectionService.startService(this);
    }

    private void startLockScreenService() {
        LockScreenService.startService(this);
    }

    private void stopFloatingService() {
        stopService(new Intent(HomeActivity.this, FloatingService.class));
    }

    private void stopScreenLockService() {
        stopService(new Intent(HomeActivity.this, LockScreenService.class));
    }

    private void stopConnectService() {
        stopService(new Intent(HomeActivity.this, ConnectionService.class));
    }


    private void checkBatteryOptimizations() {
        if (isIgnoringBatteryOptimizations()) {
            // ToastUtil.toast("已在白名单中");
        } else {
            ToastUtil.toast("申请加入白名单");
            requestIgnoreBatteryOptimizations();
        }
    }


    public void requestIgnoreBatteryOptimizations() {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void requestIgnoreBatteryOptimizationsSETTINGS() {
        try {
            Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private boolean isIgnoringBatteryOptimizations() {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                isIgnoring = powerManager.isIgnoringBatteryOptimizations(getPackageName());
            }
        }
        return isIgnoring;
    }


    private void connectDevice(DeviceItem deviceItem) {
        if (MyBluetoothManager.getInstance().isConnected(deviceItem.getDeviceId())) {
            ToastUtil.toast("设备已连接");
            return;
        }
        ScenceUtils.showCutscence(HomeActivity.this, "正在连接蓝牙设备...");
        MyBluetoothManager.getInstance().connect(deviceItem.getDeviceId());
    }

    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    private boolean isHasNeededPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            )
                return false;
        }
        return true;
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
    }

    private void registerHomeKeyReceiver() {
        homeReceiver = new HomeKeyEventBroadCastReceiver();
        registerReceiver(homeReceiver, new IntentFilter(
                Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }


    private void setCookerInfo() {
        boolean isConnect = connectionService.getBluetoothManager().isConnected("cooker");
        // setCookerConnectState(isConnect);
    }


    private void registerMonitorReceiver() {
        // 初始化广播
        this.bleListenerReceiver = new BluetoothMonitorReceiver();
        IntentFilter intentFilter = new IntentFilter();
        // 监视蓝牙关闭和打开的状态
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        // 监视蓝牙设备与APP连接的状态
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        // 注册广播
        registerReceiver(this.bleListenerReceiver, intentFilter);
    }


    private boolean isHasOverlaysPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestOverlayPermission() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
    }


    public void startFloatingService() {
        if (!isHasOverlaysPermission()) {

            DialogUtils.showDialog(this, "应用需要悬浮窗权限，请打开此应用悬浮窗权限", new SweetAlertDialog.OnSweetClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {

                    requestOverlayPermission();
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
            return;
        } else {
            // bindFloatingService();
            FloatingService.startService(this);
        }
    }


    private void initFloatBar() {


        mFloatingToolbar.setClickListener(new FloatingToolbar.ItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action1:
                       /*  MyBluetoothManager.getInstance().registerDiscoveryReceiver(LoginActivity.this);
                        MyBluetoothManager.getInstance().startScan();*/
                        MySocketManager.getInstance().sendMessage(" android client test");
                        break;
                    case R.id.action2:
                        FloatingService.startCommand(HomeActivity.this, FloatingService.SHOW_FLOATING_SERVICE);
                        break;

                    case R.id.action3:
                     /*   stopConnectService();
                        stopFloatingService();
                        stopScreenLockService();
                        finish();*/
                        MyLog.e("todaylogs:---"+CrashHandlerUtils.getTodayLogs());
                        ToastUtil.toast(CrashHandlerUtils.getTodayLogs());
                        break;
                }
            }

            @Override
            public void onItemLongClick(MenuItem item) {

            }
        });
        mFloatingToolbar.attachFab(fab);
        //mFloatingToolbar.attachRecyclerView(recyclerView);
        mFloatingToolbar.addMorphListener(new FloatingToolbar.MorphListener() {
            @Override
            public void onMorphEnd() {

            }

            @Override
            public void onMorphStart() {

            }

            @Override
            public void onUnmorphStart() {

            }

            @Override
            public void onUnmorphEnd() {

            }
        });


        mFloatingToolbar.setMenu(new FloatingToolbarMenuBuilder(this)
                .addItem(R.id.action1, R.drawable.ic_baseline_bluetooth_searching_24, "Mark unread")
                .addItem(R.id.action2, R.drawable.ic_outline_insert_drive_file_24, "Copy")
                .addItem(R.id.action3, R.drawable.login_act_icon, "exit")
                .build());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
       // unregisterReceiver(homeReceiver);
        unregisterReceiver(bleListenerReceiver);

        //  unbindService(btServiceConnection);
        // unbindService(floatingServiceConnection);
        MyLog.e("onDestroy");
        MyBluetoothManager.getInstance().unRegisterDiscoveryReceiver(this);
        removeListener();
        EventBusUtils.sendLog(getTAG(), getTAG() + "   onDestroyed!", LogEvent.LOG_IMPORTANT, true);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                //bindFloatingService();
                startFloatingService();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO request success
                    init();

                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }


    private void forceExit() {
        stopFloatingService();
        stopConnectService();
    }
}



