package com.ihome.smarthome.module.base;

import android.Manifest;
import android.annotation.SuppressLint;
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

import com.blankj.utilcode.util.ToastUtils;
import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ActivityManager;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.github.rubensousa.floatingtoolbar.FloatingToolbarMenuBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ihome.base.base.BaseActivity;
import com.ihome.base.utils.ScenceUtils;
import com.ihome.smarthome.utils.SystemTTSUtils;
import com.ihome.smarthome.R;
import com.ihome.smarthome.applockscreen.service.LockScreenService;
import com.ihome.smarthome.module.base.communicate.ICommunicate;
import com.ihome.smarthome.receiver.BluetoothMonitorReceiver;
import com.ihome.smarthome.service.AlarmService;
import com.ihome.smarthome.service.BluetoothService;
import com.ihome.smarthome.service.FloatingService;
import com.ihome.smarthome.utils.EventBusUtils;
import com.ihome.smarthome.utils.SpanUtils;


public class LoginActivity extends BaseActivity implements ICommunicate.onMessageLisenter {

    //private MyBluetoothManager communicateDevice;
    final int PERMISSION_REQUEST_COARSE_LOCATION = 1001;
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

    ServiceConnection serviceConnection = new ServiceConnection() {
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

    private void bindService(){
        Intent intent = new Intent(this,BluetoothService.class);
        bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
    }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
    protected void bindView() {
        MyLog.e("bindView");
        setContentView(R.layout.activity_login1);
        startFloatingService();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                init();
            }
        },2000);

    }


    private void init(){
        checkBatteryOptimizations();
        setOnclickListener();
        initFloatBar();
        requestPermission();
        registerHomeKeyReceiver();
        registerMonitorReceiver();
        SystemTTSUtils.getInstance(this) ;
        bindService();
        AlarmService.startService(this);
        startService(new Intent(LoginActivity.this, LockScreenService.class));
    }


    private void  checkBatteryOptimizations(){
        if(isIgnoringBatteryOptimizations()){
            ToastUtil.toast("已在白名单中");
        }else{
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






    private void setOnclickListener(){
        View cvCooker = findViewById(R.id.cv_cooker);
        cvCooker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothService.getCommunicateDevice().isConnected("cooker")) {
                    ActivityManager.startActivity(BoilerActivity.class);
                    return;
                }
                ScenceUtils.showCutscence(LoginActivity.this, "正在连接蓝牙设备...");
                bluetoothService.getCommunicateDevice().connect("cooker");
            }
        });
    }

    private void requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
    }

    private void registerHomeKeyReceiver(){
        homeReceiver=new HomeKeyEventBroadCastReceiver();
        registerReceiver(homeReceiver, new IntentFilter(
                Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }



    private void setCookerInfo(){
       boolean isConnect =   bluetoothService.getCommunicateDevice().isConnected("cooker");
        setCookerConnectState(isConnect);
    }


    private void setCookerConnectState(boolean isConnect){
        TextView tv_status = findViewById(R.id.tv_status);
        if(tv_status==null){
            ToastUtil.toast("tv_status==null");
        }

        String state = isConnect?"已连接":"未连接";

        int color = isConnect?Color.BLUE:Color.RED;

        SpanUtils.newInstance().append("连接状态:", Color.BLACK)
                .append(state,color)
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


    @SuppressLint("ShowToast")
    public void startFloatingService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT);
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 0);
            return;
        }
        FloatingService.startService(this);
    }



    private void initFloatBar() {
        FloatingActionButton fab = findViewById(R.id.fab);
        mFloatingToolbar = findViewById(R.id.floatingToolbar);
        mFloatingToolbar.setClickListener(new FloatingToolbar.ItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_unread:
                        bluetoothService.getCommunicateDevice().registerDiscoveryReceiver(LoginActivity.this);
                        bluetoothService.getCommunicateDevice().startScan();break;
                    case R.id.action_copy: startFloatingService();break;
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
        EventBusUtils.sendDeBugLog(name+":已连接");
        if(name.equals("cooker")){
            setCookerConnectState(true);
        }
    }


    @Override
    public void onDisConnect(String name, String msg) {
        String tip = name + "#" + msg;
        ToastUtils.showShort("onDisconnect!!"+tip);
        EventBusUtils.sendDeBugLog(tip);
        ScenceUtils.closeCutScence(this);
        if(name.equals("cooker")){
            setCookerConnectState(false);
        }

    }

    @Override
    public void onError(String name, String err_msg) {
        String tip = name + "#" + err_msg;
        ToastUtils.showShort("!!!!!ERROR"+tip);
        EventBusUtils.sendFailLog(tip);
        ScenceUtils.closeCutScence(this);
        if(name.equals("cooker")){
            setCookerConnectState(false);
        }
    }

    @Override
    public void onConnectFailed(String name, String err_msg) {
        ScenceUtils.closeCutScence(this);
        ToastUtil.toast(name+err_msg);
        if(name.equals("cooker")){
            setCookerConnectState(false);
        }
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(homeReceiver);
        unregisterReceiver(this.bleListenerReceiver);

        unbindService(serviceConnection);
        MyLog.e("onDestroy");
        bluetoothService.getCommunicateDevice().unRegisterDiscoveryReceiver(this);


    }

    private void stopFloatingService() {
        stopService(new Intent(LoginActivity.this, FloatingService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // bluetoothService.getCommunicateDevice().onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                startService(new Intent(LoginActivity.this, FloatingService.class));
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO request success

                }
                break;

        }
    }
}



