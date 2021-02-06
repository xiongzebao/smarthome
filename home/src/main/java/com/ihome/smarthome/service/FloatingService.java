package com.ihome.smarthome.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import com.blankj.utilcode.util.ScreenUtils;
import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ActivityManager;
import com.ihome.smarthome.R;
import com.ihome.smarthome.database.showlog.DbController;
import com.ihome.smarthome.database.showlog.ShowLogEntity;
import com.ihome.smarthome.module.base.LogActivity;
import com.ihome.smarthome.module.base.eventbusmodel.LogEvent;
import com.ihome.smarthome.module.base.LoginActivity;
import com.ihome.smarthome.utils.EventBusUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/11 10:59
 */

public class FloatingService extends Service {
    final public static int SHOW_FLOATING_SERVICE=1;
    final public static int HIDE_FLOATING_SERVICE=2;
    final public static int CLOSE_FLOATING_SERVICE=3;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private View rootView;
    private TextView textView;
    SpannableStringBuilder mBuilder;
    Handler mHandler = new Handler();
    TextView dragBtn;
    ScrollView scrollView;
    TextView clearBtn;
    TextView closeBtn;
    TextView scaleBtn;

    int minHeight = 200;
    public  static boolean isStart = false;



    public class MyBinder extends Binder {
        public FloatingService getService(){
            return FloatingService.this;
        }
    }

