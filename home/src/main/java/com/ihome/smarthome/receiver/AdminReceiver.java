package com.ihome.smarthome.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.ihome.smarthome.module.base.communicate.MySocketManager;
import com.ihome.smarthome.module.base.eventbusmodel.LogEvent;
import com.ihome.smarthome.service.AlarmService;
import com.ihome.smarthome.utils.EventBusUtils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/21 15:17
 */

public class AdminReceiver extends DeviceAdminReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        MyLog.d("AdminReceiver onReceive");
        ToastUtil.toast("AdminReceiver");
    }

    @Override
    public void onEnabled(@NonNull Context context, @NonNull Intent intent) {
        super.onEnabled(context, intent);
        MyLog.d("onEnabled");
        ToastUtil.toast("onEnabled");
    }

    @Nullable
    @Override
    public CharSequence onDisableRequested(@NonNull Context context, @NonNull Intent intent) {

        MyLog.d("onDisableRequested");
        ToastUtil.toast("onDisableRequested");
        return super.onDisableRequested(context, intent);

    }

    @Override
    public void onDisabled(@NonNull Context context, @NonNull Intent intent) {
        super.onDisabled(context, intent);
        MyLog.d("onDisabled");
        ToastUtil.toast("onDisabled");
    }


}