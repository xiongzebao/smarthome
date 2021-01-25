package com.ihome.smarthome.module.base;

import com.blankj.utilcode.util.GsonUtils;

import java.io.Serializable;


public class MessageEvent implements Serializable {

   final public static int LOG_DEBUG=1;
    final public static int LOG_FAILED=2;
    final public static int LOG_SUCCESS=3;

    private String name;
    private String msg;
    private int deviceType;


    public MessageEvent( String name,String msg) {
        this.msg = msg;
         this.name = name;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String toJson(){
       return GsonUtils.toJson(this);
    }

}
