package com.ihome.smarthome.module.base.eventbusmodel;

import com.blankj.utilcode.util.GsonUtils;

import java.io.Serializable;


public class BTMessageEvent extends  BaseMessageEvent implements Serializable {
    final public static int MESSAGE_READ = 1;
    final public static  int MESSAGE_FOUND_DEVICE = 2;
    final public static  int ON_CONNECTED_SUCCESS = 3;
    final public static int ON_DISCONNECTED = 4;
    final public static int ON_CONNECT_FAILED = 5;
    final public static int ON_READ_EXCEPTION = 6;
    final public static  int ON_CONNECT_SUCCESS = 7;

    final public static  int REFRESH_NOTICE = 8;

    private String name;
    private String message;
    private String macAddress;
    private Data data;

    private int MessageType=0;

    public int getMessageType() {
        return MessageType;
    }

    public void setMessageType(int messageType) {
        MessageType = messageType;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class Data{

        private int data_type ;
        private int device_type;
        private String json_msg;

       public int getData_type() {
           return data_type;
       }

       public void setData_type(int data_type) {
           this.data_type = data_type;
       }

       public int getDevice_type() {
           return device_type;
       }

       public void setDevice_type(int device_type) {
           this.device_type = device_type;
       }

       public String getJson_msg() {
           return json_msg;
       }

       public void setJson_msg(String json_msg) {
           this.json_msg = json_msg;
       }
   }

    public BTMessageEvent() {
    }

    public BTMessageEvent(int messageType) {
        MessageType = messageType;
    }

    public BTMessageEvent(int messageType,String message, String macAddress) {
        this.message = message;
        MessageType = messageType;
        this.macAddress = macAddress;
    }

    public BTMessageEvent(String name, String macAddress, Data data) {
        this.name = name;
        this.macAddress = macAddress;
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String toJson(){
       return GsonUtils.toJson(this);
    }


}
