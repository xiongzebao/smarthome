package com.erongdu.wireless.views.pullToZoom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 水流波动控件 更多详解见博客http://blog.csdn.net/zhongkejingwang/article/details/38556891
 *
 * @author chenjing
 */
@SuppressLint("NewApi")
public class WaveView extends LinearLayout {
    /**
     * 是否开启波浪动画
     */
    private boolean isOpen = true;
    private int   mViewWidth;
    private int   mViewHeight;
    /**
     * 水位线
     */
    private float mLevelLine;
    /**
     * 波浪起伏幅度
     */
    private float mWaveHeight = 30;
    /**
     * 波长
     */
    private float mWaveWidth  = 200;
    /**
     * 被隐藏的最左边的波形
     */
    private float mLeftSide;
    /**
     * 记录平移总位移
     */
    private float mMoveLen;
    /**
     * 水波平移速度
     */
    public static final float SPEED = 12f;
    private List<Point> mWhitePointsList;
    private List<Point> mGrayPointsList;
    private Paint       mWhitePaint;
    private Paint       mGrayPaint;
    private Path        mWhiteWavePath;
    private Path        mGrayWavePath;
    private boolean isMeasured = false;
    private boolean isUpdate   = false;
    private Timer       timer;
    private MyTimerTask mTask;
    private MyHandle updateHandler = new MyHandle();

    /**
     * 所有点的x坐标都还原到初始状态，也就是一个周期前的状态
     */
    private void resetPoints() {
        mLeftSide = -mWaveWidth;
        for (int i = 0; i < mWhitePointsList.size(); i++) {
            mWhitePointsList.get(i).setX(i * mWaveWidth / 4 - mWaveWidth);
            mGrayPointsList.get(i).setX(i * mWaveWidth / 4 - mWaveWidth);
        }
    }

    public WaveView(Context context) {
        super(context);
        init();
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        // 能够重复调用draw方法需要设置的属性
        setWillNotDraw(false);
        mWhitePointsList = new ArrayList<>();
        mGrayPointsList = new ArrayList<>();

        mWhitePaint = new Paint();
        mWhitePaint.setAntiAlias(true);
        mWhitePaint.setStyle(Style.FILL);
        mWhitePaint.setColor(Color.WHITE);

        mGrayPaint = new Paint();
        mGrayPaint.setAntiAlias(true);
        mGrayPaint.setStyle(Style.FILL);
        mGrayPaint.setColor(0x50ffffff);

        mWhiteWavePath = new Path();
        mGrayWavePath = new Path();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        // 开始波动
        hideWaveHeight();
        start();
        stop();
    }

    public void start() {
        if (isOpen) {
            showWaveHeight();
            timer = new Timer();
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
            mTask = new MyTimerTask(updateHandler);
            timer.schedule(mTask, 0, 10);
        }
    }

    public void stop() {
        hideWaveHeight();
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        updateHandler.sendMessage(updateHandler.obtainMessage());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if ((!isMeasured || !isUpdate) && isOpen) {
            isMeasured = true;
            isUpdate = true;
            mViewHeight = getMeasuredHeight();
            mViewWidth = getMeasuredWidth();
            // 根据View宽度计算波形峰值
            mWaveHeight = mViewWidth / 18f;
            // 水位线从最底下开始上升
            mLevelLine = mViewHeight + (mWaveHeight / 2) + 10;
            //Logger.i("WaveView", "mLevelLine" + mLevelLine);
            // 波长等于1.3倍View宽度也就是View中只能看到四分之一个波形，这样可以使起伏更明显
            mWaveWidth = mViewWidth * 1.3f;
            // 左边隐藏的距离预留一个波形
            mLeftSide = -mWaveWidth;
            // 这里计算在可见的View宽度中能容纳几个波形，注意n上取整
            int n = (int) Math.round(mViewWidth / mWaveWidth + 0.5);
            // n个波形需要4n+1个点，但是我们要预留一个波形在左边隐藏区域，所以需要4n+5个点
            if (mWhitePointsList.size() <= 0) {
                for (int i = 0; i < (4 * n + 5); i++) {
                    // 从P0开始初始化到P4n+4，总共4n+5个点
                    float xWhite = i * mWaveWidth / 4 - mWaveWidth;
                    float xGray  = i * mWaveWidth / 4 - mWaveWidth;
                    float yWhite = 0;
                    float yGray  = 0;
                    float rand   = Math.round(1) * 10;
                    switch (i % 4) {
                        case 0:
                        case 2:
                            // 零点位于水位线上
                            yWhite = mLevelLine;
                            yGray = mLevelLine;
                            break;
                        case 1:
                            // 往下波动的控制点
                            yWhite = mLevelLine + mWaveHeight + rand;
                            xWhite += rand;
                            yGray = mLevelLine - mWaveHeight + rand;
                            xGray -= rand;
                            break;
                        case 3:
                            // 往上波动的控制点
                            yWhite = mLevelLine - mWaveHeight + rand;
                            xWhite -= rand;
                            yGray = mLevelLine + mWaveHeight + rand;
                            xGray += rand;
                            break;
                    }
                    mWhitePointsList.add(new Point(xWhite, yWhite));
                    mGrayPointsList.add(new Point(xGray, yGray));
                }
            }
        }
    }

