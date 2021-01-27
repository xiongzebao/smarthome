package com.ihome.smarthome.module.base;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/25 11:12
 */

public class DeviceItem implements MultiItemEntity {




    private String Name;
    private int connectStatus;
    private int deviceType;
    private String deviceId;

    private String macAddress;

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public DeviceItem(String name, int deviceType, String deviceId) {
        Name = name;
        this.deviceType = deviceType;
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public DeviceItem(String name, int deviceType) {
        Name = name;
        this.deviceType = deviceType;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


    public int getConnectStatus() {
        return connectStatus;
    }

    public void setConnectStatus(int connectStatus) {
        this.connectStatus = connectStatus;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public int getItemType() {

        return deviceType;
    }
}
