package com.ihome.smarthome.database.showlog;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/14 9:30
 */

@Entity
public class ShowLog {
    @Id(autoincrement = true)//设置自增长
    private Long id;

    @Index(unique = true)//设置唯一性
    private String Date;

    private int type;

    private String msg;

    @Generated(hash = 540994890)
    public ShowLog(Long id, String Date, int type, String msg) {
        this.id = id;
        this.Date = Date;
        this.type = type;
        this.msg = msg;
    }

    @Generated(hash = 1903004309)
    public ShowLog() {
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

}
