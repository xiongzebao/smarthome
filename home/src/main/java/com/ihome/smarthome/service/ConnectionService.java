package com.ihome.smarthome.service;

import android.app.Activity;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.erongdu.wireless.tools.log.MyLog;
import com.ihome.smarthome.module.base.Constants;
import com.ihome.smarthome.module.base.communicate.ICommunicate;
import com.ihome.smarthome.module.base.communicate.MySocketManager;
import com.ihome.smarthome.module.base.eventbusmodel.BTMessageEvent;
import com.ihome.smarthome.module.base.TestUtils;
import com.ihome.smarthome.module.base.communicate.MyBluetoothManager;
import com.ihome.smarthome.module.base.eventbusmodel.BaseMessageEvent;
import com.ihome.smarthome.utils.NoticeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/19 16:31
 */

public class ConnectionService extends Service {

    public final static int START_BLUETOOTH_SERVER  = 1000;
    public final static int START_WIRELESS_SERVER  = 1001;

    private boolean isAlive = false;
    private MyBluetoothManager bluetoothManager;
    private MySocketManager socketManager;


   public MyBluetoothManager getBluetoothManager(){
        return bluetoothManager;
   }


    public class MyBinder extends Binder {
        public ConnectionService getService(){
            return ConnectionService.this;
        }
    }

    private MyBinder mBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Notification notification = NoticeUtils.createForegroundNotification(this,
                "bluetooth_notice_channal",
                "bluetooth service",
                "蓝牙服务",
                "蓝牙服务已开启"
                );
        //将服务置于启动状态 ,NOTIFICATION_ID指的是创建的通知的ID
        startForeground(2, notification);
        EventBus.getDefault().register(this);
        TestUtils.newInstance();

        MySocketManager.getInstance().on(Constants.SERVER_MSG, new ICommunicate.Listener() {
            @Override
            public void onMessage(BaseMessageEvent event) {
                MyLog.e("ConnectService1 SERVER_MSG");
            }
        });

        MySocketManager.getInstance().on(Constants.ACTION, new ICommunicate.Listener() {
            @Override
            public void onMessage(BaseMessageEvent event) {

                MyLog.e("ConnectService2 "+event.event);
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BTMessageEvent event) {
        MyLog.e("BluetoothService :"+event.getName()+":"+event.getMessage());
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent==null){
            return START_STICKY;
        }
        int  command = intent.getIntExtra(Constants.SP_KEY_COMMAND,0);
        switch (command){
            case START_BLUETOOTH_SERVER:
                startBluetoothService();
                break;
            case START_WIRELESS_SERVER:

                break;
        }
        return START_STICKY;
    }


    public void startBluetoothService(){
        if(isAlive){
            return;
        }
        initCommunicateDevice();
        isAlive = true;
    }


    public void startStartWirelessServer(){
       socketManager=MySocketManager.getInstance();
    }

    private void initCommunicateDevice() {
        bluetoothManager = MyBluetoothManager.getInstance();
        bluetoothManager.startBluetoothServer();
    }


    @Override
    public boolean onUnbind(Intent intent) {

        Log.i("test_out","----->onUnbind");
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.i("test_out","----->onRebind");
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        isAlive = false;
        EventBus.getDefault().unregister(this);
        MySocketManager.getInstance().destroy();
        MyBluetoothManager.getInstance().destroy();
       // communicateDevice.destroy();
    }

    public static void startService(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.startForegroundService(new Intent( activity, ConnectionService.class));
        }else{
            activity.startService(new Intent( activity, ConnectionService.class));
        }
    }
}
