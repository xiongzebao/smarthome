package com.erongdu.wireless.views.recyclerView;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/3/1 11:57
 * <p/>
 * Description: 分割线装饰
 */
public class DividerLine extends RecyclerView.ItemDecoration {
    /** 默认(不显示分割线) - -1 */
    public static final int DEFAULT    = -1;
    /** 水平方向 - 0 */
    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;
    /** 垂直方向 - 1 */
    public static final int VERTICAL   = LinearLayoutManager.VERTICAL;
    /** 全方向 - 9 */
    public static final int CROSS      = 9;
    private Drawable mDivider;
    // private Drawable dividerBackground;
    // 布局方向
    private int      orientation;
    // 起始点间距
    private int marginStart = 0;
    // 结束点间距
    private int marginEnd   = 0;

    public DividerLine() {
        this(DEFAULT);
    }

    public DividerLine(int orientation) {
        this.orientation = orientation;
    }

    @Override
            public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (orientation == VERTICAL) {
            drawVertical(c, parent);
        } else if (orientation == HORIZONTAL) {
            drawHorizontal(c, parent);
        } else if (orientation == CROSS) {
            drawVertical(c, parent);
            drawHorizontal(c, parent);
        } else {
            // TODO
        }
    }

    /**
     * 设置分割线起始点的间距
     *
     * @param marginStart
     *         尺寸
     */
    public void setMarginStart(int marginStart) {
        this.marginStart = marginStart;
    }

    /**
     * 设置分割线结束点的间距
     *
     * @param marginEnd
     *         尺寸
     */
    public void setMarginEnd(int marginEnd) {
        this.marginEnd = marginEnd;
    }

    // 绘制垂直分割线
    private void drawVertical(Canvas c, RecyclerView parent) {
        // 我们通过获取系统属性中的 dividerVertical 来添加，在系统中的AppTheme中设置
        final TypedArray ta = parent.getContext().obtainStyledAttributes(new int[]{android.R.attr.dividerVertical});
        this.mDivider = ta.getDrawable(0);
        ta.recycle();

        final int top    = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            // 获得child的布局信息
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int                       left   = child.getRight() + params.rightMargin;
            final int                       right  = left + mDivider.getIntrinsicWidth();

            mDivider.setBounds(left, top + marginStart, right, bottom - marginEnd);
            mDivider.draw(c);
        }
    }

    // 绘制水平分割线
    private void drawHorizontal(Canvas c, RecyclerView parent) {
        // 我们通过获取系统属性中的 dividerHorizontal 来添加，在系统中的AppTheme中设置
        final TypedArray ta = parent.getContext().obtainStyledAttributes(new int[]{android.R.attr.dividerHorizontal});
        this.mDivider = ta.getDrawable(0);
        ta.recycle();
        // 填充分割线margin或者padding部分的颜色
        // dividerBackground = ContextCompat.getDrawable(parent.getContext(), R.drawable.divider_backgroud);

        final int left  = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            // 获得child的布局信息
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int                       top    = child.getBottom() + params.bottomMargin;
            final int                       bottom = top + mDivider.getIntrinsicHeight();

            // dividerBackground.setBounds(left, top, right, bottom);
            // dividerBackground.draw(c);
            mDivider.setBounds(left + marginStart, top, right - marginEnd, bottom);
            mDivider.draw(c);
        }
    }

    // 由于Divider也有长宽高，每一个Item需要向下或者向右偏移
    //@Override
    //public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    //    super.getItemOffsets(outRect, view, parent, state);
    //    if (null == outRect || null == mDivider || parent.getChildLayoutPosition(view) == RecyclerView.NO_POSITION) {
    //        return;
    //    }
    //    switch (orientation) {
    //        // 画横线，就是往下偏移一个分割线的高度
    //        case HORIZONTAL:
    //            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
    //            break;
    //
    //        // 画竖线，就是往右偏移一个分割线的宽度
    //        case VERTICAL:
    //            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
    //            break;
    //
    //        // 画交叉线，就是往下偏移一个分割线的高度，往右偏移一个分割线的宽度
    //        case CROSS:
    //            break;
    //    }
    //}
}
