package com.ihome.smarthome.module.base.eventbusmodel;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/26 9:58
 */

public class LogEvent extends BaseMessageEvent {
    final public static int LOG_DEBUG=1;
    final public static int LOG_FAILED=2;
    final public static int LOG_SUCCESS=3;

    final public static int LOG_EVENT=4;
    final public static int LOG_IMPORTANT=5;
    final public static int LOG_NOTICE=6;
    private int type;
    private String message;
    private String event;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public LogEvent(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public LogEvent(int type, String message,String event) {
        this.type = type;
        this.message = message;
        this.event = event;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
