package com.qidian.kuaitui.module.job.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.qidian.base.base.BaseModel;
import com.qidian.kuaitui.BR;

/**
 * @author xiongbin
 * @description:
 * @date : 2020/12/30 17:02
 */

public class AddNewUserModel extends BaseObservable {
    public String name;
    public String phone;
    public String channel;
    public String recruit;


    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);

    }

    @Bindable
    public String getPhone() {
        return phone;
    }
    @Bindable
    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }
    @Bindable
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
        notifyPropertyChanged(BR.channel);
    }
    @Bindable
    public String getRecruit() {
        return recruit;
    }

    public void setRecruit(String recruit) {
        this.recruit = recruit;
        notifyPropertyChanged(BR.recruit);
    }
}