    private FloatingService.MyBinder mBinder = new FloatingService.MyBinder();






    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LogEvent event) {

        switch (event.getType()) {
            case LogEvent.LOG_DEBUG:
                append(event.getMessage(), Color.BLUE);
               // insertShowLog(LogEvent.LOG_DEBUG,event.getMsg());
                break;
            case LogEvent.LOG_FAILED:
                append(event.getMessage(), Color.RED);
               // insertShowLog(LogEvent.LOG_FAILED,event.getMsg());
                break;
            case LogEvent.LOG_SUCCESS:
                append(event.getMessage(), Color.GREEN);
              //  insertShowLog(LogEvent.LOG_SUCCESS,event.getMsg());
                break;
        }
        textView.setText(mBuilder);
        //scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        scrollToBottom(scrollView, textView);
        setDragBtnText();
    }

    private void insertShowLog(int type ,String msg){
        ShowLogEntity showLog = new ShowLogEntity();
        showLog.setDate(new Date().toString());
        showLog.setType(type);
        showLog.setMsg(msg);
        DbController.getInstance(this).insert(showLog);
    }


    public void scrollToBottom(final View scroll, final View inner) {
        mHandler.post(new Runnable() {
            public void run() {
                if (scroll == null || inner == null) {
                    return;
                }
                int offset = inner.getMeasuredHeight() - scroll.getHeight();
                if (offset < 0) {
                    offset = 0;
                }
                scroll.scrollTo(0, offset);
            }
        });
    }


    void append(String text, int color) {
        int start = mBuilder.length();
        mBuilder.append(text);
        mBuilder.append("\n");
        int end = mBuilder.length();
        mBuilder.setSpan(new ForegroundColorSpan(color), start, end, SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private Notification createForegroundNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 唯一的通知通道的id.
        String notificationChannelId = "notification_channel_id_02";

        // Android8.0以上的系统，新建消息通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //用户可见的通道名称
            String channelName = "Foreground Floating Service Notification";
            //通道的重要程度
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId, channelName, importance);
            notificationChannel.setDescription("Channel description");
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

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notificationChannelId);
        //通知小图标
        builder.setSmallIcon(R.drawable.ic_logo);
        //通知标题
        builder.setContentTitle("iHome 悬浮窗服务");
        //通知内容
        builder.setContentText("悬浮窗服务正在后台运行中");
        //设定通知显示的时间
        builder.setWhen(System.currentTimeMillis());
        //设定启动的内容
        Intent activityIntent = new Intent(this, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        //创建通知并返回
        return builder.build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        showFloatingWindow();
        EventBus.getDefault().register(this);
        //spanUtils = SpanUtils.with(textView);
        mBuilder = new SpannableStringBuilder();
        Notification notification = createForegroundNotification();
        //将服务置于启动状态 ,NOTIFICATION_ID指的是创建的通知的ID
        startForeground(2, notification);
        MyLog.e(" FloatingService Create");

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent==null){
              return START_STICKY;
        }
        int command = intent.getIntExtra("command",0);
        switch (command){
            case SHOW_FLOATING_SERVICE:setVisibility(true);break;
            case HIDE_FLOATING_SERVICE:setVisibility(false);break;
            case CLOSE_FLOATING_SERVICE:stopSelf();break;
        }
        return START_STICKY;
    }

    public static void startCommand(Context context,int command){
       Intent intent =  new Intent( context, FloatingService.class);
       intent.putExtra("command",command);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        }else{
            context.startService(intent);
        }
    }


    public void setRootViewVisible(){
        rootView.setVisibility(View.VISIBLE);
    }


    private void clear() {
        mBuilder.clear();
        textView.setText(mBuilder);
        dragBtn.setText("");
        DbController.getInstance(this).deleteAll();
    }


    public   void setVisibility(boolean b){
        if(rootView!=null)
        rootView.setVisibility(b?View.VISIBLE:View.GONE);
    }

    @SuppressLint("InflateParams")
    private void showFloatingWindow() {
        isStart = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
            // 获取WindowManager服务
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            // 设置LayoutParam
            layoutParams = new WindowManager.LayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            layoutParams.format = PixelFormat.RGBA_8888;
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            //宽高自适应
            layoutParams.width = ScreenUtils.getScreenWidth() / 3;
            layoutParams.height = ScreenUtils.getScreenHeight() / 3;
            //显示的位置

            layoutParams.x = 300;
            layoutParams.y = 300;
            // 新建悬浮窗控件
            rootView = LayoutInflater.from(this).inflate(R.layout.float_window, null);
            rootView.setOnTouchListener(new FloatingOnTouchListener());
            textView = rootView.findViewById(R.id.tv_message);
            dragBtn = rootView.findViewById(R.id.dragBtn);
            clearBtn = rootView.findViewById(R.id.clearBtn);
            closeBtn = rootView.findViewById(R.id.closeBtn);
            scaleBtn = rootView.findViewById(R.id.scaleBtn);
            scrollView = (ScrollView) rootView.findViewById(R.id.scrollView);
            // 将悬浮窗控件添加到WindowManager
            windowManager.addView(rootView, layoutParams);

            clearBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clear();
                }
            });
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rootView.setVisibility(View.GONE);
                }
            });

            scaleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  toggleView();
                    scaleWindow();
                }
            });

         /*   dragBtn.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ToastUtil.toast("长按");
                    return false;
                }
            });*/
        }
    }


    private void scaleWindow(){
       WindowManager.LayoutParams params = (WindowManager.LayoutParams) rootView.getLayoutParams();
        params.height=200;
        rootView.setLayoutParams(params);
        rootView.requestLayout();
        rootView.invalidate();

    }

    private void setDragBtnText() {

        dragBtn.setText(mBuilder.length() < 100 ? mBuilder : mBuilder.subSequence(mBuilder.length() - 90,mBuilder.length()));
    }


    private void toggleView() {
        if (scrollView.getVisibility() == View.VISIBLE) {
            scrollView.setVisibility(View.GONE);
            setDragBtnText();
        } else {
            scrollView.setVisibility(View.VISIBLE);
            dragBtn.setText("长按隐藏");
        }
    }

    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;
        private boolean isLongClickModule = false;
        Timer timer = null;

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            isLongClickModule = true;
                            // ToastUtil.toast("true");

                        }
                    }, 1000); // 按下时长设置
                    break;
                case MotionEvent.ACTION_MOVE:
                    double deltaX = Math.sqrt((event.getRawX() - x) * (event.getRawX() - x) + (event.getRawY() - y) * (event.getRawY() - y));
                    if (deltaX > 20 && timer != null) { // 移动大于20像素
                        timer.cancel();
                        timer = null;
                    }
                    if (isLongClickModule) {
                        //添加你长按之后的方法
                        // toggleView();
                        timer = null;
                        ActivityManager.startActivity(LogActivity.class);
                    }
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    // 更新悬浮窗控件布局
                    windowManager.updateViewLayout(view, layoutParams);
                    break;

                default:
                    isLongClickModule = false;
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    break;
            }
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
         isStart = false;
        windowManager.removeViewImmediate(rootView);
        EventBusUtils.sendLog("FloatingService","Floating service onDestroyed!",LogEvent.LOG_IMPORTANT,true);
    }

    public static void startService(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.startForegroundService(new Intent( activity, FloatingService.class));
        }else{
            activity.startService(new Intent( activity, FloatingService.class));
        }
    }
}