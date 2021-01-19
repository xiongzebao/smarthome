package com.ihome.smarthome.module.base;

import com.blankj.utilcode.util.TimeUtils;

import java.util.Date;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/15 10:46
 */

public class Event {
    final public static  String ON="on";
    final public static  String OFF="off";
   public String name;
   public Date date;
   public String type;
   public long time;

   public String getEvent(){

       return name+"："+ TimeUtils.date2String(date);
   }
}
