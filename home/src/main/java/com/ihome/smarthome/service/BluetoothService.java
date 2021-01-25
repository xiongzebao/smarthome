package com.ihome.smarthome.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.erongdu.wireless.tools.log.MyLog;
import com.ihome.smarthome.module.base.MessageEvent;
import com.ihome.smarthome.module.base.communicate.ICommunicate;
import com.ihome.smarthome.module.base.communicate.MyBluetoothManager;
import com.ihome.smarthome.utils.NoticeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/19 16:31
 */

public class BluetoothService extends Service {

    private boolean isAlive = false;
    private MyBluetoothManager communicateDevice;

   public MyBluetoothManager getCommunicateDevice(){
        return communicateDevice;
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
        EventBus.getDefault().register(this);
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        MyLog.e("BluetoothService :"+event.getName()+":"+event.getMsg());
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


    public void startBluetoothService(){
        if(isAlive){
            return;
        }
        initCommunicateDevice();
        isAlive = true;
    }


    private void initCommunicateDevice() {
        communicateDevice = MyBluetoothManager.Instance(this);
        communicateDevice.startBluetoothServer();
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
       // communicateDevice.destroy();
    }
}