    /**
     * 绘画白色波浪线
     */
    private void onDrawWhite(Canvas canvas) {
        mWhiteWavePath.reset();
        int i = 0;
        mWhiteWavePath.moveTo(mWhitePointsList.get(0).getX(), mWhitePointsList.get(0).getY());
        for (; i < mWhitePointsList.size() - 2; i = i + 2) {
            mWhiteWavePath.quadTo(mWhitePointsList.get(i + 1).getX(), mWhitePointsList.get(i + 1).getY(),
                    mWhitePointsList.get(i + 2).getX(), mWhitePointsList.get(i + 2).getY());
        }
        mWhiteWavePath.lineTo(mWhitePointsList.get(i).getX(), mViewHeight);
        mWhiteWavePath.lineTo(mLeftSide, mViewHeight);
        mWhiteWavePath.close();

        // mWhitePaint的Style是FILL，会填充整个Path区域
        canvas.drawPath(mWhiteWavePath, mWhitePaint);
    }

    /**
     * 绘画灰色波浪线
     */
    private void onDrawGray(Canvas canvas) {
        mGrayWavePath.reset();
        int i = 0;
        mGrayWavePath.moveTo(mGrayPointsList.get(0).getX(), mGrayPointsList.get(0).getY());
        for (; i < mWhitePointsList.size() - 2; i = i + 2) {
            mGrayWavePath.quadTo(mGrayPointsList.get(i + 1).getX(), mGrayPointsList.get(i + 1).getY(),
                    mGrayPointsList.get(i + 2).getX(), mGrayPointsList.get(i + 2).getY());
        }
        mGrayWavePath.lineTo(mGrayPointsList.get(i).getX(), mViewHeight);
        mGrayWavePath.lineTo(mLeftSide, mViewHeight);
        mGrayWavePath.close();

        // mGrayPaint的Style是FILL，会填充整个Path区域
        canvas.drawPath(mGrayWavePath, mGrayPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isOpen) {
            onDrawGray(canvas);
            onDrawWhite(canvas);
        }
    }

    class MyTimerTask extends TimerTask {
        Handler handler;

        public MyTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }
    }

    class Point {
        private float x;
        private float y;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    /**
     * 显示波纹
     */
    public void showWaveHeight() {
        mLevelLine = mViewHeight - (mWaveHeight / 2) - 10;
    }

    /**
     * 隐藏波纹
     */
    public void hideWaveHeight() {
        mLevelLine = mViewHeight + (mWaveHeight / 2) + 10;
    }

    /**
     * 修改波浪所在位置
     */
    public void updateWaveLine(int mHeaderHeight) {
        mViewHeight = mHeaderHeight;
        mLevelLine = mViewHeight - (mWaveHeight / 2) - 10;
        // System.out.println("mLevelLine" + mLevelLine);
    }

    private class MyHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 记录平移总位移
            mMoveLen += SPEED;
            // 水位上升
            //			mLevelLine -= 0.1f;
            if (mLevelLine < 0)
                mLevelLine = 0;
            mLeftSide += SPEED;
            float  rand = 1;
            double one  = -1;
            // 波形平移
            for (int i = 0; i < mWhitePointsList.size(); i++) {
                one = Math.pow(one, (double) i);
                rand = Math.round(one) * 10;
                mWhitePointsList.get(i).setX(mWhitePointsList.get(i).getX() + SPEED);
                mGrayPointsList.get(i).setX(mGrayPointsList.get(i).getX() + SPEED);
                switch (i % 4) {
                    case 0:
                    case 2:
                        mWhitePointsList.get(i).setY(mLevelLine);
                        mGrayPointsList.get(i).setY(mLevelLine);
                        break;
                    case 1:
                        mWhitePointsList.get(i).setY(mLevelLine + mWaveHeight + rand);
                        mGrayPointsList.get(i).setY(mLevelLine - mWaveHeight + rand);
                        break;
                    case 3:
                        mWhitePointsList.get(i).setY(mLevelLine - mWaveHeight + rand);
                        mGrayPointsList.get(i).setY(mLevelLine + mWaveHeight + rand);
                        break;
                }
            }
            if (mMoveLen >= mWaveWidth) {
                // 波形平移超过一个完整波形后复位
                mMoveLen = 0;
                resetPoints();
            }
            invalidate();
        }
    }
}
