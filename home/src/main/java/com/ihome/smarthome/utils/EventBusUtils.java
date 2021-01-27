package com.ihome.smarthome.utils;

import com.ihome.smarthome.module.base.communicate.MyBluetoothManager;
import com.ihome.smarthome.module.base.eventbusmodel.LogEvent;
import com.ihome.smarthome.module.base.eventbusmodel.BTMessageEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/11 19:22
 */

public class EventBusUtils {
    public static void sendDeBugLog(String msg){
        EventBus.getDefault().post(new LogEvent(LogEvent.LOG_DEBUG,msg));
    }

    public static void sendFailLog(String msg){
        EventBus.getDefault().post(new LogEvent(LogEvent.LOG_FAILED,msg));
    }

    public static void sendSucessLog(String msg){
        EventBus.getDefault().post(new LogEvent(LogEvent.LOG_SUCCESS,msg));
    }


    public static void sendMessageEvent(BTMessageEvent event){
        EventBus.getDefault().post(event);
        MyBluetoothManager.getInstance().callListener(event);
    }

}
