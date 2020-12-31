package com.erongdu.wireless.views.progress;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.databinding.BindingAdapter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.erongdu.wireless.tools.utils.ConverterUtil;
import com.erongdu.wireless.views.R;

import java.text.DecimalFormat;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/5/13 16:21
 * <p/>
 * Description: 进度条上显示百分比的ProgressBar
 */
public class PercentProgressBar extends View {
    /** 进度百分比 */
    private float  progress;
    /** 背景色 */
    private int    defaultColor;
    /** 进度条颜色 */
    private int    progressColor;
    /** 字体颜色 */
    private int    textColor;
    /** 字体大小 */
    private float  textSize;
    /** 进度条宽度 */
    private float  strokeWidth;
    /** 百分比背景padding值 */
    private float  textPadding;
    /** 画笔对象的引用 */
    private Paint  paint;
    /** 显示用的百分比值 */
    private String percentText;
    /** 总份数 */
    private float  totalCopy;
    /** 百分比所占份数 */
    private float  percentTextCopy;
    /** 是否显示中间的进度 */
    private boolean displayable;

    public PercentProgressBar(Context context) {
        this(context, null, 0);
    }

    public PercentProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PercentProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar);
        // 获取自定义属性和默认值
        progress = mTypedArray.getFloat(R.styleable.ProgressBar_progressBarProgress, 0f);
        defaultColor = mTypedArray.getColor(R.styleable.ProgressBar_progressBarDefaultColor, Color.parseColor("#E6E6E6"));
        progressColor = mTypedArray.getColor(R.styleable.ProgressBar_progressBarProgressColor, Color.GREEN);
        textColor = mTypedArray.getColor(R.styleable.ProgressBar_progressBarTextColor, Color.WHITE);
        textSize = mTypedArray.getDimensionPixelSize(R.styleable.ProgressBar_progressBarTextSize, 30);
        strokeWidth = mTypedArray.getFloat(R.styleable.ProgressBar_progressBarWidth, 10f);
        displayable = mTypedArray.getBoolean(R.styleable.ProgressBar_displayable, true);
        textPadding = strokeWidth * 2;
        percentText = new DecimalFormat("#.#").format(progress) + "%";
        // 进度条显示效果
        if (progress < 0) {
            progress = 0;
        } else if (progress > 100) {
            progress = 100;
        }
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
        // view的宽度
        final int width = getWidth() - paddingLeft - paddingRight;
        // view的高度
        int height = getHeight() - paddingTop - paddingBottom;
        height = Math.max(height, (int) strokeWidth) / 2;

        // 设置字体大小
        paint.setTextSize(textSize);
        // 设置字体
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        float textBackgroundWidth = paint.measureText(percentText) + 2 * textPadding;
        percentTextCopy = textBackgroundWidth * 100 / (width - textBackgroundWidth);
        totalCopy = 100 + percentTextCopy;

        if(!displayable) {
            totalCopy = 100;
        }

        /** 绘制进度条 */
        drawDefaultProgress(canvas, width, height, paddingLeft, paddingTop);
        drawProgress(canvas, width, height, paddingLeft, paddingTop);

        /** 绘制百分比 */
        if(displayable) {
            drawPercentTextBackground(canvas, width, height, paddingLeft, paddingTop);
            drawPercentText(canvas, width, height, paddingLeft, paddingTop);
        }
    }

    /**
     * 画默认进度条
     */
    private void drawDefaultProgress(Canvas canvas, int width, int height, int paddingLeft, int paddingTop) {
        // 默认进度条颜色
        paint.setColor(defaultColor);
        // 消除锯齿
        paint.setAntiAlias(true);
        float left   = 0f + paddingLeft;
        float right  = width + paddingLeft;
        float top    = height - strokeWidth / 2 + paddingTop;
        float bottom = height + strokeWidth / 2 + paddingTop;
        RectF rectF  = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(rectF, strokeWidth / 2, strokeWidth / 2, paint);
    }

    /**
     * 画进度条
     */
    private void drawProgress(Canvas canvas, int width, int height, int paddingLeft, int paddingTop) {
        // 设置进度条颜色
        paint.setColor(progressColor);
        float left   = 0f + paddingLeft;
        float right  = width * progress / totalCopy + paddingLeft;
        float top    = height - strokeWidth / 2 + paddingTop;
        float bottom = height + strokeWidth / 2 + paddingTop;
        RectF rectF  = new RectF(left, top, right, bottom);
        if(displayable) {
            canvas.drawRect(paddingLeft + strokeWidth / 2, top, right, bottom, paint);
        }
        canvas.drawRoundRect(rectF, strokeWidth / 2, strokeWidth / 2, paint);
    }

    /**
     * 画进度百分比背景
     */
    private void drawPercentTextBackground(Canvas canvas, int width, int height, int paddingLeft, int paddingTop) {
        float left   = width * progress / totalCopy + paddingLeft;
        float right  = width * (progress + percentTextCopy) / totalCopy + paddingLeft;
        float top    = height - textPadding + paddingTop;
        float bottom = height + textPadding + paddingTop;
        // 设置背景颜色
        RectF rectF = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(rectF, textPadding, textPadding, paint);
    }

    /**
     * 画进度百分比
     */
    private void drawPercentText(Canvas canvas, int width, int height, int paddingLeft, int paddingTop) {
        paint.setStrokeWidth(0);
        // 设置字体颜色
        paint.setColor(textColor);
        // 设置水平居中
        paint.setTextAlign(Paint.Align.CENTER);

        final float x = width * (progress + percentTextCopy / 2) / totalCopy + paddingLeft;
        final float y = height + textSize / 2 - 4 + paddingTop;
        canvas.drawText(percentText, x, y, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int desiredWidth  = 500;
        int desiredHeight = 50;

        int widthMode  = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize  = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        // MODE:分为一下三种类别,
        //      AT_MOST：子容器可以是声明大小内的任意大小
        //      EXACTLY:父容器已经为子容器确定的大小，子容器应该遵守
        //      UNSPECIFIED:父容器对子容器没有做任何限制，子容器可以任意大小

        // SIZE:是父容器为子容器提供的大小
        //      当MODE为AT_MOST时，SIZE大小为父容器所能提供的最大值。
        //      当MODE为EXACTLY时，SIZE为父容器提供的限制值。
        //      当MODE为UNSPECIFIED时，大小为0，SIZE完全由子容器的大小决定。

        // Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            // Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            // Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            // Be whatever you want
            width = desiredWidth;
        }

        // Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            // Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            // Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            // Be whatever you want
            height = desiredHeight;
        }

        // MUST CALL THIS ONE
        setMeasuredDimension(width, height);
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        percentText = new DecimalFormat("#.#").format(progress) + "%";
        // 进度条显示效果
        if (progress < 0) {
            this.progress = 0;
        } else if (progress > 100) {
            this.progress = 100;
        }
        postInvalidate();
    }

    @BindingAdapter("progressBarProgress")
    public static void progress(View view, String progress) {
        ((PercentProgressBar) view).setProgress(ConverterUtil.getFloat(progress));
    }

}
