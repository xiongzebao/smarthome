package com.ihome.smarthome.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import com.ihome.smarthome.module.base.communicate.MyBluetoothManager;
import com.ihome.smarthome.module.base.eventbusmodel.LogEvent;
import com.ihome.smarthome.utils.EventBusUtils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/13 10:27
 */


public class BluetoothMonitorReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action != null){
            switch (action) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_ON:
                            Toast.makeText(context,"蓝牙正在打开",Toast.LENGTH_SHORT).show();
                           // EventBusUtils.sendDeBugLog("蓝牙正在打开");
                            break;
                        case BluetoothAdapter.STATE_ON:
                            Toast.makeText(context,"蓝牙已经打开",Toast.LENGTH_SHORT).show();
                            //EventBusUtils.sendDeBugLog("蓝牙已经打开");
                            MyBluetoothManager.getInstance().connectCurrentBT();
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            Toast.makeText(context,"蓝牙正在关闭",Toast.LENGTH_SHORT).show();
                           // EventBusUtils.sendDeBugLog("蓝牙正在关闭");
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            Toast.makeText(context,"蓝牙已经关闭",Toast.LENGTH_SHORT).show();
                          //  EventBusUtils.sendDeBugLog("蓝牙已经关闭");
                            break;
                    }
                    break;

                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    BluetoothDevice conDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String tip = conDevice.getName()+"蓝牙设备已连接";

                    EventBusUtils.sendLog("bluetooth",tip, LogEvent.LOG_SUCCESS,true);
                    break;

                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    BluetoothDevice conDevice1 = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String tip1 = conDevice1.getName()+"蓝牙设备已断开";
                    EventBusUtils.sendLog("bluetooth",tip1, LogEvent.LOG_SUCCESS,true);
                    MyBluetoothManager.getInstance().disConnect(conDevice1.getName());

                    break;
            }

        }
    }
}