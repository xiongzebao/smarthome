package com.erongdu.wireless.views.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/12/16 15:10
 * <p/>
 * Description: 六边形等待控件
 */
public class HexagonProgressView extends View implements Animatable {
    private HexagonProgressDrawable mDrawable;

    public HexagonProgressView(Context context) {
        super(context);
    }

    public HexagonProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HexagonProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        int minLength = Math.min(w, h);
        mDrawable = new HexagonProgressDrawable(Color.GRAY, minLength);
        mDrawable.setCallback(this);
        mDrawable.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDrawable.draw(canvas);
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable drawable) {
        super.invalidateDrawable(drawable);
        invalidate();
    }

    public void setProgressColor(@ColorInt int color) {
        if (mDrawable != null) {
            mDrawable.setColor(color);
        }
    }

    @Override
    public void start() {
        if (mDrawable != null) {
            mDrawable.start();
        }
    }

    @Override
    public void stop() {
        if (mDrawable != null) {
            mDrawable.stop();
        }
    }

    @Override
    public boolean isRunning() {
        return mDrawable != null && mDrawable.isRunning();
    }
}
