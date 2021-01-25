package com.ihome.smarthome.utils;

import com.ihome.smarthome.module.base.MessageEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/11 19:22
 */

public class EventBusUtils {
    public static void sendDeBugLog(String msg){
        EventBus.getDefault().post(new MessageEvent(MessageEvent.LOG_DEBUG,msg));
    }

    public static void sendFailLog(String msg){
        EventBus.getDefault().post(new MessageEvent(MessageEvent.LOG_FAILED,msg));
    }

    public static void sendSucessLog(String msg){
        EventBus.getDefault().post(new MessageEvent(MessageEvent.LOG_SUCCESS,msg));
    }

    public static void sendMessage(String name,String msg){
        EventBus.getDefault().post(new MessageEvent(name,msg));
    }

}
