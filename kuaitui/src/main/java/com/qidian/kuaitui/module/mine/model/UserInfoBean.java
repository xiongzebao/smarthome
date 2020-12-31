package com.qidian.kuaitui.module.mine.model;

/**
 * @author xiongbin
 * @description:
 * @date : 2020/12/29 15:40
 */

public class UserInfoBean {


    /**
     * UserId : zhangsan
     * UserName : 张三
     * UserPwd : 12345
     * UserPhone : 15307169100
     * StaffId : 123
     */

    private String UserId;
    private String UserName;
    private String UserPwd;
    private String UserPhone;
    private String StaffId;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getUserPwd() {
        return UserPwd;
    }

    public void setUserPwd(String UserPwd) {
        this.UserPwd = UserPwd;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String UserPhone) {
        this.UserPhone = UserPhone;
    }

    public String getStaffId() {
        return StaffId;
    }

    public void setStaffId(String StaffId) {
        this.StaffId = StaffId;
    }
}
