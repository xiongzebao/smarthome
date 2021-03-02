package com.ihome.smarthome.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.fence.FenceAlarmPushInfo;
import com.baidu.trace.api.fence.MonitoredAction;
import com.baidu.trace.api.track.LatestPoint;
import com.baidu.trace.api.track.LatestPointResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.LocationMode;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TraceLocation;
import com.erongdu.wireless.tools.log.MyLog;
import com.ihome.base.base.BaseActivity;
import com.ihome.smarthome.R;
import com.ihome.smarthome.base.CurrentLocation;
import com.ihome.smarthome.base.MyApplication;
import com.ihome.smarthome.module.base.communicate.MySocketManager;
import com.ihome.smarthome.module.base.eventbusmodel.LogEvent;
import com.ihome.smarthome.receiver.TrackReceiver;
import com.ihome.smarthome.utils.CommonUtil;
import com.ihome.smarthome.utils.Constants;
import com.ihome.smarthome.utils.EventBusUtils;
import com.ihome.smarthome.utils.MapUtil;
import com.ihome.smarthome.utils.NoticeUtils;
import com.ihome.smarthome.utils.ViewUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 轨迹追踪
 */
public class TracingService extends Service {

    private MyApplication trackApp = null;
    private PowerManager powerManager = null;
    private PowerManager.WakeLock wakeLock = null;
    private TrackReceiver trackReceiver = null;
    private MapUtil mapUtil = null;
    private OnTraceListener traceListener = null;
    private OnTrackListener trackListener = null;
    private OnEntityListener entityListener = null;
    private RealTimeHandler realTimeHandler = new RealTimeHandler();
    private RealTimeLocRunnable realTimeLocRunnable = null;
    private boolean isRealTimeRunning = true;
    public int packInterval = Constants.DEFAULT_PACK_INTERVAL;

    public static void startService(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.startForegroundService(new Intent(activity, TracingService.class));
        } else {
            activity.startService(new Intent(activity, TracingService.class));
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Notification notification = NoticeUtils.createForegroundNotification(this,
                "baidumap_trace_notice_channal",
                "trace service",
                "鹰眼轨迹服务",
                "正在运行中"
        );
        //将服务置于启动状态 ,NOTIFICATION_ID指的是创建的通知的ID
        startForeground(3, notification);
        init();
    }

    private void init() {
        initListener();
        trackApp = (MyApplication) getApplicationContext();
        startRealTimeLoc(Constants.LOC_INTERVAL);
        powerManager = (PowerManager) trackApp.getSystemService(Context.POWER_SERVICE);
        startTrace();
    }

    private void startTrace() {
        if (!trackApp.isTraceStarted) {
            trackApp.mClient.startTrace(trackApp.mTrace, traceListener);
            stopRealTimeLoc();
            startRealTimeLoc(packInterval);
        }
    }


    private void stopTrace() {
        if (trackApp.isTraceStarted) {
            trackApp.mClient.stopTrace(trackApp.mTrace, traceListener);
            stopRealTimeLoc();
        }

    }

    private void stopGather() {
        trackApp.mClient.stopGather(traceListener);
    }

    private void startGather() {
        trackApp.mClient.startGather(traceListener);
    }


    class RealTimeLocRunnable implements Runnable {

        private int interval = 0;

        public RealTimeLocRunnable(int interval) {
            this.interval = interval;
        }

        @Override
        public void run() {
            if (isRealTimeRunning) {
                MyLog.e("RealTimeLocRunnable run");
                trackApp.getCurrentLocation(entityListener, trackListener);
                realTimeHandler.postDelayed(this, interval * 1000);
            }
        }
    }

    public void startRealTimeLoc(int interval) {
        isRealTimeRunning = true;
        realTimeLocRunnable = new RealTimeLocRunnable(interval);
        realTimeHandler.post(realTimeLocRunnable);
    }

    public void stopRealTimeLoc() {
        isRealTimeRunning = false;
        if (null != realTimeHandler && null != realTimeLocRunnable) {
            realTimeHandler.removeCallbacks(realTimeLocRunnable);
        }
        trackApp.mClient.stopRealTimeLoc();
    }


