package com.ihome.smarthome.module.base;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/25 14:22
 */

public class BluetoothDeviceItem  extends DeviceItem{

    private String btName;

    public String getBtName() {
        return btName;
    }

    public void setBtName(String btName) {
        this.btName = btName;
    }

    public BluetoothDeviceItem(String name, int deviceType, String deviceId) {
        super(name, deviceType, deviceId);
    }

    public BluetoothDeviceItem(String name, int deviceType) {
        super(name, deviceType);
    }


}
