package com.ihome.smarthome.module.base;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ActivityManager;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.ihome.base.views.ExpandLayout;
import com.ihome.smarthome.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/11 10:59
 */

public class FloatingService extends Service {

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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

        switch (event.getAction()) {
            case MessageEvent.LOG_DEBUG:
                append(event.getMsg(), Color.BLUE);
                break;
            case MessageEvent.LOG_FAILED:
                append(event.getMsg(), Color.RED);
                break;
            case MessageEvent.LOG_SUCCESS:
                append(event.getMsg(), Color.GREEN);
                break;
        }
        textView.setText(mBuilder);
        //scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        scrollToBottom(scrollView, textView);
        setDragBtnText();
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

    @Override
    public void onCreate() {
        super.onCreate();
        showFloatingWindow();
        EventBus.getDefault().register(this);
        //spanUtils = SpanUtils.with(textView);
        mBuilder = new SpannableStringBuilder();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        rootView.setVisibility(View.VISIBLE);
        return super.onStartCommand(intent, flags, startId);
    }


    private void clear() {
        mBuilder.clear();
        textView.setText(mBuilder);
        dragBtn.setText("");
    }


    @SuppressLint("InflateParams")
    private void showFloatingWindow() {
        LoginActivity.isStart = true;
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
            textView = rootView.findViewById(R.id.tv);
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
                    toggleView();
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
        LoginActivity.isStart = false;
        windowManager.removeViewImmediate(rootView);
    }
}