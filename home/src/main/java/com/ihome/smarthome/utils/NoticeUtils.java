package com.ihome.smarthome.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

import com.ihome.smarthome.R;
import com.ihome.smarthome.module.base.HomeActivity;
import com.ihome.smarthome.module.base.NoticeManager;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/20 13:36
 */

public class NoticeUtils {

    public static void notice(Context context){
        Vibrator vibrator = (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
        long[] patter = {1000, 1000, 2000, 50};
        vibrator.vibrate(patter, 0);
        NoticeManager.startNotification("久坐提醒","一个小时提醒一次");



    }

    public static Notification createForegroundNotification(Context context,String notificationChannelId,
                                                      String channelName,
                                                      String contentTitle,
                                                      String contentText
                                                      ) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);



        // Android8.0以上的系统，新建消息通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //通道的重要程度
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId, channelName, importance);
            notificationChannel.setDescription("bluetooth  service is Running");
            //LED灯
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            //震动
            // notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            //  notificationChannel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, notificationChannelId);
        //通知小图标
        builder.setSmallIcon(R.drawable.ic_logo);
        //通知标题
        builder.setContentTitle(contentTitle);
        //通知内容
        builder.setContentText(contentText);
        //设定通知显示的时间
        builder.setWhen(System.currentTimeMillis());
        //设定启动的内容
        Intent activityIntent = new Intent(context, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        //创建通知并返回
        return builder.build();
    }
}
