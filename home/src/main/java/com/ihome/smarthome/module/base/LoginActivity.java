package com.ihome.smarthome.module.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ActivityManager;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.ihome.base.base.BaseActivity;
import com.ihome.base.utils.ScenceUtils;
import com.ihome.smarthome.R;
import com.ihome.smarthome.databinding.ActivityLogin1Binding;
import com.ihome.smarthome.module.base.communicate.ICommunicate;
import com.ihome.smarthome.module.base.communicate.MyBluetoothManager;
import com.ihome.smarthome.module.base.communicate.MySocketManager;
import com.ihome.smarthome.receiver.BluetoothMonitorReceiver;
import com.ihome.smarthome.utils.EventBusUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * A login screen that offers login via username.
 */
public class LoginActivity extends BaseActivity implements ICommunicate.onMessageLisenter {
    private User user = new User("xiongbin", "123456");
    private MyBluetoothManager communicateDevice;
    //private String communicateMode = SPUtils.getInstance().getString(Constants.COMMUNICATE_MODE);
    public static boolean isStart = false;
    private String communicateMode = Constants.BLUETOOTH_MODE;
      final int PERMISSION_REQUEST_COARSE_LOCATION = 1001;
    private FloatingToolbar mFloatingToolbar;
    ActivityLogin1Binding binding;
    HomeKeyEventBroadCastReceiver homeReceiver;
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


    @Override
    protected void onResume() {
        super.onResume();
        if (!isStart) {
            startFloatingService();


        }
    }


    private void registerMonitorReceiver(){
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


    @Override
    protected void bindView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login1);
        binding.setUser(user);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }



        binding.cvCooker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // MyBluetoothManager.Instance(LoginActivity.this).requestDiscoverable();
                communicateDevice.startScan();
          /*      if(communicateDevice.isConnected("cooker")){
                    ActivityManager.startActivity(BoilerActivity.class);
                    return;
                }
                ScenceUtils.showCutscence(LoginActivity.this,"正在连接蓝牙设备...");
                communicateDevice.connect("cooker");*/

            }
        });

        initCommunicateDevice(communicateMode);

        // 监听home键广播
        homeReceiver = new HomeKeyEventBroadCastReceiver();
        registerReceiver(homeReceiver, new IntentFilter(
                Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        registerMonitorReceiver();

        startFloatingService();

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
        if (!isStart) {
            startService(new Intent(LoginActivity.this, FloatingService.class));
        }
    }

 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                if (!isStart)
                    startService(new Intent(LoginActivity.this, FloatingService.class));
            }
        }
    }

*/




/*
    private void initFloatBar(){
        mFloatingToolbar.setClickListener(this);
        mFloatingToolbar.attachFab(binding.fab);
        mFloatingToolbar.addMorphListener(this);

        //Create a custom menu
        mFloatingToolbar.setMenu(new FloatingToolbarMenuBuilder(this)
                .addItem(R.id.action_unread, R.drawable.ic_markunread_black_24dp, "Mark unread")

                .build());
    }

*/


    private void initCommunicateDevice(String communicateMode) {
        communicateDevice =   MyBluetoothManager.Instance(this);
        communicateDevice.startBluetoothServer();
        communicateDevice.setOnMessageLisenter(this);
    }


    @Override
    public void onMessage(String name, String msg) {
        EventBusUtils.sendDeBugLog(name+"->:"+msg);
        ToastUtils.showShort(name + ":" + msg);
        EventBusUtils.sendDeBugLog("receive msg:"+name + ":" + msg);
    }

    @Override
    public void onConnect(String name, String type) {
        ScenceUtils.closeCutScence(this);
        ToastUtil.toast("连接"+name+"成功！");
        ActivityManager.startActivity(BoilerActivity.class);
    }



    @Override
    public void onDisConnect(String name ,String msg) {
        String tip = name+"#"+msg;
        ToastUtils.showShort(tip);
        EventBusUtils.sendDeBugLog(tip);
        ScenceUtils.closeCutScence(this);
    }

    @Override
    public void onError(String name,String err_msg) {
        String tip = name+"#"+err_msg;
        ToastUtils.showShort(tip);
        EventBusUtils.sendFailLog(tip);
        ScenceUtils.closeCutScence(this);
    }


    public void onClickLogin(View view) {
        if (communicateDevice == null || !communicateDevice.isConnected("")) {
            ToastUtils.showShort("通信未连接");
            return;
        }
        communicateDevice.sendMessage("",user.username);
    }


    //菜单触发事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    /*    switch (item.getItemId()) {
            case R.id.action_bluetooth:
                communicateMode = Constants.BLUETOOTH_MODE;
                break;
            case R.id.action_wire_less:
                communicateMode = Constants.WIFI_MODE;
                break;
        }*/
        SPUtils.getInstance().put(Constants.COMMUNICATE_MODE, communicateMode);
        initCommunicateDevice(communicateMode);
        communicateDevice.connect();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(homeReceiver);
        unregisterReceiver(this.bleListenerReceiver);
        communicateDevice.destroy();

    }


    private void stopFloatingService() {
        stopService(new Intent(LoginActivity.this, FloatingService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        communicateDevice.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                if (!isStart)
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
                    communicateDevice.startScan();
                }
                break;

        }
    }
}



