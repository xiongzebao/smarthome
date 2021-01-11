package com.ihome.smarthome.module.base;

import com.blankj.utilcode.util.CacheDiskStaticUtils;
import com.blankj.utilcode.util.GsonUtils;

import java.io.Serializable;


public class MessageEvent implements Serializable {

   final public static int LOG_DEBUG=1;
    final public static int LOG_FAILED=2;
    final public static int LOG_SUCCESS=3;

    private String role= CacheDiskStaticUtils.getString(Constants.SP_USERNAME);
    private boolean success=true;
    private String code;
    private String msg;
    private int action;
    private String recv="pi";

    public MessageEvent( int action) {

        this.action = action;
    }

    public MessageEvent( int action,String msg) {
        this.msg = msg;
        this.action = action;
    }



    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String toJson(){
       return GsonUtils.toJson(this);
    }

}
