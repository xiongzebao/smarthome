package com.erongdu.wireless.views.appbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erongdu.wireless.views.MarqueeText;
import com.erongdu.wireless.views.R;

import java.util.LinkedList;


/**
 * Author: Chenming
 * E-mail: cm1@erongdu.com
 * Date: 16/10/21 上午11:58
 * <p/>
 * Description:
 */
@SuppressWarnings("unused")
public class TitleBar extends ViewGroup implements View.OnClickListener {
    /**
     * 标题字体大小
     */
    private int TITLE_SIZE = 18;
    /**
     * 副标题字体大小
     */
    private int SUB_TITLE_SIZE = 12;
    /**
     * 左侧字体大小
     */
    private int ACTION_LEFT_TEXT_SIZE = 15;
    /**
     * 右侧字体大小
     */
    private int ACTION_RIGHT_TEXT_SIZE = 15;
    /**
     * 标题栏高度
     */
    private int TITLE_BAR_HEIGHT = 40;
    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
    /**
     * 左侧内容区域
     */
    private TextView mLeftText;
    /**
     * 右侧内容区域
     */
    private LinearLayout mRightLayout;
    /**
     * 标题内容区域
     */
    private LinearLayout mCenterLayout;
    /**
     * 主标题
     */
    private MarqueeText mCenterText;
    /**
     * 副标题
     */
    private TextView mSubTitleText;
    /**
     * 自定义标题
     */
    private View mCustomCenterView;
    /**
     * 底部分割线
     */
    private View mDividerView;
    /**
     * 是否支持沉浸模式
     */
    private boolean mImmersive;
    /**
     * 屏幕宽度
     */
    private int mScreenWidth;
    /**
     * 状态栏高度
     */
    private int mStatusBarHeight;
    /**
     * 右侧区域内容间隔
     */
    private int mActionPadding;
    /**
     * 右侧区域相对屏幕边界的间隔
     */
    private int mOutPadding;
    /**
     * 标题栏高度
     */
    private int mHeight;
    /**
     * 右侧文字颜色
     */
    private int mRightTextColor;

    public TitleBar(Context context) {
        super(context);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        if (mImmersive) {
            mStatusBarHeight = getStatusBarHeight();
        }
        mActionPadding = (int) context.getResources().getDimension(R.dimen.x8);
        mOutPadding = (int) context.getResources().getDimension(R.dimen.x30);
        mHeight = dip2px(TITLE_BAR_HEIGHT);
        initView(context);
    }

