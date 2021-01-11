package com.ihome.smarthome.module.base;

import android.Manifest;
import android.annotation.SuppressLint;
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
import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.ihome.base.base.BaseActivity;
import com.ihome.smarthome.R;
import com.ihome.smarthome.databinding.ActivityLogin1Binding;
import com.ihome.smarthome.module.base.communicate.ICommunicate;
import com.ihome.smarthome.module.base.communicate.MyBluetoothManager;
import com.ihome.smarthome.module.base.communicate.MySocketManager;
import com.ihome.smarthome.utils.EventBusUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * A login screen that offers login via username.
 */
public class LoginActivity extends BaseActivity implements ICommunicate.onMessageLisenter {
    private User user = new User("xiongbin", "123456");
    private ICommunicate communicateDevice;
    //private String communicateMode = SPUtils.getInstance().getString(Constants.COMMUNICATE_MODE);
    public static boolean isStart = false;
    private String communicateMode = Constants.BLUETOOTH_MODE;
    int PERMISSION_REQUEST_COARSE_LOCATION = 1001;
    private FloatingToolbar mFloatingToolbar;
    ActivityLogin1Binding binding;
    HomeKeyEventBroadCastReceiver homeReceiver;

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
               // ActivityManager.startActivity(BoilerActivity.class);
                EventBusUtils.sendDeBugLog("debugjuhkjkjkll");
                EventBusUtils.sendFailLog("failed");
                EventBusUtils.sendSucessLog("success;ll;lkjklkljkj");
            }
        });

        initCommunicateDevice(communicateMode);

        // 监听home键广播
        homeReceiver = new HomeKeyEventBroadCastReceiver();
        registerReceiver(homeReceiver, new IntentFilter(
                Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

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
        if (communicateMode.equals(Constants.BLUETOOTH_MODE)) {
            communicateDevice = new MyBluetoothManager(this);
            // ((MyBluetoothManager) communicateDevice).requestDiscoverable();
            ((MyBluetoothManager) communicateDevice).startBluetoothServer();
        } else {
            communicateDevice = new MySocketManager(this);
        }
        communicateDevice.setOnMessageLisenter(this);
    }


    @Override
    public void onMessage(String name, String msg) {
        LogUtils.eTag("xiong", msg);
        ToastUtils.showShort(name + ":" + msg);
    }

    @Override
    public void onConnect(String name, String type) {
        ToastUtils.showShort(name + ":" + type);
    }

    @Override
    public void onDisConnect(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void onError(String err_msg) {
        ToastUtils.showShort(err_msg);
    }


    public void onClickLogin(View view) {
        if (communicateDevice == null || !communicateDevice.isConnected()) {
            ToastUtils.showShort("通信未连接");
            return;
        }
        communicateDevice.sendMessage(user.username);
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

}



