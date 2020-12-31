package com.erongdu.wireless.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/10/26 10:48
 * <p/>
 * Description: 【Item侧滑删除菜单】
 * 继承自ViewGroup，实现滑动出现删除等选项的效果。
 * 思路：跟随手势将item向左滑动， 在onMeasure时 将第一个Item设为屏幕宽度
 * <p/>
 * 【解决屏幕上多个侧滑删除菜单】
 * 内设一个类静态View类型变量 ViewCache，存储的是当前正处于右滑状态的 SwipeMenu Item，
 * 每次Touch时对比，如果两次Touch的不是一个View，那么令ViewCache恢复普通状态，并且设置新的CacheView
 * 只要有一个侧滑菜单处于打开状态， 就不给外层布局上下滑动了
 * <p/>
 * other:
 * 1 菜单处于侧滑时，拦截长按事件
 * 2 解决侧滑时 点击 的冲突；多指一起滑，使用一个类静态布尔变量控制
 * 3 通过 isIos 变量控制是否是IOS阻塞式交互，默认是打开的。
 * 4 通过 isSwipeEnable 变量控制是否开启右滑菜单，默认打开。（某些场景，复用item，没有编辑权限的用户不能右滑）
 * 5 通过开关 isLeftSwipe支持左滑右滑
 * 6 增加 ViewCache 的 get()方法，可以用在：当点击外部空白处时，关闭正在展开的侧滑菜单。
 * 7 仿QQ，侧滑菜单展开时，点击除侧滑菜单之外的区域，关闭侧滑菜单。
 * github: https://github.com/mcxtzhang/SwipeDelMenuLayout
 */
@SuppressWarnings("unused")
public class SwipeMenu extends ViewGroup {
    /** 右滑删除功能的开关,默认开 */
    private boolean isSwipeEnable = true;
    /** 为了处理单击事件的冲突 */
    private int mScaleTouchSlop;
    /** 计算滑动速度用 */
    private int mMaxVelocity;
    /** 多点触摸只算第一根手指的速度 */
    private int mPointerId;
    /** 自己的高度 */
    private int mHeight;
    /** 父控件留给自己的最大的水平空间 */
    private int mMaxWidth;
    /** 右侧菜单宽度总和(最大滑动距离) */
    private int mRightMenuWidths;
    /** 滑动判定临界值（右侧菜单宽度的40%） 手指抬起时，超过了展开，没超过收起menu */
    private int mLimit;
    /** 上一次的xy */
    private PointF  mLastP    = new PointF();
    /**
     * 仿QQ，侧滑菜单展开时，点击除侧滑菜单之外的区域，关闭侧滑菜单。
     * 增加一个布尔值变量，dispatch函数里，每次down时，为true；move时判断，如果是滑动动作，设为false。
     * 在Intercept函数的up时，判断这个变量，如果仍为true 说明是点击事件，则关闭菜单。
     */
    private boolean isUnMoved = true;
    /** 存储的是当前正在展开的View */
    private static SwipeMenu       mViewCache;
    /** 防止多只手指一起滑我的flag 在每次down里判断， touch事件结束清空 */
    private static boolean         isTouching;
    /** 滑动速度变量 */
    private        VelocityTracker mVelocityTracker;
    /** IOS类型的开关 */
    private boolean isIos            = true;
    /** IOS类型下，是否拦截事件的flag */
    private boolean iosInterceptFlag = false;
    /** 左滑 右滑的开关 */
    private boolean isLeftSwipe      = false;

    public SwipeMenu(Context context) {
        this(context, null);
    }

