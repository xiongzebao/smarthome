package com.ihome.smarthome.utils;

import com.blankj.utilcode.util.TimeUtils;
import com.ihome.smarthome.base.MyApplication;
import com.ihome.smarthome.database.showlog.DbController;
import com.ihome.smarthome.database.showlog.ShowLogEntity;
import com.ihome.smarthome.module.base.communicate.MyBluetoothManager;
import com.ihome.smarthome.module.base.eventbusmodel.LogEvent;
import com.ihome.smarthome.module.base.eventbusmodel.BTMessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/11 19:22
 */

public class EventBusUtils {


    public static void saveToDatabase(String tag,String msg,int type ){
        ShowLogEntity entity = new ShowLogEntity();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss SSS");
        //DateFormat.getDateTimeInstance()
        String date = TimeUtils.getNowString(sdf);
        entity.setDate(date);
        entity.setMsg(msg);
        entity.setType(type);
        entity.setTag(tag);
        DbController.getInstance(MyApplication.application).insert(entity);
    }

    public static void saveToDatabase(String tag,String msg,int type,String event){
        ShowLogEntity entity = new ShowLogEntity();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss SSS");
        //DateFormat.getDateTimeInstance()
        String date = TimeUtils.getNowString(sdf);
        entity.setDate(date);
        entity.setMsg(msg);
        entity.setType(type);
        entity.setEvent(event);
        entity.setTag(tag);
        DbController.getInstance(MyApplication.application).insert(entity);
    }


    public static void sendLog(String tag,String msg,String event, int level, boolean showTime) {
        if (showTime) {
            msg = TimeUtils.getNowString(DateFormat.getTimeInstance())+":"+msg;
        }
        LogEvent logEvent =  new LogEvent( level, msg,event);
        logEvent.setEvent(event);
        EventBus.getDefault().post(logEvent);
        saveToDatabase(tag,msg,level,event);
    }

    public static void sendLog(String tag,String msg , int level, boolean showTime) {
        if (showTime) {
            msg = TimeUtils.getNowString(DateFormat.getTimeInstance())+":"+msg;
        }
        LogEvent logEvent =  new LogEvent(level, msg);
        EventBus.getDefault().post(logEvent);
        saveToDatabase(tag,msg,level);
    }


    public static void sendLogToFloatingWindow(int level,String msg){
        msg = TimeUtils.getNowString(DateFormat.getTimeInstance())+":"+msg;
        LogEvent logEvent =  new LogEvent(level, msg);
        EventBus.getDefault().post(logEvent);
    }

    public static void sendMessageEvent(BTMessageEvent event) {
        EventBus.getDefault().post(event);
        MyBluetoothManager.getInstance().sendMessage(event);
    }

}
