package com.ihome.smarthome.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.ihome.smarthome.R;
import com.ihome.smarthome.module.base.LoginActivity;
import com.ihome.smarthome.module.base.communicate.ICommunicate;
import com.ihome.smarthome.module.base.communicate.MyBluetoothManager;
import com.ihome.smarthome.utils.NoticeUtils;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/19 16:31
 */

public class BluetoothService extends Service {

    private boolean isAlive = false;
    private MyBluetoothManager communicateDevice;


    private ICommunicate.onMessageLisenter lisenter;

    public ICommunicate.onMessageLisenter getLisenter() {
        return lisenter;
    }

   public MyBluetoothManager getCommunicateDevice(){
        return communicateDevice;
   }

    public void setLisenter(ICommunicate.onMessageLisenter lisenter) {
        this.lisenter = lisenter;
    }

    public class MyBinder extends Binder {
        public BluetoothService getService(){
            return BluetoothService.this;
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
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    public void startService(ICommunicate.onMessageLisenter lisenter){

        if(isAlive){
            return;
        }
        this.lisenter = lisenter;
        initCommunicateDevice();
        isAlive = true;

    }
    private void initCommunicateDevice() {
        communicateDevice = MyBluetoothManager.Instance(this);
        communicateDevice.startBluetoothServer();
        communicateDevice.setOnMessageLisenter(lisenter);
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
      //  communicateDevice.destroy();
    }
}
