package com.ihome.smarthome.utils;

import com.blankj.utilcode.util.TimeUtils;
import com.ihome.smarthome.module.base.communicate.MyBluetoothManager;
import com.ihome.smarthome.module.base.eventbusmodel.LogEvent;
import com.ihome.smarthome.module.base.eventbusmodel.BTMessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/11 19:22
 */

public class EventBusUtils {


    public static void sendDeBugLog(String... msg) {
        if(msg.length==1){
            sendLog(msg[0],LogEvent.LOG_DEBUG,false);
            return;
        }
        if(msg.length==2&&msg[1]=="1"){
            sendLog(msg[0],LogEvent.LOG_DEBUG,true);
            return;
        }
    }

    public static void sendFailLog(String... msg) {
        if(msg.length==1){
            sendLog(msg[0],LogEvent.LOG_FAILED,false);
            return;
        }
        if(msg.length==2&&msg[1]=="1"){
            sendLog(msg[0],LogEvent.LOG_FAILED,true);
            return;
        }
    }

    public static void sendSucessLog(String... msg) {
        if(msg.length==1){
            sendLog(msg[0],LogEvent.LOG_SUCCESS,false);
            return;
        }
        if(msg.length==2&&msg[1]=="1"){
            sendLog(msg[0],LogEvent.LOG_SUCCESS,true);
            return;
        }
    }

    private static void sendLog(String msg, int level, boolean showTime) {
        if (showTime) {
            msg = TimeUtils.getNowString(DateFormat.getTimeInstance())+":"+msg;
        }
        EventBus.getDefault().post(new LogEvent(level, msg));
    }


    public static void sendMessageEvent(BTMessageEvent event) {
        EventBus.getDefault().post(event);
        MyBluetoothManager.getInstance().sendMessage(event);
    }

}