    public SwipeMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScaleTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 最大宽度根据父控件计算出，如果没有父控件用屏幕宽度
        ViewParent parent = getParent();
        if (null != parent && parent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) parent;
            mMaxWidth = viewGroup.getMeasuredWidth() - viewGroup.getPaddingLeft() - viewGroup.getPaddingRight();
        } else {
            mMaxWidth = getResources().getDisplayMetrics().widthPixels;
        }

        // 由于ViewHolder的复用机制，每次这里要手动恢复初始值
        mRightMenuWidths = 0;
        int childCount = getChildCount();

        // 为了子View的高，可以matchParent(参考的FrameLayout 和 LinearLayout 的 Horizontal)
        final boolean measureMatchParentChildren = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
        boolean       isNeedMeasureChildHeight   = false;

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
                mHeight = Math.max(mHeight, childView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                if (measureMatchParentChildren && lp.height == LayoutParams.MATCH_PARENT) {
                    isNeedMeasureChildHeight = true;
                }
                // 第一个布局是Left item，从第二个开始才是 RightMenu
                if (i > 0) {
                    mRightMenuWidths += childView.getMeasuredWidth();
                }
            }
        }
        // 宽度取最大宽度
        setMeasuredDimension(mMaxWidth, mHeight);
        // 滑动判断的临界值
        mLimit = mRightMenuWidths * 4 / 10;
        // 如果子View的height有 MatchParent 属性的，设置子View高度
        if (isNeedMeasureChildHeight) {
            forceUniformHeight(childCount, widthMeasureSpec);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * 给MatchParent的子View设置高度
     *
     * @see android.widget.LinearLayout# 同名方法
     */
    private void forceUniformHeight(int count, int widthMeasureSpec) {
        // Pretend that the linear layout has an exact size. This is the measured height of
        // ourselves. The measured height should be the max height of the children, changed
        // to accommodate the heightMeasureSpec from the parent
        int uniformMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(),
                MeasureSpec.EXACTLY);//以父布局高度构建一个Exactly的测量参数
        for (int i = 0; i < count; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                if (lp.height == LayoutParams.MATCH_PARENT) {
                    // Temporarily force children to reuse their old measured width
                    // FIXME: this may not be right for something like wrapping text?
                    int oldWidth = lp.width;//measureChildWithMargins 这个函数会用到宽，所以要保存一下
                    lp.width = child.getMeasuredWidth();
                    // Remeasure with new dimensions
                    measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0);
                    lp.width = oldWidth;
                }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int left       = getPaddingLeft();
        int right      = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                // 第一个子View是内容 宽度设置为全屏
                if (i == 0) {
                    childView.layout(left, getPaddingTop(), left + mMaxWidth, getPaddingTop() + childView.getMeasuredHeight());
                    left = left + mMaxWidth;
                } else {
                    if (isLeftSwipe) {
                        childView.layout(left, getPaddingTop(), left + childView.getMeasuredWidth(), getPaddingTop() + childView.getMeasuredHeight());
                        left = left + childView.getMeasuredWidth();
                    } else {
                        childView.layout(right - childView.getMeasuredWidth(), getPaddingTop(), right, getPaddingTop() + childView.getMeasuredHeight());
                        right = right - childView.getMeasuredWidth();
                    }
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isSwipeEnable) {
            acquireVelocityTracker(ev);
            final VelocityTracker verTracker = mVelocityTracker;
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。
                    isUnMoved = true;
                    // 每次DOWN时，默认是不拦截的
                    iosInterceptFlag = false;
                    // 如果有别的指头摸过了，那么就return false。这样后续的move..等事件也不会再来找这个View了。
                    if (isTouching) {
                        return false;
                    } else {
                        // 第一个摸的指头，赶紧改变标志，宣誓主权。
                        isTouching = true;
                    }
                    mLastP.set(ev.getRawX(), ev.getRawY());

                    //如果down，view和 viewCache 不一样，则立马让它还原。且把它置为null
                    if (mViewCache != null) {
                        if (mViewCache != this) {
                            mViewCache.smoothClose();
                            mViewCache = null;
                            // IOS模式开启的话，且当前有侧滑菜单的View，且不是自己的，就该拦截事件咯。
                            iosInterceptFlag = isIos;
                        }
                        // 只要有一个侧滑菜单处于打开状态，就不给外层布局上下滑动了
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    // 求第一个触点的id， 此时可能有多个触点，但至少一个，计算滑动速率用
                    mPointerId = ev.getPointerId(0);
                    break;

                case MotionEvent.ACTION_MOVE:
                    // IOS模式开启的话，且当前有侧滑菜单的View，且不是自己的，就该拦截事件咯。滑动也不该出现
                    if (iosInterceptFlag) {
                        break;
                    }
                    float gap = mLastP.x - ev.getRawX();
                    // 为了在水平滑动中禁止父类ListView等再竖直滑动，使屏蔽父布局滑动更加灵敏
                    if (Math.abs(gap) > 10 || Math.abs(getScrollX()) > 10) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    // 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。begin
                    if (Math.abs(gap) > mScaleTouchSlop) {
                        isUnMoved = false;
                    }
                    // 滑动使用scrollBy
                    scrollBy((int) (gap), 0);
                    // 越界修正
                    if (isLeftSwipe) { // 左滑
                        if (getScrollX() < 0) {
                            scrollTo(0, 0);
                        }
                        if (getScrollX() > mRightMenuWidths) {
                            scrollTo(mRightMenuWidths, 0);
                        }
                    } else {// 右滑
                        if (getScrollX() < -mRightMenuWidths) {
                            scrollTo(-mRightMenuWidths, 0);
                        }
                        if (getScrollX() > 0) {
                            scrollTo(0, 0);
                        }
                    }

                    mLastP.set(ev.getRawX(), ev.getRawY());
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // IOS模式开启的话，且当前有侧滑菜单的View，且不是自己的，就该拦截事件咯。滑动也不该出现
                    if (!iosInterceptFlag) {
                        // 求伪瞬时速度
                        verTracker.computeCurrentVelocity(1000, mMaxVelocity);
                        final float velocityX = verTracker.getXVelocity(mPointerId);
                        // 滑动速度超过阈值
                        if (Math.abs(velocityX) > 1000) {
                            if (velocityX < -1000) {
                                if (isLeftSwipe) {// 左滑
                                    // 平滑展开Menu
                                    smoothExpand();
                                    // 展开就加入ViewCache：
                                    mViewCache = this;
                                } else {
                                    // 平滑关闭Menu
                                    smoothClose();
                                }
                            } else {
                                if (isLeftSwipe) {// 左滑
                                    // 平滑关闭Menu
                                    smoothClose();
                                } else {
                                    // 平滑展开Menu
                                    smoothExpand();
                                    // 展开就加入ViewCache：
                                    mViewCache = this;
                                }
                            }
                        } else {
                            if (Math.abs(getScrollX()) > mLimit) {// 否则就判断滑动距离
                                // 平滑展开Menu
                                smoothExpand();
                                // 展开就加入ViewCache：
                                mViewCache = this;
                            } else {
                                // 平滑关闭Menu
                                smoothClose();
                            }
                        }
                    }
                    // 释放
                    releaseVelocityTracker();
                    // 没有手指在摸我了
                    isTouching = false;
                    break;

                default:
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                // 为了在侧滑时，屏蔽子View的点击事件
                if (isLeftSwipe) {
                    if (getScrollX() > mScaleTouchSlop) {
                        // 解决一个智障问题~ 居然不给点击侧滑菜单 我跪着谢罪
                        // 这里判断落点在内容区域屏蔽点击，内容区域外，允许传递事件继续向下的的。。。
                        if (ev.getX() < getWidth() - getScrollX()) {
                            // 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。
                            if (isUnMoved) {
                                smoothClose();
                            }
                            // true表示拦截
                            return true;
                        }
                    }
                } else {
                    if (-getScrollX() > mScaleTouchSlop) {
                        // 点击范围在菜单外 屏蔽
                        if (ev.getX() > -getScrollX()) {
                            // 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。
                            if (isUnMoved) {
                                smoothClose();
                            }
                            return true;
                        }
                    }
                }

                break;
        }
        // 模仿IOS 点击其他区域关闭：
        if (iosInterceptFlag) {
            // IOS模式开启，且当前有菜单的View，且不是自己的 拦截点击事件给子View
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /** 平滑展开 */
    private ValueAnimator mExpandAnim, mCloseAnim;

    public void smoothExpand() {
        mExpandAnim = ValueAnimator.ofInt(getScrollX(), isLeftSwipe ? mRightMenuWidths : -mRightMenuWidths);
        mExpandAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        mExpandAnim.setInterpolator(new OvershootInterpolator());
        mExpandAnim.setDuration(300).start();
    }

    /** 平滑关闭 */
    public void smoothClose() {
        mCloseAnim = ValueAnimator.ofInt(getScrollX(), 0);
        mCloseAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        mCloseAnim.setInterpolator(new AnticipateInterpolator());
        mCloseAnim.setDuration(300).start();
    }

    /**
     * @param event
     *         向 VelocityTracker 添加 MotionEvent
     *
     * @see VelocityTracker#obtain()
     * @see VelocityTracker#addMovement(MotionEvent)
     */
    private void acquireVelocityTracker(final MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * * 释放VelocityTracker
     *
     * @see android.view.VelocityTracker#clear()
     * @see android.view.VelocityTracker#recycle()
     */
    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    // 每次 ViewDetach 的时候，判断一下 ViewCache 是不是自己，如果是自己，关闭侧滑菜单，且 ViewCache 设置为null，
    // 理由：
    // 1 防止内存泄漏(ViewCache是一个静态变量)
    // 2 侧滑删除自己后，这个View被Recycler回收，复用，下一个进入屏幕的View的状态应该是普通状态，而不是展开状态。
    @Override
    protected void onDetachedFromWindow() {
        if (this == mViewCache) {
            mViewCache.smoothClose();
            mViewCache = null;
        }
        super.onDetachedFromWindow();
    }

    /**
     * 展开时，禁止长按
     */
    @Override
    public boolean performLongClick() {
        return Math.abs(getScrollX()) <= mScaleTouchSlop && super.performLongClick();
    }

    /**
     * 快速关闭。
     * 用于 点击侧滑菜单上的选项,同时想让它快速关闭(删除 置顶)。
     * 这个方法在ListView里是必须调用的，
     * 在RecyclerView里，视情况而定，如果是mAdapter.notifyItemRemoved(pos)方法不用调用。
     */
    public void quickClose() {
        if (this == mViewCache) {
            // 先取消展开动画
            if (null != mExpandAnim && mExpandAnim.isRunning()) {
                mExpandAnim.cancel();
            }
            // 关闭
            mViewCache.scrollTo(0, 0);
            mViewCache = null;
        }
    }

    public boolean isSwipeEnable() {
        return isSwipeEnable;
    }

    /**
     * 设置侧滑功能开关
     */
    public void setSwipeEnable(boolean swipeEnable) {
        isSwipeEnable = swipeEnable;
    }

    public boolean isIos() {
        return isIos;
    }

    /**
     * 设置是否开启IOS阻塞式交互
     */
    public SwipeMenu setIos(boolean ios) {
        isIos = ios;
        return this;
    }

    public boolean isLeftSwipe() {
        return isLeftSwipe;
    }

    /**
     * 设置是否开启左滑出菜单，设置false 为右滑出菜单
     */
    public SwipeMenu setLeftSwipe(boolean leftSwipe) {
        isLeftSwipe = leftSwipe;
        return this;
    }

    /**
     * 返回ViewCache
     */
    public static SwipeMenu getViewCache() {
        return mViewCache;
    }
}
