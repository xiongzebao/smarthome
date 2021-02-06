package com.ihome.smarthome.database.showlog;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/14 9:30
 */

@Entity
public class ShowLogEntity {
    @Id(autoincrement = true)//设置自增长
    private Long id;
    private String Date;
    private Integer type;
    private String msg;
    private String event;
    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public ShowLogEntity(String tag,String date, Integer type, String msg) {
        Date = date;
        this.type = type;
        this.msg = msg;
        this.tag = tag;
    }

    public ShowLogEntity(String tag,String date, Integer type, String msg, String event) {
        Date = date;
        this.type = type;
        this.msg = msg;
        this.event = event;
        this.tag = tag;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Generated(hash = 1299184614)
    public ShowLogEntity() {
    }

    public ShowLogEntity(Long id, String date, int type, String msg) {
        this.id = id;
        Date = date;
        this.type = type;
        this.msg = msg;
    }

    @Generated(hash = 350178163)
    public ShowLogEntity(Long id, String Date, Integer type, String msg, String event,
            String tag) {
        this.id = id;
        this.Date = Date;
        this.type = type;
        this.msg = msg;
        this.event = event;
        this.tag = tag;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return this.Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
