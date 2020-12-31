package com.qidian.kuaitui.module.mine.model;

/**
 * @author xiongbin
 * @description:
 * @date : 2020/12/29 9:58
 */

public class LoginBean {


    /**
     * Success : true
     * Token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ3YW5ndGVuZyIsImlhdCI6MTYwOTIwNjgzOC4wLCJleHAiOjE2MDkyMTQwMzguMCwiYXVkIjoiaHR0cDovL2V4YW1wbGUuY29tIiwic3ViIjoiSG9tZUNhcmUuVklQIiwianRpIjoiMjAyMDEyMjkwOTUzNTMzNTAiLCJVc2VySWQiOiJ6aGFuZ3NhbiIsIlVzZXJOYW1lIjoi5byg5LiJIiwiVXNlclB3ZCI6IjEyMzQ1NiIsIlVzZXJQaG9uZSI6IjE1MzA3MTY5MTAwIiwiU3RhZmZJZCI6ImFmNjYwNDk3LTIyZDQtNDc1YS04MDNhLTFmNjI3YWY5NTc5OSJ9.BQlrFs2NwPr2IdXCnXYw1QuxYZoLrVsgjroGaaqthY4
     * Message : 登入成功
     * staffName : 张三
     * staffPhone : 15307169100
     */

    private boolean Success;
    private String Token;
    private String Message;
    private String staffName;
    private String staffPhone;

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffPhone() {
        return staffPhone;
    }

    public void setStaffPhone(String staffPhone) {
        this.staffPhone = staffPhone;
    }

    @Override
    public String toString() {
        return "LoginModel{" +
                "Success=" + Success +
                ", Token='" + Token + '\'' +
                ", Message='" + Message + '\'' +
                ", staffName='" + staffName + '\'' +
                ", staffPhone='" + staffPhone + '\'' +
                '}';
    }
}
