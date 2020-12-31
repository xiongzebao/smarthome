package com.erongdu.wireless.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/5/17 15:56
 * <p/>
 * Description: 带drawable的EditText
 */
public class EditTextWithDrawable extends EditText {
    /** 左侧图片点击监听 */
    private DrawableLeftListener   mLeftListener;
    /** 顶部图片点击监听 */
    private DrawableTopListener    mTopListener;
    /** 右侧图片点击监听 */
    private DrawableRightListener  mRightListener;
    /** 底部图片点击监听 */
    private DrawableBottomListener mBottomListener;
    // 左侧图片
    final int DRAWABLE_LEFT   = 0;
    // 顶部图片
    final int DRAWABLE_TOP    = 1;
    // 右侧图片
    final int DRAWABLE_RIGHT  = 2;
    // 底部图片
    final int DRAWABLE_BOTTOM = 3;

    public EditTextWithDrawable(Context context) {
        super(context);
    }

    public EditTextWithDrawable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextWithDrawable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public EditTextWithDrawable(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setLeftListener(DrawableLeftListener leftListener) {
        this.mLeftListener = leftListener;
    }

    public void setTopListener(DrawableTopListener topListener) {
        this.mTopListener = topListener;
    }

    public void setRightListener(DrawableRightListener rightListener) {
        this.mRightListener = rightListener;
    }

    public void setBottomListener(DrawableBottomListener bottomListener) {
        this.mBottomListener = bottomListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                // 左侧图片监听
                if (null != mLeftListener) {
                    Drawable drawableLeft = getCompoundDrawables()[DRAWABLE_LEFT];
                    if (null != drawableLeft && event.getRawX() <= (getLeft() + drawableLeft.getBounds().width())) {
                        mLeftListener.onDrawableLeftClick(this);
                        return true;
                    }
                }
                // 顶部图片监听
                if (null != mTopListener) {
                    Drawable drawableTop = getCompoundDrawables()[DRAWABLE_TOP];
                    if (null != drawableTop && event.getRawY() <= (getTop() + drawableTop.getBounds().width())) {
                        mTopListener.onDrawableTopClick(this);
                        return true;
                    }
                }
                // 右侧图片监听
                if (null != mRightListener) {
                    Drawable drawableRight = getCompoundDrawables()[DRAWABLE_RIGHT];
                    if (null != drawableRight && event.getRawX() >= (getRight() - drawableRight.getBounds().width())) {
                        mRightListener.onDrawableRightClick(this);
                        return true;
                    }
                }
                // 底部图片监听
                if (null != mBottomListener) {
                    Drawable drawableBottom = getCompoundDrawables()[DRAWABLE_BOTTOM];
                    if (null != drawableBottom && event.getRawY() >= (getBottom() - drawableBottom.getBounds().width())) {
                        mBottomListener.onDrawableBottomClick(this);
                        return true;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public interface DrawableLeftListener {
        void onDrawableLeftClick(View view);
    }

    public interface DrawableTopListener {
        void onDrawableTopClick(View view);
    }

    public interface DrawableRightListener {
        void onDrawableRightClick(View view);
    }

    public interface DrawableBottomListener {
        void onDrawableBottomClick(View view);
    }
}
