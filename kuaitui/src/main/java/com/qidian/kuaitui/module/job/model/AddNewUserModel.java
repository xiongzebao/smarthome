package com.qidian.kuaitui.module.job.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.qidian.kuaitui.BR;

/**
 * @author xiongbin
 * @description:
 * @date : 2020/12/30 17:02
 */

public class AddNewUserModel extends BaseObservable {
    public String MemRealName;
    public String MemCellPhone;
    public String ChannelSource;


    public String ChannelName;

    public String RecruitID;
    public String RecruitName;


    @Bindable
    public String getRecruitName() {
        return RecruitName;
    }

    public void setRecruitName(String recruitName) {
        this.RecruitName = recruitName;
        notifyPropertyChanged(BR.recruitName);

    }


    @Bindable
    public String getChannelName() {
        return ChannelName;
    }

    public void setChannelName(String channelName) {
        ChannelName = channelName;
        notifyPropertyChanged(BR.channelName);
    }

    @Bindable
    public String getMemRealName() {
        return MemRealName;
    }

    public void setMemRealName(String memRealName) {
        this.MemRealName = memRealName;
        notifyPropertyChanged(BR.memRealName);

    }

    @Bindable
    public String getMemCellPhone() {
        return MemCellPhone;
    }



    public void setMemCellPhone(String memCellPhone) {
        this.MemCellPhone = memCellPhone;
        notifyPropertyChanged(BR.memCellPhone);
    }
    @Bindable
    public String getChannelSource() {
        return ChannelSource;
    }

    public void setChannelSource(String channelSource) {
        this.ChannelSource = channelSource;
        notifyPropertyChanged(BR.channelSource);
    }
    @Bindable
    public String getRecruitID() {
        return RecruitID;
    }

    public void setRecruitID(String recruitID) {
        this.RecruitID = recruitID;
        notifyPropertyChanged(BR.recruitID);
    }

    @Override
    public String toString() {
        return "AddNewUserModel{" +
                "name='" + MemRealName + '\'' +
                ", phone='" + MemCellPhone + '\'' +
                ", channel='" + ChannelSource + '\'' +
                ", recruit='" + RecruitID + '\'' +
                '}';
    }
}
