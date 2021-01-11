package com.ihome.smarthome.module.base;

import com.blankj.utilcode.util.CacheDiskStaticUtils;

public class Constants {

    public static String COMMUNICATE_MODE="COMMUNICATE_MODE";
    public static String BLUETOOTH_MODE="BLUETOOTH_MODE";
    public static String WIFI_MODE="WIFI_MODE";

    public static String CURRENT_BLUETOOTH="CURRENT_BLUETOOTH";


    public static final String CHAT_SERVER_URL = "http://192.168.8.216:4000";

    public static String EVENT_CONNECTED="on_connected";
    public static String EVENT_MSG="message";


    public static String ACTION_TURN_LEFT = "left";
    public static String ACTION_TURN_RIGHT = "right";
    public static String ACTION_FORWARD = "forward";
    public static String ACTION_BACK = "back";
    public static String ACTION_SPEED_UP = "speedup";
    public static String ACTION_SPEED_DOWN = "speeddown";
    public static String ACTION_TURN_ROUND = "round";
    public static String ACTION_STOP = "stop";


    public static String SP_USERNAME = "sp_username";

    public static String getUserName(){
      return   CacheDiskStaticUtils.getString(SP_USERNAME);
    }

    public static void putUserName(String username){
        CacheDiskStaticUtils.put(SP_USERNAME,username);

    }

}
