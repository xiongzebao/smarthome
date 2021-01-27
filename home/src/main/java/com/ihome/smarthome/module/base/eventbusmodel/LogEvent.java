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

    private int type;
    private String message;

    public LogEvent(int type, String message) {
        this.type = type;
        this.message = message;
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
