package com.erongdu.wireless.views.horizontalScroll;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erongdu.wireless.tools.utils.DensityUtil;
import com.erongdu.wireless.views.R;

import java.util.List;

/**
 * Author: TaoHao
 * E-mail: taoh@erongdu.com
 * Date: 2016/11/22 16:40
 * <p/>
 * Description:
 */
public class MyHorizontalScroll extends HorizontalScrollView {
    private Context        mContext;
    /** 滚动条的唯一直系子控件 */
    private LinearLayout   layout;
    /** 一屏幕显示的列数 */
    private int            column;
    /** 文字图片之间的 */
    private int            padding;
    /** 字体颜色 */
    private int            textColor;
    /** 字体大小 */
    private float          textSize;
    /** 子控件的宽度 */
    private int            childWidth;
    /** 数据存储列表 */
    private List<TextBean> list;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public MyHorizontalScroll(Context context) {
        super(context);
    }

    public MyHorizontalScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
    }

    public MyHorizontalScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        this.mContext = context;
        // 获取属性
        TypedArray mTypeArray = context.obtainStyledAttributes(attrs, R.styleable.MyHorizontalScroll);
        column = mTypeArray.getInteger(R.styleable.MyHorizontalScroll_scrollColumn, 1);
        padding = mTypeArray.getDimensionPixelOffset(R.styleable.MyHorizontalScroll_scrollPadding, 0);
        textColor = mTypeArray.getColor(R.styleable.MyHorizontalScroll_scrollTextColor, Color.parseColor("#666666"));
        textSize = mTypeArray.getDimension(R.styleable.MyHorizontalScroll_scrollTextSize, 32);
        textSize = DensityUtil.px2dp(context, textSize);

        layout = new LinearLayout(context);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setFadingEdgeLength(0);

        // 子控件的宽度=屏幕宽度/column
        int screenWidth = getScreenWH(context)[0];
        childWidth = screenWidth / column;
    }

    /**
     * 添加子控件
     */
    private void addChild(TextBean bean, final int position) {
        // 每一个子布局的父类
        LinearLayout childLayout = new LinearLayout(mContext);
        childLayout.setLayoutParams(new LayoutParams(childWidth, LayoutParams.MATCH_PARENT));
        childLayout.setOrientation(LinearLayout.VERTICAL);
        childLayout.setGravity(Gravity.CENTER);
        childLayout.setFadingEdgeLength(0);

        ImageView imageView = new ImageView(mContext);
        if (bean.getResId() != 0) {
            imageView.setImageResource(bean.getResId());
        } else if (bean.getBitmap() != null) {
            imageView.setImageBitmap(bean.getBitmap());
        } else if (bean.getUrl() != null) {
            // 如果使用url网络加载图片对外开发网络接口
        }
        // 添加图片
        childLayout.addView(imageView);

        TextView tv = new TextView(mContext);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(textColor);
        tv.setTextSize(textSize);
        tv.setText(bean.getText());
        tv.setPadding(0, padding, 0, 0);
        // 添加文本
        childLayout.addView(tv);

        // 设置子控件的点击事件
        childLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(position);
                }
            }
        });
        layout.addView(childLayout);
    }

    /**
     * 添加数据
     */
    public void setData(List<TextBean> list) {
        this.list = list;
        for (TextBean bean : list) {
            addChild(bean, list.indexOf(bean));
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 添加最外层子控件
        this.addView(layout);
    }

    /**
     * 屏幕尺寸
     */
    public int[] getScreenWH(Context context) {
        WindowManager  wm         = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display        display    = wm.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        return new int[]{outMetrics.widthPixels, outMetrics.heightPixels};
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }
}
