package com.ihome.smarthome.module.base.eventbusmodel;

import java.io.Serializable;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/27 10:20
 */

public class BaseMessageEvent implements Serializable {

    public String event;
    public String eventType;
    public String name;
    public String deviceId;
    public String ownerId;
    public String message;


}
