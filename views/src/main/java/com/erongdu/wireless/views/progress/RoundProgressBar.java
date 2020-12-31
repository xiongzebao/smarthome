package com.erongdu.wireless.views.progress;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.databinding.BindingAdapter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.erongdu.wireless.tools.utils.ConverterUtil;
import com.erongdu.wireless.views.R;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/4/12 14:54
 * <p/>
 * Description: 进度的进度条，线程安全的View，可直接在线程中更新进度
 */
public class RoundProgressBar extends View {
    /** 画笔对象的引用 */
    private Paint   paint;
    /** 圆环的默认颜色 */
    private int     defaultColor;
    /** 圆环的进度颜色 */
    private int     progressColor;
    /** 已抢光圆环颜色 */
    private int     overColor;
    /** 已抢光文字颜色 */
    private int     overTextColor;
    /** 是否已抢光 */
    private boolean progressBarIsOver;
    /** 中间进度百分比的字符串的颜色 */
    private int     textColor;
    /** 中间进度百分比的字符串的字体 */
    private float   textSize;
    /** 圆环的宽度 */
    private float   roundWidth;
    /** 最大进度 */
    private int     max;
    /** 当前进度 */
    private float   progress;
    /** 是否显示中间的进度 */
    private boolean textIsDisplayable;
    /** 进度的风格，实心或者空心 */
    private int     style;
    public static final int STROKE = 0;
    public static final int FILL   = 1;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint = new Paint();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar);

        // 获取自定义属性和默认值
        defaultColor = mTypedArray.getColor(R.styleable.ProgressBar_progressBarDefaultColor, Color.RED);
        progressColor = mTypedArray.getColor(R.styleable.ProgressBar_progressBarProgressColor, Color.GREEN);
        overColor = mTypedArray.getColor(R.styleable.ProgressBar_progressBarOverColor, Color.GREEN);
        overTextColor = mTypedArray.getColor(R.styleable.ProgressBar_progressBarOverTextColor, Color.GREEN);
        progressBarIsOver = mTypedArray.getBoolean(R.styleable.ProgressBar_progressBarIsOver, false);
        textColor = mTypedArray.getColor(R.styleable.ProgressBar_progressBarTextColor, Color.GREEN);
        textSize = mTypedArray.getDimension(R.styleable.ProgressBar_progressBarTextSize, 25);
        roundWidth = mTypedArray.getDimension(R.styleable.ProgressBar_progressBarWidth, 4);
        max = mTypedArray.getInteger(R.styleable.ProgressBar_progressBarMax, 100);
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.ProgressBar_displayable, true);
        style = mTypedArray.getInt(R.styleable.ProgressBar_progressBarPaintStyle, 0);
        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // padding
        final int paddingLeft   = getPaddingLeft();
        final int paddingRight  = getPaddingRight();
        final int paddingTop    = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        final int diameter = getWidth() < getHeight() ? getWidth() : getHeight();

        // view的宽度
        final int width = diameter - paddingLeft - paddingRight;
        // view的高度
        final int height = diameter - paddingTop - paddingBottom;

        // 圆心 - x轴坐标
        final float x = paddingLeft + width / 2;
        // 圆心 - y轴坐标
        final float y = paddingTop + height / 2;
        // 外圆的半径
        final int radius = (int) ((Math.min(width, height) - 2 * roundWidth) / 2);

        /**
         * 画最外层的大圆环
         */
        paint.setColor(defaultColor); // 设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); // 设置空心
        paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        paint.setAntiAlias(true);  // 消除锯齿
        canvas.drawCircle(x, y, radius, paint); // 画出圆环

        /**
         * 画进度百分比
         */
        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.FILL_AND_STROKE); // 设置实心
        paint.setTypeface(Typeface.DEFAULT); // 设置字体

        // 测量字体高度，我们需要根据字体的宽度设置在圆环中间
        float textHeight;

        String progressText;
        // 中间的进度百分比，先转换成float在进行除法运算，不然都为0
        // int   percent   = (int) ((progress / (float) max) * 100);
        if (progressBarIsOver) {
            paint.setColor(overTextColor);
            paint.setTextSize(textSize - 5);
            textHeight = textSize - 5;
            progressText = "已抢光";

            // 测量字体宽度，我们需要根据字体的宽度设置在圆环中间
            float textWidth = paint.measureText(progressText);
            // 画出进度百分比
            canvas.drawText(progressText, x - textWidth / 2, y + textHeight / 2, paint);
        } else {
            paint.setColor(textColor);
            paint.setTextSize(textSize);
            textHeight = textSize;
            progressText = Math.abs((int) progress) + "";
            // 测量数字宽度，我们需要根据字体的宽度设置在圆环中间
            float textWidth = paint.measureText(progressText);
            paint.setTextSize(textSize - 15);
            // 测量百分号宽度，我们需要根据字体的宽度设置在圆环中间
            float perWidth = paint.measureText("%");
            // 画出进度百分比
            paint.setTextSize(textSize);
            canvas.drawText(progressText, x - (textWidth + perWidth) / 2, y + textHeight / 2, paint);
            paint.setTextSize(textSize - 15);
            canvas.drawText("%", x + (textWidth - perWidth) / 2, y + textHeight / 2, paint);
        }

        /**
         * 画圆弧 ，画圆环的进度
         */
        // 设置圆环的宽度
        paint.setStrokeWidth(roundWidth);
        // 设置进度的颜色
        if (progressBarIsOver) {
            paint.setColor(overColor);
        } else {
            paint.setColor(progressColor);
        }
        // 用于定义的圆弧的形状和大小的界限
        RectF oval = new RectF(x - radius, y - radius, x + radius, y + radius);

        // 设置进度是实心还是空心
        switch (style) {
            case STROKE: {
                if (progress != 0) {
                    paint.setStyle(Paint.Style.STROKE);
                    // 根据进度画圆弧
                    canvas.drawArc(oval, -90, 360 * progress / max, false, paint);
                }
                break;
            }
            case FILL: {
                if (progress != 0) {
                    paint.setStyle(Paint.Style.FILL_AND_STROKE);
                    // 根据进度画圆弧
                    canvas.drawArc(oval, -90, 360 * progress / max, true, paint);
                }
                break;
            }
        }
    }

    /**
     * 获取进度的最大值
     */
    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     */
    public synchronized float getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     *         百分比
     * @param flag
     *         是否显示动画
     */
    public synchronized void setProgress(float progress, boolean flag) {
        if (!flag) {
            if (Math.abs(progress) > max) {
                return;
            }
            this.progress = progress;
            postInvalidate();
        } else {
            if (progress > max || progress < 0) {
                return;
            }
            final float p = progress;
            // 启动线程执行任务
            new Thread() {
                public void run() {
                    while (RoundProgressBar.this.progress++ < p) {
                        // 发送消息
                        handler.sendEmptyMessage(0x11);
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            postInvalidate();
        }
    };

    public int getCricleColor() {
        return defaultColor;
    }

    public void setCricleColor(int cricleColor) {
        this.defaultColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return progressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.progressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public boolean isProgressBarIsOver() {
        return progressBarIsOver;
    }

    public void setProgressBarIsOver(boolean progressBarIsOver) {
        this.progressBarIsOver = progressBarIsOver;
    }

    @BindingAdapter("progress")
    public static void progress(View view, String progress) {
        ((RoundProgressBar) view).setProgress(ConverterUtil.getFloat(progress), false);
    }
}
