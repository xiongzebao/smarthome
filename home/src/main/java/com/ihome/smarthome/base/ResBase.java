package com.ihome.smarthome.base;

/**
 * @author xiongbin
 * @description:
 * @date : 2020/12/29 14:46
 */

public class ResBase<T> {
    public int type=1;
    public int errorcode=0;
    public String message;
    public T resultdata;


    @Override
    public String toString() {
        return "ResBase{" +
                "type=" + type +
                ", errorcode=" + errorcode +
                ", message='" + message + '\'' +
                ", resultdata=" + resultdata +
                '}';
    }
}
