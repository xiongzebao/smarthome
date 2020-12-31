package com.erongdu.wireless.views.editText;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;

import com.erongdu.wireless.tools.utils.TextUtil;
import com.erongdu.wireless.views.R;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/12/15 17:10
 * <p/>
 * Description:
 */
public class ClearEditText extends AppCompatEditText {
    // 按钮资源
    private final int CLEAR = R.drawable.icon_clear;
    // 动画时长
    protected final int ANIMATOR_TIME = 200;
    // 按钮左右间隔,单位PX
    private int Interval;
    // 清除按钮宽度,单位PX
    private int mWidthClear;
    // 左内边距
    private int mPaddingLeft;
    // 右内边距
    private int mPaddingRight;
    // 右侧多出的空间(间隔 + 按钮宽度)
    private int mExtraWidth;
    // 清除按钮的bitmap
    private Bitmap mBitmapClear;
    // 清除按钮出现动画
    private ValueAnimator mAnimatorVisible;
    // 消失动画
    private ValueAnimator mAnimatorGone;


    // 是否显示的记录
    private boolean isVisible = false;
    // 右边添加其他按钮时使用
    private int mRight = 0;
    private PaintFlagsDrawFilter drawFilter;
    protected InputMethodManager inputMethodManager;


    public ClearEditText(final Context context) {
        super(context);
        init(context);
    }

    public ClearEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClearEditText(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // 获得清除图片的Bitmap对象
        mBitmapClear = createBitmap(CLEAR, context);

        Interval = (int) context.getResources().getDimension(R.dimen.x10);
        mWidthClear = (int) context.getResources().getDimension(R.dimen.x30);
        mPaddingLeft = getPaddingLeft();
        mPaddingRight = getPaddingRight();
        mExtraWidth = Interval + mWidthClear + Interval;
        mAnimatorVisible = ValueAnimator.ofFloat(0f, 1f).setDuration(ANIMATOR_TIME);
        mAnimatorGone = ValueAnimator.ofFloat(1f, 0f).setDuration(ANIMATOR_TIME);
        drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        // 屏蔽EditText的复制、粘贴功能
        setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        });
        setLongClickable(false);
        setTextIsSelectable(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 抗锯齿
        canvas.setDrawFilter(drawFilter);
        if (mAnimatorVisible.isRunning()) {
            float scale = (float) mAnimatorGone.getAnimatedValue();
            drawClear(scale, canvas);
            invalidate();
        } else if (isVisible) {
            drawClear(1f, canvas);
        }

        if (mAnimatorGone.isRunning()) {
            float scale = (float) mAnimatorGone.getAnimatedValue();
            drawClear(scale, canvas);
            invalidate();
        }
    }

    /**
     * 绘制清除按钮的图案
     *
     * @param scale 缩放比例
     */
    protected void drawClear(float scale, Canvas canvas) {
        int right = (int) (getWidth() + getScrollX() - mPaddingRight - Interval - mRight - mWidthClear * (1f - scale) / 2f);
        int left = (int) (getWidth() + getScrollX() - mPaddingRight - Interval - mRight - mWidthClear * (scale + (1f - scale) / 2f));
        int top = (int) ((getHeight() - mWidthClear * scale) / 2);
        int bottom = (int) (top + mWidthClear * scale);
        Rect rect = new Rect(left, top, right, bottom);
        //buyao
        canvas.drawBitmap(mBitmapClear, null, rect, null);
    }

    /**
     * EditText内容变化的监听
     */
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (getInputType() == (InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER) && !TextUtils.isEmpty(text) && text.charAt(0) == '.') {
            if (text.length() == 1) {
                setText("0.");
            } else {
                setText(getText().replace(0, 0, "0."));
            }
            setSelection(getText().length());
        }
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text.length() > 0 && isFocused() && isEditThis()) {
            if (!isVisible) {
                isVisible = true;
                startVisibleAnimator();

            }
        } else {

            if (isVisible) {
                isVisible = false;
                startGoneAnimator();
            }
        }
    }



    /**
     * 焦点变更
     */
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        // 无焦点，则永不显示清除按钮
        if (focused && !TextUtil.isEmpty(getText()) && isEditThis()) {
            if (!isVisible) {
                isVisible = true;
                startVisibleAnimator();
            }
        } else {

            if (isVisible) {
                isVisible = false;
                startGoneAnimator();

            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int additional = 0;
        if (getGravity() == Gravity.CENTER || getGravity() == Gravity.CENTER_HORIZONTAL) {
            additional = mExtraWidth + mRight + mPaddingRight;
        }
        // 设置内边距
        setPadding(mPaddingLeft + additional, getPaddingTop(), mExtraWidth + mRight + mPaddingRight, getPaddingBottom());

        if (!TextUtil.isEmpty(getText()) && isEditThis()) {
            if (!isVisible) {
                isVisible = true;
                startVisibleAnimator();
            }
        } else {
            if (isVisible) {
                isVisible = false;
                startGoneAnimator();
            }
        }
    }

    /**
     * 触控执行的监听
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            boolean touchable = (getWidth() - mPaddingRight - Interval - mRight - mWidthClear < event.getX())
                    && (event.getX() < getWidth() - mPaddingRight - Interval - mRight);
            if (touchable && isVisible) {
                setError(null);
                this.setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 是否在编辑当前view
     */
    public boolean isEditThis() {
        return isKeyboardShown(getRootView()) && inputMethodManager.isActive(this);
    }

    /**
     * 软键盘是否显示
     */
    private boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect rect = new Rect();
        rootView.getWindowVisibleDisplayFrame(rect);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - rect.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }


    /**
     * 开始清除按钮的显示动画
     */
    private void startVisibleAnimator() {
        endAnimator();
        mAnimatorVisible.start();
        invalidate();
    }

    /**
     * 开始清除按钮的消失动画
     */
    private void startGoneAnimator() {
        endAnimator();
        mAnimatorGone.start();
        invalidate();
    }

    /**
     * 结束所有动画
     */
    private void endAnimator() {
        mAnimatorGone.end();
        mAnimatorVisible.end();
    }

    /**
     * 开始晃动动画
     */
    public void startShakeAnimation() {
        if (getAnimation() == null) {
            this.setAnimation(shakeAnimation(4));
        }
        this.startAnimation(getAnimation());
    }

    /**
     * 晃动动画
     *
     * @param counts 0.5秒钟晃动多少下
     */
    private Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(500);
        return translateAnimation;
    }

    /**
     * 给图标染上当前提示文本的颜色并且转出Bitmap
     */
    public Bitmap createBitmap(int resources, Context context) {
        final Drawable drawable = ContextCompat.getDrawable(context, resources);
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable, getCurrentHintTextColor());
        return drawableToBitmap(wrappedDrawable);
    }

    /**
     * drawable转换成bitmap
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    public int dp2px(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    // ----------------以下方法为方便子类继承，只使用ClearEditText就没有用处---------------------------------------------------------------------

    public int getInterval() {
        return Interval;
    }

    public int getWidthClear() {
        return mWidthClear;
    }

    public Bitmap getBitmapClear() {
        return mBitmapClear;
    }

    public int getmPaddingRight() {
        return mPaddingRight;
    }

    public void addRight(int right) {
        mRight += right;
    }

//    public void setOnInputCompeletListener(OnInputCompeletListener onInputCompeletListener) {
//        this.onInputCompeletListener = onInputCompeletListener;
//    }
//
//    OnInputCompeletListener onInputCompeletListener;
//
//    public interface OnInputCompeletListener {
//        void onFocus();
//
//        void noFocus();
//    }
}
