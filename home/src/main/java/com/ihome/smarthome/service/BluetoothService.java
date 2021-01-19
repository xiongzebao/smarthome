package com.ihome.smarthome.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/19 16:31
 */

public class BluetoothService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
