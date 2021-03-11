package com.ihome.smarthome.utils;

import android.content.Context;

import com.erongdu.wireless.tools.log.MyLog;
import com.vise.baseble.ViseBle;
import com.vise.baseble.callback.IConnectCallback;
import com.vise.baseble.callback.scan.IScanCallback;
import com.vise.baseble.callback.scan.ListFilterScanCallback;
import com.vise.baseble.callback.scan.RegularFilterScanCallback;
import com.vise.baseble.callback.scan.ScanCallback;
import com.vise.baseble.callback.scan.SingleFilterScanCallback;
import com.vise.baseble.callback.scan.UuidFilterScanCallback;
import com.vise.baseble.core.DeviceMirror;
import com.vise.baseble.exception.BleException;
import com.vise.baseble.model.BluetoothLeDevice;
import com.vise.baseble.model.BluetoothLeDeviceStore;

import java.util.List;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/3/11 8:57
 */

public class BleManager {

    static boolean isConnectiong=false;
    public static void init(Context context){
        //蓝牙相关配置修改
        ViseBle.config()
                .setScanTimeout(-1)//扫描超时时间，这里设置为永久扫描
                .setConnectTimeout(5000)//连接超时时间
                .setOperateTimeout(5 * 1000)//设置数据操作超时时间
                .setConnectRetryCount(300000)//设置连接失败重试次数
                .setConnectRetryInterval(1000)//设置连接失败重试间隔时间
                .setOperateRetryCount(3)//设置数据操作失败重试次数
                .setOperateRetryInterval(1000)//设置数据操作失败重试间隔时间
                .setMaxConnectCount(3);//设置最大连接设备数量
//蓝牙信息初始化，全局唯一，必须在应用初始化时调用
        ViseBle.getInstance().init(context);
    }


    public static void scanAllDevices(){
        ViseBle.getInstance().startScan(new ScanCallback(new IScanCallback() {
            @Override
            public void onDeviceFound(BluetoothLeDevice bluetoothLeDevice) {
                MyLog.e(bluetoothLeDevice.getName()+":"+bluetoothLeDevice.getAddress());
            }

            @Override
            public void onScanFinish(BluetoothLeDeviceStore bluetoothLeDeviceStore) {
                MyLog.e("onScanFinish");
            }

            @Override
            public void onScanTimeout() {
                MyLog.e("onScanTimeout");
            }
        }));
    }


    public static void scanDeviceByMac(String deviceMac){
        //该方式是扫到指定设备就停止扫描
        ViseBle.getInstance().startScan(new SingleFilterScanCallback(new IScanCallback() {
            @Override
            public void onDeviceFound(BluetoothLeDevice bluetoothLeDevice) {

            }

            @Override
            public void onScanFinish(BluetoothLeDeviceStore bluetoothLeDeviceStore) {

            }

            @Override
            public void onScanTimeout() {

            }
        }).setDeviceMac(deviceMac));
    }


    public static void scanDeviceByName(String deviceName){
        //该方式是扫到指定设备就停止扫描
        ViseBle.getInstance().startScan(new SingleFilterScanCallback(new IScanCallback() {
            @Override
            public void onDeviceFound(BluetoothLeDevice bluetoothLeDevice) {
                    MyLog.e(bluetoothLeDevice.getName());
            }

            @Override
            public void onScanFinish(BluetoothLeDeviceStore bluetoothLeDeviceStore) {
                MyLog.e(bluetoothLeDeviceStore.toString());
            }

            @Override
            public void onScanTimeout() {
                MyLog.e("onScanTimeout");
            }
        }).setDeviceName(deviceName));
    }


    public static void scanDeviceByUUID(String uuid){
        ViseBle.getInstance().startScan(new UuidFilterScanCallback(new IScanCallback() {
            @Override
            public void onDeviceFound(BluetoothLeDevice bluetoothLeDevice) {

            }

            @Override
            public void onScanFinish(BluetoothLeDeviceStore bluetoothLeDeviceStore) {

            }

            @Override
            public void onScanTimeout() {

            }
        }).setUuid(uuid));
    }


    public static void scanDevicesBySet(List deviceMacList,List deviceNameList){
        ViseBle.getInstance().startScan(new ListFilterScanCallback(new IScanCallback() {
            @Override
            public void onDeviceFound(BluetoothLeDevice bluetoothLeDevice) {

            }

            @Override
            public void onScanFinish(BluetoothLeDeviceStore bluetoothLeDeviceStore) {

            }

            @Override
            public void onScanTimeout() {

            }
        }).setDeviceMacList(deviceMacList).setDeviceNameList(deviceNameList));
    }

    public static void scanByRssi(int rssi,String regularDeviceName){
        ViseBle.getInstance().startScan(new RegularFilterScanCallback(new IScanCallback() {
            @Override
            public void onDeviceFound(BluetoothLeDevice bluetoothLeDevice) {

            }

            @Override
            public void onScanFinish(BluetoothLeDeviceStore bluetoothLeDeviceStore) {

            }

            @Override
            public void onScanTimeout() {

            }
        }).setDeviceRssi(rssi).setRegularDeviceName(regularDeviceName));
    }


    public static void connectByDeviceInfo(BluetoothLeDevice bluetoothLeDevice){
        ViseBle.getInstance().connect(bluetoothLeDevice, new IConnectCallback() {
            @Override
            public void onConnectSuccess(DeviceMirror deviceMirror) {

            }

            @Override
            public void onConnectFailure(BleException exception) {

            }

            @Override
            public void onDisconnect(boolean isActive) {

            }
        });
    }

    public static void connectByMac(String deviceMac){
        if(isConnectiong){
            return;
        }
        ViseBle.getInstance().connectByMac(deviceMac, new IConnectCallback() {
            @Override
            public void onConnectSuccess(DeviceMirror deviceMirror) {
                MyLog.e("onConnectSuccess:"+deviceMirror.getBluetoothLeDevice().toString());
            }

            @Override
            public void onConnectFailure(BleException exception) {
                MyLog.e("onConnectFailure:"+exception.toString());
               // connectByMac(deviceMac);

            }

            @Override
            public void onDisconnect(boolean isActive) {
                MyLog.e("onDisconnect:"+isActive);
            }
        });
    }

    public static void connectByName(String deviceName){
        ViseBle.getInstance().connectByName(deviceName, new IConnectCallback() {
            @Override
            public void onConnectSuccess(DeviceMirror deviceMirror) {

            }

            @Override
            public void onConnectFailure(BleException exception) {

            }

            @Override
            public void onDisconnect(boolean isActive) {

            }
        });
    }

}
