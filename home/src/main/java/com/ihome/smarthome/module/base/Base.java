package com.ihome.smarthome.module.base;

/**
 * @author xiongbin
 * @description:
 * @date : 2020/12/29 14:46
 */

public class Base<T> {
    public int type=1;
    public String message;
    public T result_data;


    @Override
    public String toString() {
        return "ResBase{" +
                "type=" + type +
                ", message='" + message + '\'' +
                ", resultdata=" + result_data +
                '}';
    }
}
