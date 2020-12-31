package com.erongdu.wireless.views.appbar;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.erongdu.wireless.tools.utils.DensityUtil;
import com.erongdu.wireless.views.R;

/**
 * Author: Chenming
 * E-mail: cm1@erongdu.com
 * Date: 16/10/21 上午11:55
 * <p/>
 * Description:
 */
public class ToolBar extends LinearLayout {
    /**
     * 标题栏
     */
    private TitleBar titleBar;
    /**
     * 背景资源
     */
    int backgroundResId;
    /**
     * 左文本内容
     */
    String leftText;
    /**
     * 左图标资源
     */
    int leftResId;
    /**
     * 左文本大小
     */
    float leftSize;
    /**
     * 左文本压缩呢
     */
    int leftColor;
    /**
     * 标题文本内容
     */
    String titleText;
    /**
     * 标题文本大小
     */
    float titleSize;
    /**
     * 标题文本颜色
     */
    int titleColor;
    /**
     * 副标题文本大小
     */
    float subTitleSize;
    /**
     * 副标题文本颜色
     */
    int subTitleColor;
    /**
     * 右文本大小
     */
    float rightSize;
    /**
     * 右文本颜色
     */
    int rightColor;
    /**
     * 分割线颜色
     */
    int dividerColor;
    /**
     * 分割线高度
     */
    float dividerHeight;

    public ToolBar(Context context) {
        this(context, null);
    }

    public ToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    /**
     * 初始化属性
     */
    public void initView(AttributeSet attrs) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ToolBar);
        /**-------------创建头部内容------------*/
        titleBar = new TitleBar(getContext());
        titleBar.setBackgroundColor(Color.WHITE);
        /**-------------默认头部内容初始化-------*/