    private void initView(Context context) {
        mLeftText = new TextView(context);
        mCenterLayout = new LinearLayout(context);
        mRightLayout = new LinearLayout(context);
        mDividerView = new View(context);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

        mLeftText.setTextSize(ACTION_LEFT_TEXT_SIZE);
        mLeftText.setSingleLine();
        mLeftText.setGravity(Gravity.CENTER_VERTICAL);
        mLeftText.setPadding(mOutPadding, 0, mOutPadding, 0);

        /** 设置跑马灯效果 */
        mCenterText = new MarqueeText(context);
        mCenterText.setTextSize(TITLE_SIZE);

        mSubTitleText = new TextView(context);
        mSubTitleText.setTextSize(SUB_TITLE_SIZE);
        mSubTitleText.setSingleLine();
        mSubTitleText.setGravity(Gravity.CENTER);
        mSubTitleText.setEllipsize(TextUtils.TruncateAt.END);

        mCenterLayout.addView(mCenterText);
        mCenterLayout.addView(mSubTitleText);
        mCenterLayout.setGravity(Gravity.CENTER);

        mRightLayout.setPadding(mOutPadding, 0, mOutPadding, 0);

        addView(mLeftText, layoutParams);
        addView(mCenterLayout);
        addView(mRightLayout, layoutParams);
        addView(mDividerView, new LayoutParams(LayoutParams.MATCH_PARENT, 1));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setImmersive(true);
        } else {
            setImmersive(false);
        }
//        setImmersive(true);
    }

    /**
     * 设置是否支持沉浸模式
     */
    public void setImmersive(boolean immersive) {
        mImmersive = immersive;
        if (mImmersive) {
            mStatusBarHeight = getStatusBarHeight();
        } else {
            mStatusBarHeight = 0;
        }
    }

    public void setHeight(int height) {
        mHeight = height;
        setMeasuredDimension(getMeasuredWidth(), mHeight);
    }

    public void setLeftImageResource(int resId) {
        mLeftText.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
    }

    public void setLeftImage(Drawable object) {
        if (object == null) {
            mLeftText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else {
            mLeftText.setCompoundDrawablesWithIntrinsicBounds(object, null, null, null);
        }
    }

    public void setLeftClickListener(OnClickListener l) {
        mLeftText.setOnClickListener(l);
    }

    public void setLeftText(CharSequence title) {
        mLeftText.setText(title);
    }

    public void setLeftText(int resId) {
        mLeftText.setText(resId);
    }

    public void setLeftTextSize(float size) {
        mLeftText.setTextSize(size);
    }

    public void setRightTextSize(float size) {
        this.ACTION_RIGHT_TEXT_SIZE = (int) size;
    }

    public void setRightTextColor(int color) {
        this.mRightTextColor = color;
    }

    public void setLeftTextColor(int color) {
        mLeftText.setTextColor(color);
    }

    public void setLeftVisible(boolean visible) {
        mLeftText.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setTitle(CharSequence title) {
        int index = title.toString().indexOf("\n");
        if (index > 0) {
            setTitle(title.subSequence(0, index), title.subSequence(index + 1, title.length()), LinearLayout.VERTICAL);
        } else {
            index = title.toString().indexOf("\t");
            if (index > 0) {
                setTitle(title.subSequence(0, index), "  " + title.subSequence(index + 1, title.length()), LinearLayout.HORIZONTAL);
            } else {
                mCenterText.setText(title);
                mSubTitleText.setVisibility(View.GONE);
            }
        }
    }

    private void setTitle(CharSequence title, CharSequence subTitle, int orientation) {
        mCenterLayout.setOrientation(orientation);
        mCenterText.setText(title);

        mSubTitleText.setText(subTitle);
        mSubTitleText.setVisibility(View.VISIBLE);
    }

    public void setCenterClickListener(OnClickListener l) {
        mCenterLayout.setOnClickListener(l);
    }

    public void setTitle(int resId) {
        setTitle(getResources().getString(resId));
    }

    public void setTitleSize(float titleSize) {
        this.TITLE_SIZE = (int) titleSize;
        mCenterText.setTextSize(titleSize);
    }

    public void setTitleColor(int resId) {
        mCenterText.setTextColor(resId);
    }

    public void setTitleBackground(int resId) {
        mCenterText.setBackgroundResource(resId);
    }

    public void setSubTitleColor(int resId) {
        mSubTitleText.setTextColor(resId);
    }

    public void setSubTitleSize(float subTitleSize) {
        this.SUB_TITLE_SIZE = (int) subTitleSize;
    }

    public void setCustomTitle(View titleView) {
        if (titleView == null) {
            mCenterText.setVisibility(View.VISIBLE);
            if (mCustomCenterView != null) {
                mCenterLayout.removeView(mCustomCenterView);
            }
        } else {
            if (mCustomCenterView != null) {
                mCenterLayout.removeView(mCustomCenterView);
            }
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mCustomCenterView = titleView;
            mCenterLayout.addView(titleView, layoutParams);
            mCenterText.setVisibility(View.GONE);
        }
    }

    public void setDivider(Drawable drawable) {
        mDividerView.setBackgroundDrawable(drawable);
    }

    public void setDividerColor(int color) {
        mDividerView.setBackgroundColor(color);
    }

    public void setDividerHeight(int dividerHeight) {
        mDividerView.getLayoutParams().height = dividerHeight;
    }

    /**
     * Function to set a click listener for Title TextView
     *
     * @param listener the onClickListener
     */
    public void setOnTitleClickListener(OnClickListener listener) {
        mCenterText.setOnClickListener(listener);
    }

    @Override
    public void onClick(View view) {
        final Object tag = view.getTag();
        if (tag instanceof Action) {
            final Action action = (Action) tag;
            action.performAction(view);
        }
    }

    /**
     * 更新右侧按钮字体颜色
     *
     * @param position 按钮下标
     * @param color    字体颜色
     * @param text     文字
     */
    public void UpdateRightText(int position, int color, String text) {
        this.mRightTextColor = color;
        if (position < mRightLayout.getChildCount()) {
            View view = mRightLayout.getChildAt(position);
            if (view instanceof TextView) {
                if (mRightTextColor != 0) {
                    ((TextView) view).setTextColor(getContext().getResources().getColor(mRightTextColor));
                }
                if (null != text && !"".equals(text)) {
                    ((TextView) view).setText(text);
                }
            }
        }
    }

    /**
     * 更新右侧按钮图片
     *
     * @param position 下标
     * @param image    图片
     */
    public void UpdateRightImageView(int position, int image) {
        if (position < mRightLayout.getChildCount()) {
            View view = mRightLayout.getChildAt(position);
            if (view instanceof ImageView) {
                if (mRightTextColor != 0) {
                    ((ImageView) view).setImageResource(image);
                }
            }
        }
    }

    /**
     * does titleBar has action
     */
    public boolean hasAction() {
        return mRightLayout.getChildCount() > 0;
    }

    /**
     * Adds a list of {@link Action}s.
     *
     * @param actionList the actions to add
     */
    public void addActions(ActionList actionList) {
        int actions = actionList.size();
        for (int i = 0; i < actions; i++) {
            addAction(actionList.get(i));
        }
    }

    /**
     * Adds a new {@link Action}.
     *
     * @param action the action to add
     */
    public View addAction(Action action) {
        final int index = mRightLayout.getChildCount();
        return addAction(action, index);
    }

    /**
     * Adds a new {@link Action} at the specified index.
     *
     * @param action the action to add
     * @param index  the position at which to add the action
     */
    public View addAction(Action action, int index) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        View view = inflateAction(action);
        mRightLayout.addView(view, index, params);
        return view;
    }

    /**
     * Removes all action views from this action bar
     */
    public void removeAllActions() {
        mRightLayout.removeAllViews();
    }

    /**
     * Remove a action from the action bar.
     *
     * @param index position of action to remove
     */
    public void removeActionAt(int index) {
        mRightLayout.removeViewAt(index);
    }

    /**
     * Remove a action from the action bar.
     *
     * @param action The action to remove
     */
    public void removeAction(Action action) {
        int childCount = mRightLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mRightLayout.getChildAt(i);
            if (view != null) {
                final Object tag = view.getTag();
                if (tag instanceof Action && tag.equals(action)) {
                    mRightLayout.removeView(view);
                }
            }
        }
    }

    /**
     * Returns the number of actions currently registered with the action bar.
     *
     * @return action count
     */
    public int getActionCount() {
        return mRightLayout.getChildCount();
    }

    /**
     * Inflates a {@link View} with the given {@link Action}.
     *
     * @param action the action to inflate
     * @return a view
     */
    private View inflateAction(Action action) {
        View view;
        if (TextUtils.isEmpty(action.getText())) {
            ImageView img = new ImageView(getContext());
            img.setImageResource(action.getDrawable());
            view = img;
        } else {
            TextView text = new TextView(getContext());
            text.setGravity(Gravity.CENTER);
            text.setText(action.getText());
            text.setTextSize(ACTION_RIGHT_TEXT_SIZE);

            if (mRightTextColor != 0) {
                text.setTextColor(mRightTextColor);
            }
            view = text;
        }

        view.setPadding(mActionPadding, 0, mActionPadding, 0);
        view.setTag(action);
        view.setOnClickListener(this);
        return view;
    }

    public View getViewByAction(Action action) {
        return findViewWithTag(action);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height;
        if (heightMode != MeasureSpec.EXACTLY) {
            height = mHeight + mStatusBarHeight;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec) + mStatusBarHeight;
        }

        measureChild(mLeftText, widthMeasureSpec, heightMeasureSpec);
        measureChild(mRightLayout, widthMeasureSpec, heightMeasureSpec);
        if (mLeftText.getMeasuredWidth() > mRightLayout.getMeasuredWidth()) {
            mCenterLayout.measure(MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mLeftText.getMeasuredWidth(), MeasureSpec.EXACTLY), heightMeasureSpec);
        } else {
            mCenterLayout.measure(MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mRightLayout.getMeasuredWidth(), MeasureSpec.EXACTLY), heightMeasureSpec);
        }
        measureChild(mDividerView, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLeftText.layout(0, mStatusBarHeight, mLeftText.getMeasuredWidth(), mLeftText.getMeasuredHeight() + mStatusBarHeight);
        mRightLayout.layout(mScreenWidth - mRightLayout.getMeasuredWidth(), mStatusBarHeight,
                mScreenWidth, mRightLayout.getMeasuredHeight() + mStatusBarHeight);
        if (mLeftText.getMeasuredWidth() > mRightLayout.getMeasuredWidth()) {
            mCenterLayout.layout(mLeftText.getMeasuredWidth(), mStatusBarHeight,
                    mScreenWidth - mLeftText.getMeasuredWidth(), getMeasuredHeight());
        } else {
            mCenterLayout.layout(mRightLayout.getMeasuredWidth(), mStatusBarHeight,
                    mScreenWidth - mRightLayout.getMeasuredWidth(), getMeasuredHeight());
        }
        mDividerView.layout(0, getMeasuredHeight() - mDividerView.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
    }

    public static int dip2px(int dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 计算状态栏高度高度
     * getStatusBarHeight
     */
    public static int getStatusBarHeight() {
        return getInternalDimensionSize(Resources.getSystem(), STATUS_BAR_HEIGHT_RES_NAME);
    }

    private static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * A {@link LinkedList} that holds a list of {@link Action}s.
     */
    @SuppressWarnings("serial")
    public static class ActionList extends LinkedList<Action> {
    }

    /**
     * Definition of an action that could be performed, along with a icon to
     * show.
     */
    public interface Action {
        String getText();

        int getDrawable();

        void performAction(View view);
    }

    public static abstract class ImageAction implements Action {
        private int mDrawable;

        public ImageAction(int drawable) {
            mDrawable = drawable;
        }

        @Override
        public int getDrawable() {
            return mDrawable;
        }

        @Override
        public String getText() {
            return null;
        }
    }

    public static abstract class TextAction implements Action {
        final private String mText;

        public TextAction(String text) {
            mText = text;
        }

        @Override
        public int getDrawable() {
            return 0;
        }

        @Override
        public String getText() {
            return mText;
        }
    }
}