    private void initListener() {

        trackListener = new OnTrackListener() {

            @Override
            public void onLatestPointCallback(LatestPointResponse response) {
                if (StatusCodes.SUCCESS != response.getStatus()) {
                    return;
                }

                LatestPoint point = response.getLatestPoint();
                if (null == point || CommonUtil.isZeroPoint(point.getLocation().getLatitude(), point.getLocation()
                        .getLongitude())) {
                    return;
                }

                LatLng currentLatLng = mapUtil.convertTrace2Map(point.getLocation());
                if (null == currentLatLng) {
                    return;
                }
                CurrentLocation.locTime = point.getLocTime();
                CurrentLocation.latitude = currentLatLng.latitude;
                CurrentLocation.longitude = currentLatLng.longitude;

               /* if (null != mapUtil) {
                    mapUtil.updateStatus(currentLatLng, true);
                }*/
                EventBusUtils.sendLog("trace", String.format("LatestPointResponse, currentLatLng:%s ", currentLatLng.toString()), LogEvent.LOG_FAILED, true);
            }
        };

        entityListener = new OnEntityListener() {

            @Override
            public void onReceiveLocation(TraceLocation location) {
                MyLog.d(String.format("%s ", "onReceiveLocation:"+location.getMessage()));
                if (StatusCodes.SUCCESS != location.getStatus() || CommonUtil.isZeroPoint(location.getLatitude(),
                        location.getLongitude())) {
                    return;
                }
                LatLng currentLatLng = mapUtil.convertTraceLocation2Map(location);
                if (null == currentLatLng) {
                    return;
                }
                CurrentLocation.locTime = CommonUtil.toTimeStamp(location.getTime());
                CurrentLocation.latitude = currentLatLng.latitude;
                CurrentLocation.longitude = currentLatLng.longitude;

                if (null != mapUtil) {
                    mapUtil.updateStatus(currentLatLng, true);
                }
                MyLog.d(String.format("%s ", location.toString()));
            }

        };

        traceListener = new OnTraceListener() {

            /**
             * 绑定服务回调接口
             * @param errorNo  状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功 </pre>
             *                <pre>1：失败</pre>
             */
            @Override
            public void onBindServiceCallback(int errorNo, String message) {
                EventBusUtils.sendLog("trace", String.format("onBindServiceCallback, errorNo:%d, message:%s ", errorNo, message), LogEvent.LOG_FAILED, true);
            }

            /**
             * 开启服务回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功 </pre>
             *                <pre>10000：请求发送失败</pre>
             *                <pre>10001：服务开启失败</pre>
             *                <pre>10002：参数错误</pre>
             *                <pre>10003：网络连接失败</pre>
             *                <pre>10004：网络未开启</pre>
             *                <pre>10005：服务正在开启</pre>
             *                <pre>10006：服务已开启</pre>
             */
            @SuppressLint("DefaultLocale")
            @Override
            public void onStartTraceCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.START_TRACE_NETWORK_CONNECT_FAILED <= errorNo) {
                    trackApp.isTraceStarted = true;
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.putBoolean("is_trace_started", true);
                    editor.apply();
                    registerReceiver();

                }
                startGather();
                EventBusUtils.sendLog("trace", String.format("onStartTraceCallback, errorNo:%d, message:%s ", errorNo, message), LogEvent.LOG_FAILED, true);
            }