//        titleBar.setHeight((int) getContext().getResources().getDimension(R.dimen.app_bar_height));
        titleBar.setLeftImageResource(R.drawable.app_bar_back);
        titleBar.setTitleColor(getContext().getResources().getColor(R.color.app_bar_title_color));
        titleBar.setRightTextColor(getContext().getResources().getColor(R.color.app_bar_right_color));
        titleBar.setLeftTextColor(getContext().getResources().getColor(R.color.app_bar_left_color));
        titleBar.setDividerColor(getContext().getResources().getColor(R.color.app_bar_divider_color));
        titleBar.setDividerHeight((int) DensityUtil.px2dp(getContext(), getContext().getResources().getDimension(R.dimen.app_bar_divider_height)));
        titleBar.setTitleSize(DensityUtil.px2sp(getContext(), getContext().getResources().getDimension(R.dimen.app_bar_title_size)));
        titleBar.setLeftTextSize(DensityUtil.px2sp(getContext(), getContext().getResources().getDimension(R.dimen.app_bar_left_size)));
        titleBar.setRightTextSize(DensityUtil.px2sp(getContext(), getContext().getResources().getDimension(R.dimen.app_bar_right_size)));
        titleBar.setLeftClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity(view).onBackPressed();
                //((Activity) view.getContext()).onBackPressed();
            }


        });



        /**-------------获取左边按钮属性------------*/
        if (typedArray.hasValue(R.styleable.ToolBar_leftSrc)) {
            leftResId = typedArray.getResourceId(R.styleable.ToolBar_leftSrc, -1);
            titleBar.setLeftImageResource(leftResId);
        } else {
            titleBar.setLeftImage(null);
        }
        /**-------------获取左边属性------------*/
        if (typedArray.hasValue(R.styleable.ToolBar_leftText)) {
            leftText = typedArray.getString(R.styleable.ToolBar_leftText);
            titleBar.setLeftText(leftText);
        }
        if (typedArray.hasValue(R.styleable.ToolBar_leftSize)) {
            leftSize = typedArray.getDimensionPixelSize(R.styleable.ToolBar_leftSize, DensityUtil.sp2px(getContext(), 16));
            titleBar.setLeftTextSize(DensityUtil.px2sp(getContext(), leftSize));
        }
        if (typedArray.hasValue(R.styleable.ToolBar_leftColor)) {
            leftColor = typedArray.getColor(R.styleable.ToolBar_leftColor, Color.WHITE);
            titleBar.setLeftTextColor(leftColor);
        }


        /**-------------获取标题属性------------*/
        if (typedArray.hasValue(R.styleable.ToolBar_titleText)) {
            titleText = typedArray.getString(R.styleable.ToolBar_titleText);
            titleBar.setTitle(titleText);
        }
        if (typedArray.hasValue(R.styleable.ToolBar_titleSize)) {
            titleSize = typedArray.getDimensionPixelSize(R.styleable.ToolBar_titleSize, DensityUtil.sp2px(getContext(), 16));
            titleBar.setTitleSize(DensityUtil.px2sp(getContext(), titleSize));
        }
        if (typedArray.hasValue(R.styleable.ToolBar_titleColor)) {
            titleColor = typedArray.getColor(R.styleable.ToolBar_titleColor, Color.WHITE);
            titleBar.setTitleColor(titleColor);
        }
        if (typedArray.hasValue(R.styleable.ToolBar_subTitleSize)) {
            subTitleSize = typedArray.getDimensionPixelSize(R.styleable.ToolBar_subTitleSize, DensityUtil.sp2px(getContext(), 12));
            titleBar.setSubTitleSize(DensityUtil.px2sp(getContext(), subTitleSize));
        }
        if (typedArray.hasValue(R.styleable.ToolBar_subTitleColor)) {
            subTitleColor = typedArray.getColor(R.styleable.ToolBar_subTitleColor, Color.WHITE);
            titleBar.setSubTitleColor(subTitleColor);
        }
        /**-------------获取右侧文本属性------------*/
        if (typedArray.hasValue(R.styleable.ToolBar_rightColor)) {
            rightColor = typedArray.getColor(R.styleable.ToolBar_rightColor, Color.WHITE);
            titleBar.setRightTextColor(rightColor);
        }
        if (typedArray.hasValue(R.styleable.ToolBar_rightSize)) {
            rightSize = typedArray.getDimensionPixelSize(R.styleable.ToolBar_rightSize, DensityUtil.sp2px(getContext(), 12));
            titleBar.setRightTextSize(DensityUtil.px2sp(getContext(), rightSize));
        }
        /**-------------获取divider颜色------------*/
        if (typedArray.hasValue(R.styleable.ToolBar_bottomDivider)) {
            dividerColor = typedArray.getColor(R.styleable.ToolBar_bottomDivider, Color.GRAY);
            titleBar.setDividerColor(dividerColor);
        }
        if (typedArray.hasValue(R.styleable.ToolBar_bottomDividerHeight)) {
            dividerHeight = typedArray.getDimensionPixelSize(R.styleable.ToolBar_bottomDividerHeight, DensityUtil.sp2px(getContext(), 1));
            titleBar.setDividerHeight((int) DensityUtil.px2sp(getContext(), dividerHeight));
        }
        /**-------------背景颜色------------*/
        if (typedArray.hasValue(R.styleable.ToolBar_barBackground)) {
            backgroundResId = typedArray.getResourceId(R.styleable.ToolBar_barBackground, -1);
        } else {
            backgroundResId = R.color.colorTitleBar;
        }
        titleBar.setBackgroundResource(backgroundResId);
        typedArray.recycle();

        //将设置完成之后的View添加到此LinearLayout中
        addView(titleBar, 0);
    }



    /**
     * -------------添加设置标题---------
     */
    public void setTitle(String title) {
        if (titleBar != null) {
            titleBar.setTitle(title);
        }
    }

    /**
     * -------------添加设置标题---------
     */
    public void setTitle(@StringRes int title) {
        if (titleBar != null) {
            titleBar.setTitle(getContext().getString(title));
        }
    }

    /**
     * -------------添加右侧操作按钮列表---------
     */
    public boolean hasAction() {
        return titleBar != null && titleBar.hasAction();
    }

    /**
     * -------------添加右侧操作按钮列表---------
     */
    public void addActions(TitleBar.ActionList actions) {
        if (titleBar != null) {
            titleBar.addActions(actions);
        }
    }

    /**
     * -------------添加右侧操作按钮-------------
     */
    public void addAction(TitleBar.Action action) {
        if (titleBar != null && action != null) {
            titleBar.addAction(action);
        }
    }

    /**
     * -------------添加右侧操作按钮-------------
     */
    public void removeAction(TitleBar.Action action) {
        if (titleBar != null && action != null) {
            titleBar.removeAction(action);
        }
    }

    /** -------------设置右侧操作按钮颜色---------- */
    /**
     * 设置右侧字体颜色
     *
     * @param position 下标
     * @param color    颜色值
     */
    public void setRightTextAction(int position, int color) {
        if (titleBar != null) {
            setRightTextAction(position, color, "");
        }
    }

    /**
     * 设置右侧字体颜色
     *
     * @param position 下标
     * @param value    文字内容
     */
    public void setRightTextAction(int position, String value) {
        if (titleBar != null) {
            setRightTextAction(position, 0, value);
        }
    }

    /**
     * 设置右侧字体颜色
     *
     * @param position 下标
     * @param color    颜色值
     * @param value    文字内容
     */
    public void setRightTextAction(int position, int color, String value) {
        if (titleBar != null) {
            titleBar.UpdateRightText(position, color, value);
        }
    }

    /** -------------移除右侧action---------- */
    /**
     * 设置右侧图片
     *
     * @param position 下标
     * @param image    图片
     */
    public void setRightImageAction(int position, int image) {
        if (titleBar != null) {
            titleBar.UpdateRightImageView(position, image);
        }
    }

    /**
     * -------------设置左按钮图片-------------
     */
    public void setLeftImage(Drawable l) {
        if (titleBar != null) {
            titleBar.setLeftImage(l);
        }
    }

    /**
     * -------------添加左按钮操作-------------
     */
    public void setLeftListener(View.OnClickListener l) {
        if (titleBar != null) {
            if (l == null) {
                titleBar.setLeftImage(null);
            }
            titleBar.setLeftClickListener(l);
        }
    }

    /**
     * -------------添加titleBar显示隐藏操作-------------
     */
    public void setTitleBarVisibility(boolean visibility) {
        if (titleBar != null) {
            if (!visibility) {
                titleBar.setVisibility(View.GONE);
            }else {
                titleBar.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * -------------设置titleBar颜色-------------
     */
    public void setTitleBarBackgroundColor(@ColorInt int color) {
        if (titleBar != null) {
            titleBar.setBackgroundColor(color);
        }
        titleBar.setBackgroundColor(color);
    }

    /**
     * 通过view暴力获取getContext()(Android不支持view.getContext()了)
     *
     * @param view
     *         要获取context的view
     *
     * @return 返回一个activity
     */
    /**
     * 通过 View 获取Activity
     */
    public static Activity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return (Activity) view.getRootView().getContext();
    }


    public TitleBar getTitleBar() {
        return titleBar;
    }
}
