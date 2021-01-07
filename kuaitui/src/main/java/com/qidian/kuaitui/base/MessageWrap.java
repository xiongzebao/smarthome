package com.qidian.kuaitui.base;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/5 10:58
 */

public class MessageWrap {

    public final String message;

    public static MessageWrap getInstance(String message) {
        return new MessageWrap(message);
    }

    private MessageWrap(String message) {
        this.message = message;
    }
}