            /**
             * 停止服务回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功</pre>
             *                <pre>11000：请求发送失败</pre>
             *                <pre>11001：服务停止失败</pre>
             *                <pre>11002：服务未开启</pre>
             *                <pre>11003：服务正在停止</pre>
             */
            @Override
            public void onStopTraceCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.CACHE_TRACK_NOT_UPLOAD == errorNo) {
                    trackApp.isTraceStarted = false;
                    trackApp.isGatherStarted = false;
                    // 停止成功后，直接移除is_trace_started记录（便于区分用户没有停止服务，直接杀死进程的情况）
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.remove("is_trace_started");
                    editor.remove("is_gather_started");
                    editor.apply();
                    unregisterPowerReceiver();
                }
                EventBusUtils.sendLog("trace", String.format("onStopTraceCallback, errorNo:%d, message:%s ", errorNo, message), LogEvent.LOG_FAILED, true);
            }

            /**
             * 开启采集回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功</pre>
             *                <pre>12000：请求发送失败</pre>
             *                <pre>12001：采集开启失败</pre>
             *                <pre>12002：服务未开启</pre>
             */
            @Override
            public void onStartGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STARTED == errorNo) {
                    trackApp.isGatherStarted = true;
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.putBoolean("is_gather_started", true);
                    editor.apply();

                }
                EventBusUtils.sendLog("trace", String.format("onStartGatherCallback, errorNo:%d, message:%s ", errorNo, message), LogEvent.LOG_FAILED, true);
            }

            /**
             * 停止采集回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功</pre>
             *                <pre>13000：请求发送失败</pre>
             *                <pre>13001：采集停止失败</pre>
             *                <pre>13002：服务未开启</pre>
             */
            @Override
            public void onStopGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STOPPED == errorNo) {
                    trackApp.isGatherStarted = false;
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.remove("is_gather_started");
                    editor.apply();

                }


                EventBusUtils.sendLog("trace", String.format("onStopGatherCallback, errorNo:%d, message:%s ", errorNo, message), LogEvent.LOG_FAILED, true);
            }

            /**
             * 推送消息回调接口
             *
             * @param messageType 状态码
             * @param pushMessage 消息
             *                  <p>
             *                  <pre>0x01：配置下发</pre>
             *                  <pre>0x02：语音消息</pre>
             *                  <pre>0x03：服务端围栏报警消息</pre>
             *                  <pre>0x04：本地围栏报警消息</pre>
             *                  <pre>0x05~0x40：系统预留</pre>
             *                  <pre>0x41~0xFF：开发者自定义</pre>
             */
            @Override
            public void onPushCallback(byte messageType, PushMessage pushMessage) {

                EventBusUtils.sendLog("trace", String.format("onPushCallback, message:%s ", pushMessage.getMessage()), LogEvent.LOG_FAILED, true);
            /*    if (messageType < 0x03 || messageType > 0x04) {
                    viewUtil.showToast(TracingService.this, pushMessage.getMessage());
                    return;
                }
                FenceAlarmPushInfo alarmPushInfo = pushMessage.getFenceAlarmPushInfo();
                if (null == alarmPushInfo) {
                    viewUtil.showToast(TracingService.this,
                            String.format("onPushCallback, messageType:%d, messageContent:%s ", messageType,
                                    pushMessage));
                    return;
                }
                StringBuffer alarmInfo = new StringBuffer();
                alarmInfo.append("您于")
                        .append(CommonUtil.getHMS(alarmPushInfo.getCurrentPoint().getLocTime() * 1000))
                        .append(alarmPushInfo.getMonitoredAction() == MonitoredAction.enter ? "进入" : "离开")
                        .append(messageType == 0x03 ? "云端" : "本地")
                        .append("围栏：").append(alarmPushInfo.getFenceName());

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    Notification notification = new Notification.Builder(trackApp)
                            .setContentTitle("baidu map")
                            .setContentText(alarmInfo.toString())
                            .setSmallIcon(R.mipmap.icon_tracing)
                            .setWhen(System.currentTimeMillis()).build();
                    notificationManager.notify(notifyId++, notification);
                }*/
            }

            @Override
            public void onInitBOSCallback(int errorNo, String message) {

                EventBusUtils.sendLog("trace", String.format("onInitBOSCallback, errorNo:%d, message:%s ", errorNo, message), LogEvent.LOG_FAILED, true);

            }
        };

    }

    static class RealTimeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    /**
     * 注册广播（电源锁、GPS状态）
     */
    private void registerReceiver() {
        if (trackApp.isRegisterReceiver) {
            return;
        }

        if (null == wakeLock) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getString(R.string.pm_tag3));
        }
        if (null == trackReceiver) {
            trackReceiver = new TrackReceiver(wakeLock);
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(StatusCodes.GPS_STATUS_ACTION);
        trackApp.registerReceiver(trackReceiver, filter);
        trackApp.isRegisterReceiver = true;

    }

    private void unregisterPowerReceiver() {
        if (!trackApp.isRegisterReceiver) {
            return;
        }
        if (null != trackReceiver) {
            trackApp.unregisterReceiver(trackReceiver);
        }
        trackApp.isRegisterReceiver = false;
    }


    private boolean isNeedRequestPermissions(List<String> permissions) {
        // 定位精确位置
        addPermission(permissions, Manifest.permission.ACCESS_FINE_LOCATION);
        // 存储权限
        addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // 前台服务权限
            addPermission(permissions, Manifest.permission.FOREGROUND_SERVICE);
        }

        return permissions.size() > 0;
    }

    private void addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRealTimeLoc();
        stopTrace();
        stopGather();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
