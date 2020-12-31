package com.erongdu.wireless.views.progress;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/12/16 15:05
 * <p/>
 * Description: 六边形 Drawable
 */
public class HexagonProgressDrawable extends Drawable implements Animatable {
    // 根号3
    private static final float sqrt_3            = (float) Math.sqrt(3);
    // 动画周期时间
    private static final long  ANIM_TIME         = 1_500;
    // 默认长宽
    private static final float WIDTH_DEFAULT     = 56;
    // 默认颜色
    private static final int   COLOR_DEFAULT     = Color.GRAY;
    // 最大透明度
    private static final int   MAX_ALPHA_DEFAULT = 0xFF;
    // 最小比例
    private static final float MIN_SIZE          = 0.5f;
    // 宽高相等，只保存宽
    private float         mLength;
    // 颜色
    private int           mColor;
    // 间距
    private float         mPadding;
    // 原点X，这里是中心点
    private float         mOriginX;
    // 原点Y，这里是中心点
    private float         mOriginY;
    // 六边形边长
    private float         mHexagonLength;
    private AnimatorSet   mAnimator;
    private Paint         mPaint;
    private List<Hexagon> mHexagons;
    private int           mMaxAlpha;
    private boolean mCancel = false;

    public HexagonProgressDrawable(Context context) {
        final DisplayMetrics metrics       = context.getResources().getDisplayMetrics();
        final float          screenDensity = metrics.density;

        init(COLOR_DEFAULT, WIDTH_DEFAULT * screenDensity);
    }

    public HexagonProgressDrawable(@ColorInt int color, float length) {
        init(color, length);
    }

    private void init(@ColorInt int color, float length) {
        mColor = color;
        mMaxAlpha = MAX_ALPHA_DEFAULT;

        mLength = length;
        mPadding = Math.max(mLength / 3 / 2 / 10, 1f);

        mHexagonLength = (mLength / 3 - mPadding * 2) / sqrt_3;

        mOriginX = mLength / 2;
        mOriginY = mLength / 2;

        mHexagons = new ArrayList<>();
        //  1
        float x = mOriginX - (sqrt_3 * mHexagonLength * 0.5f + mPadding);
        float y = mOriginY - (1.5f * mHexagonLength + sqrt_3 * mPadding);
        mHexagons.add(new Hexagon(mHexagonLength, x, y));
        //  2
        x = mOriginX + (sqrt_3 * mHexagonLength * 0.5f + mPadding);
        y = mOriginY - (1.5f * mHexagonLength + sqrt_3 * mPadding);
        mHexagons.add(new Hexagon(mHexagonLength, x, y));
        //  3
        x = mOriginX + (sqrt_3 * mHexagonLength + 2 * mPadding);
        y = mOriginY;
        mHexagons.add(new Hexagon(mHexagonLength, x, y));
        //  4
        x = mOriginX + (sqrt_3 * mHexagonLength * 0.5f + mPadding);
        y = mOriginY + (1.5f * mHexagonLength + sqrt_3 * mPadding);
        mHexagons.add(new Hexagon(mHexagonLength, x, y));
        //  5
        x = mOriginX - (sqrt_3 * mHexagonLength * 0.5f + mPadding);
        y = mOriginY + (1.5f * mHexagonLength + sqrt_3 * mPadding);
        mHexagons.add(new Hexagon(mHexagonLength, x, y));
        //  6
        x = mOriginX - (sqrt_3 * mHexagonLength + 2 * mPadding);
        y = mOriginY;
        mHexagons.add(new Hexagon(mHexagonLength, x, y));
        //  7
        mHexagons.add(new Hexagon(mHexagonLength, mOriginX, mOriginY));

        // 初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(1f);
        // 连线节点平滑处理
        PathEffect pathEffect = new CornerPathEffect(mHexagonLength / 10);
        mPaint.setPathEffect(pathEffect);

        setupAnimators();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mAnimator != null && mAnimator.isRunning()) {
            for (Hexagon hex : mHexagons) {
                mPaint.setAlpha(hex.getAlpha());
                canvas.drawPath(hex.getPath(), mPaint);
            }
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mMaxAlpha = alpha;
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void start() {
        if (mAnimator != null && !mAnimator.isRunning()) {
            for (Hexagon hexagon : mHexagons) {
                hexagon.setAlpha(0f);
            }
            mCancel = false;
            mAnimator.start();
        }
    }

    @Override
    public void stop() {
        if (mAnimator != null) {
            mCancel = true;
            mAnimator.end();
        }
    }

    public void setColor(@ColorInt int color) {
        if (color != mColor) {
            mPaint.setColor(color);
        }
    }

    @Override
    public boolean isRunning() {
        return mAnimator.isRunning();
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) mLength;
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) mLength;
    }

    private void setupAnimators() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 4f);
        animator.setDuration(ANIM_TIME);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();

                for (int i = 0; i < 7; i++) {
                    if (i * 0.5f < value && value <= (1f + 0.5f * i)) {
                        mHexagons.get(i).setAlpha(value - 0.5f * i);
                        mHexagons.get(i).setLength(value - 0.5f * i);
                        if ((i - 2) >= 0) {
                            mHexagons.get(i - 2).setAlpha(1f);
                        }
                    }
                }

                invalidateSelf();
            }
        });

        ValueAnimator animator_second = ValueAnimator.ofFloat(0f, 4f);
        animator_second.setDuration(ANIM_TIME);
        animator_second.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();

                for (int i = 0; i < 7; i++) {
                    if (i * 0.5f < value && value <= (1f + 0.5f * i)) {
                        mHexagons.get(i).setAlpha(1f - (value - 0.5f * i));
                        mHexagons.get(i).setLength(1f - (value - 0.5f * i));

                        if ((i - 2) >= 0) {
                            mHexagons.get(i - 2).setAlpha(0f);
                        }
                    }
                }

                invalidateSelf();
            }
        });

        mAnimator = new AnimatorSet();
        mAnimator.playSequentially(animator, animator_second);
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!mCancel) {
                    mAnimator.start();
                }
            }
        });
    }

    private class Hexagon {
        //六边形边长
        private float length;
        //中心点
        private float originX;
        private float originY;
        //路径
        private Path  path;
        //透明度
        private int alpha = 0;

        Hexagon(float length, float x, float y) {
            this.length = length;
            originX = x;
            originY = y;

            changePath(this.length);
        }

        void changePath(float length) {
            this.length = length;

            path = new Path();
            path.moveTo(originX, originY - this.length);
            path.lineTo(originX + sqrt_3 * this.length / 2, originY - this.length / 2);
            path.lineTo(originX + sqrt_3 * this.length / 2, originY + this.length / 2);
            path.lineTo(originX, originY + this.length);
            path.lineTo(originX - sqrt_3 * this.length / 2, originY + this.length / 2);
            path.lineTo(originX - sqrt_3 * this.length / 2, originY - this.length / 2);
            path.close();
        }

        public float getLength() {
            return length;
        }

        public float getOriginX() {
            return originX;
        }

        public float getOriginY() {
            return originY;
        }

        public Path getPath() {
            return path;
        }

        public int getAlpha() {
            return alpha;
        }

        public void setAlpha(float v) {
            this.alpha = (int) (mMaxAlpha * v);
        }

        public void setLength(float v) {
            this.length = mHexagonLength * (1 - MIN_SIZE) * v + mHexagonLength * MIN_SIZE;
            changePath(length);
        }
    }
}
