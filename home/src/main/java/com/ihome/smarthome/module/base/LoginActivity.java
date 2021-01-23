package com.ihome.smarthome.module.base;

import android.Manifest;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.blankj.utilcode.util.ToastUtils;
import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ActivityManager;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.github.rubensousa.floatingtoolbar.FloatingToolbarMenuBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ihome.base.base.BaseActivity;
import com.ihome.base.utils.DialogUtils;
import com.ihome.base.utils.ScenceUtils;
import com.ihome.base.utils.SystemTTS;
import com.ihome.smarthome.applockscreen.service.LockScreenService;
import com.ihome.smarthome.service.AlarmService;
import com.ihome.smarthome.utils.SystemTTSUtils;
import com.ihome.smarthome.R;
import com.ihome.smarthome.module.base.communicate.ICommunicate;
import com.ihome.smarthome.receiver.BluetoothMonitorReceiver;
import com.ihome.smarthome.service.BluetoothService;
import com.ihome.smarthome.service.FloatingService;
import com.ihome.smarthome.utils.EventBusUtils;
import com.ihome.smarthome.utils.SpanUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class LoginActivity extends BaseActivity implements ICommunicate.onMessageLisenter {

    private final static int REQUEST_OVERLAY_PERMISSION = 1001;
    private final static int REQUEST_PERMISSION_CODE = 1002;


    private FloatingToolbar mFloatingToolbar;
    private HomeKeyEventBroadCastReceiver homeReceiver;
    private BluetoothMonitorReceiver bleListenerReceiver = null;

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

                    } else if (reason.equals(SYSTEM_RECENT_APPS)) {
                        //   stopFloatingService();
                    }
                }
            }
        }
    }


    BluetoothService bluetoothService;
    FloatingService  floatingService;

    ServiceConnection btServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bluetoothService = ((BluetoothService.MyBinder) service).getService();
            bluetoothService.startService(LoginActivity.this);
            bluetoothService.getCommunicateDevice().connect("cooker");
            EventBusUtils.sendSucessLog("onServiceConnected");
            setCookerInfo();

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
    };






    private void bindBluetoothService() {
        Intent intent = new Intent(this, BluetoothService.class);
        bindService(intent, btServiceConnection, Service.BIND_AUTO_CREATE);
    }

    private void bindFloatingService() {
        Intent intent = new Intent(this, FloatingService.class);
        bindService(intent, floatingServiceConnection, Service.BIND_AUTO_CREATE);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void bindView() {
        setContentView(R.layout.activity_login1);
        setOnclickListener();
        initFloatBar();

        startFloatingService();



       /* if (!isHasNeededPermission()) {
            requestPermission();
            return;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }, 2000);
*/
    }


    private void init() {
        if(!isHasNeededPermission()){
            requestPermission();
            return;
        }

        registerHomeKeyReceiver();
        registerMonitorReceiver();
         bindBluetoothService();
        AlarmService.startService(this);
        startService(new Intent(LoginActivity.this, LockScreenService.class));
        SystemTTSUtils.getInstance(this);
    }


    private void checkBatteryOptimizations() {
        if (isIgnoringBatteryOptimizations()) {
            ToastUtil.toast("已在白名单中");
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


    private void setOnclickListener() {
        View cvCooker = findViewById(R.id.cv_cooker);
        cvCooker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.startActivity(BoilerActivity.class);
                if (bluetoothService.getCommunicateDevice().isConnected("cooker")) {
                    ActivityManager.startActivity(BoilerActivity.class);
                    return;
                }
                ScenceUtils.showCutscence(LoginActivity.this, "正在连接蓝牙设备...");
                bluetoothService.getCommunicateDevice().connect("cooker");
            }
        });
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
        boolean isConnect = bluetoothService.getCommunicateDevice().isConnected("cooker");
        setCookerConnectState(isConnect);
    }


    private void setCookerConnectState(boolean isConnect) {
        TextView tv_status = findViewById(R.id.tv_status);
        if (tv_status == null) {
            ToastUtil.toast("tv_status==null");
        }

        String state = isConnect ? "已连接" : "未连接";

        int color = isConnect ? Color.BLUE : Color.RED;

        SpanUtils.newInstance().append("连接状态:", Color.BLACK)
                .append(state, color)
                .setText(tv_status);
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

    private void requestOverlayPermission() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
    }


    public void startFloatingService() {
        if(!isHasOverlaysPermission()){

            DialogUtils.showDialog(this, "应用需要悬浮窗权限，请打开此应用悬浮窗权限", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {

                    requestOverlayPermission();
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
            return;
        }else{
             bindFloatingService();
        }
    }


    private void initFloatBar() {
        FloatingActionButton fab = findViewById(R.id.fab);
        mFloatingToolbar = findViewById(R.id.floatingToolbar);
        mFloatingToolbar.setClickListener(new FloatingToolbar.ItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_unread:
                        bluetoothService.getCommunicateDevice().registerDiscoveryReceiver(LoginActivity.this);
                        bluetoothService.getCommunicateDevice().startScan();
                        break;
                    case R.id.action_copy:
                        floatingService.setRootViewVisible();
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

        //Create a custom menu
        mFloatingToolbar.setMenu(new FloatingToolbarMenuBuilder(this)
                .addItem(R.id.action_unread, R.drawable.ic_baseline_bluetooth_searching_24, "Mark unread")
                .addItem(R.id.action_copy, R.drawable.ic_outline_insert_drive_file_24, "Copy")
                .build());
    }


    @Override
    public void onMessage(String name, String msg) {
        EventBusUtils.sendDeBugLog(name + "->:" + msg);
        //  ToastUtils.showShort(name + ":" + msg);
        //  EventBusUtils.sendDeBugLog("receive msg:" + name + ":" + msg);
    }

    @Override
    public void onConnect(String name, String type) {
        ScenceUtils.closeCutScence(this);
        EventBusUtils.sendDeBugLog(name + ":已连接");
        if (name.equals("cooker")) {
            setCookerConnectState(true);
        }
    }


    @Override
    public void onDisConnect(String name, String msg) {
        String tip = name + "#" + msg;
        ToastUtils.showShort("onDisconnect!!" + tip);
        EventBusUtils.sendDeBugLog(tip);
        ScenceUtils.closeCutScence(this);
        if (name.equals("cooker")) {
            setCookerConnectState(false);
        }

    }

    @Override
    public void onError(String name, String err_msg) {
        String tip = name + "#" + err_msg;
        ToastUtils.showShort("!!!!!ERROR" + tip);
        EventBusUtils.sendFailLog(tip);
        ScenceUtils.closeCutScence(this);
        if (name.equals("cooker")) {
            setCookerConnectState(false);
        }
    }

    @Override
    public void onConnectFailed(String name, String err_msg) {
        ScenceUtils.closeCutScence(this);
        ToastUtil.toast(name + err_msg);
        if (name.equals("cooker")) {
            setCookerConnectState(false);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(homeReceiver);
        unregisterReceiver(this.bleListenerReceiver);

        unbindService(btServiceConnection);
        unbindService(floatingServiceConnection);
        MyLog.e("onDestroy");
        bluetoothService.getCommunicateDevice().unRegisterDiscoveryReceiver(this);


    }

    private void stopFloatingService() {
        stopService(new Intent(LoginActivity.this, FloatingService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                bindFloatingService();
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
}



