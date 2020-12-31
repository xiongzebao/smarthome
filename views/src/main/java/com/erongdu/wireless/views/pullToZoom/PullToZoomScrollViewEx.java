package com.erongdu.wireless.views.pullToZoom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.views.R;

/**
 * Author:    ZhuWenWu
 * Version    V1.0
 * Date:      2014/11/10  14:25.
 * Description:
 * Modification  History:
 * Date               Author            Version             Description
 * -----------------------------------------------------------------------------------
 * 2014/11/10        ZhuWenWu            1.0                    1.0
 * Why & What is modified:
 */
public class PullToZoomScrollViewEx extends PullToZoomBase<ScrollView> {
    private static final String  TAG                  = PullToZoomScrollViewEx.class.getSimpleName();
    /** 是否自定义header高度 */
    private              boolean isCustomHeaderHeight = false;
    /** header容器 */
    private FrameLayout       mHeaderContainer;
    /** content容器 */
    private FrameLayout       mContentContainer;
    /** RootView */
    private LinearLayout      mRootContainer;
    /** header的高度 */
    private int               mHeaderHeight;
    /** 下拉动画 */
    private ScalingRunnable   mScalingRunnable;
    /** 下拉刷新接口 */
    private PullDownToRefresh pullDownToRefresh;
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float paramAnonymousFloat) {
            float f = paramAnonymousFloat - 1.0F;
            return 1.0F + f * (f * (f * (f * f)));
        }
    };

    public PullToZoomScrollViewEx(Context context) {
        this(context, null);
    }

    public PullToZoomScrollViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScalingRunnable = new ScalingRunnable();
    }

    private boolean isStarted = false;

    /**
     * 下拉header水波纹动画
     */
    @Override
    protected void pullHeaderToZoom(int newScrollValue) {
        // Logger.d(TAG, "pullHeaderToZoom --> newScrollValue = " + newScrollValue);
        // Logger.d(TAG, "pullHeaderToZoom --> mHeaderHeight = " + mHeaderHeight);
        if (mScalingRunnable != null && !mScalingRunnable.isFinished()) {
            mScalingRunnable.abortAnimation();
        }
        // 当向下滑动幅度等于0时，停止波浪动画（最大等于0）
        if (newScrollValue == 0) {
            mZoomView.stop();
            isStarted = false;
        } else {
            // 当向下滑动幅度小于0时，实时更新波浪在动画中的高度
            mZoomView.updateWaveLine(Math.abs(newScrollValue) + mHeaderHeight);
            // 如果没有开启波浪动画，则开启动画
            if (!isStarted) {
                mZoomView.start();
                isStarted = true;
            }
        }

        ViewGroup.LayoutParams localLayoutParams = mHeaderContainer.getLayoutParams();
        localLayoutParams.height = Math.abs(newScrollValue) + mHeaderHeight;
        mHeaderContainer.setLayoutParams(localLayoutParams);

        if (isCustomHeaderHeight) {
            ViewGroup.LayoutParams zoomLayoutParams = mZoomView.getLayoutParams();
            zoomLayoutParams.height = Math.abs(newScrollValue) + mHeaderHeight;
            mZoomView.setLayoutParams(zoomLayoutParams);
        }
    }

    /**
     * 是否显示headerView
     *
     * @param isHideHeader
     *         true: show false: hide
     */
    @Override
    public void setHideHeader(boolean isHideHeader) {
        if (isHideHeader != isHideHeader() && mHeaderContainer != null) {
            super.setHideHeader(isHideHeader);
            if (isHideHeader) {
                mHeaderContainer.setVisibility(GONE);
            } else {
                mHeaderContainer.setVisibility(VISIBLE);
            }
        }
    }

    @Override
    public void setHeaderView(View headerView) {
        if (headerView != null) {
            mHeaderView = headerView;
            updateHeaderView();
        }
    }

    @Override
    public void setZoomView(WaveView zoomView) {
        if (zoomView != null) {
            mZoomView = zoomView;
            updateHeaderView();
        }
    }

    private void updateHeaderView() {
        if (mHeaderContainer != null) {
            mHeaderContainer.removeAllViews();

            if (mZoomView != null) {
                mHeaderContainer.addView(mZoomView);
            }

            if (mHeaderView != null) {
                mHeaderContainer.addView(mHeaderView);
            }
        }
    }

    public void setContentView(View contentView) {
        if (contentView != null) {
            mContentView = contentView;
            updateContentView();
        }
    }

    private void updateContentView() {
        if (mContentContainer != null) {
            mContentContainer.removeAllViews();

            if (mContentView != null) {
                mContentContainer.addView(mContentView);
            }
        }
    }

    @Override
    protected ScrollView createRootView(Context context, AttributeSet attrs) {
        ScrollView scrollView = new InternalScrollView(context, attrs);
        scrollView.setId(R.id.scrollview);
        return scrollView;
    }

    @Override
    protected void smoothScrollToTop() {
        //        Logger.d(TAG, "smoothScrollToTop --> ");
        mScalingRunnable.startAnimation(200L);
    }

    @Override
    protected boolean isReadyForPullStart() {
        return mRootView.getScrollY() == 0;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void handleStyledAttributes(TypedArray a) {
        mRootContainer = new LinearLayout(getContext());
        mRootContainer.setOrientation(LinearLayout.VERTICAL);
        mHeaderContainer = new FrameLayout(getContext());
        mContentContainer = new FrameLayout(getContext());

        LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mContentContainer.setLayoutParams(lp1);

        if (mZoomView != null) {
            mHeaderContainer.addView(mZoomView);
        }
        if (mHeaderView != null) {
            mHeaderContainer.addView(mHeaderView);
        }
        int contentViewResId = a.getResourceId(R.styleable.PullToZoomView_contentView, 0);
        if (contentViewResId > 0) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(getContext());
            mContentView = mLayoutInflater.inflate(contentViewResId, null, false);
        }

        if (mContentView != null) {
            mContentContainer.addView(mContentView);
        }
        mRootContainer.addView(mHeaderContainer);
        mRootContainer.addView(mContentContainer);

        // 是否限制子View在其范围内
        mRootContainer.setClipChildren(false);
        mHeaderContainer.setClipChildren(true);
        mContentContainer.setClipChildren(false);

        mRootView.addView(mRootContainer);
    }

    /**
     * 设置HeaderView高度
     */
    public void setHeaderViewSize(int width, int height) {
        if (mHeaderContainer != null) {
            Object localObject = mHeaderContainer.getLayoutParams();
            if (localObject == null) {
                localObject = new ViewGroup.LayoutParams(width, height);
            }
            ((ViewGroup.LayoutParams) localObject).width = width;
            ((ViewGroup.LayoutParams) localObject).height = height;
            mHeaderContainer.setLayoutParams((ViewGroup.LayoutParams) localObject);
            mHeaderHeight = height;
            isCustomHeaderHeight = true;
        }
    }

    /**
     * 设置HeaderView LayoutParams
     */
    public void setHeaderLayoutParams(LayoutParams layoutParams) {
        if (mHeaderContainer != null) {
            mHeaderContainer.setLayoutParams(layoutParams);
            mHeaderHeight = layoutParams.height;
            isCustomHeaderHeight = true;
        }
    }

    public void setPullDownToRefresh(final PullDownToRefresh pullDownToRefresh) {
        this.pullDownToRefresh = pullDownToRefresh;
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
        // Logger.d(TAG, "onLayout --> ");
        if (mHeaderHeight == 0 && mZoomView != null) {
            mHeaderHeight = mHeaderContainer.getHeight();
        }
    }

    /**
     * 水波纹动画类
     */
    class ScalingRunnable implements Runnable {
        /**
         * 动画持续时间
         */
        protected long  mDuration;
        /**
         * 动画开始事件
         */
        protected long  mStartTime;
        protected float mScale;
        /**
         * 是否停止动画
         */
        protected boolean mIsFinished = true;
        /**
         * 是否刷新
         */
        protected boolean isRefresh   = false;

        ScalingRunnable() {
        }

        /**
         * 终止动画
         */
        public void abortAnimation() {
            mIsFinished = true;
        }

        public boolean isFinished() {
            return mIsFinished;
        }

        public void run() {
            if (mZoomView != null) {
                float                  f2;
                ViewGroup.LayoutParams localLayoutParams;
                if ((!mIsFinished) && (mScale > 1.0D)) {
                    float f1 = ((float) SystemClock.currentThreadTimeMillis() - (float) mStartTime) / (float) mDuration;
                    f2 = mScale - (mScale - 1.0F) * PullToZoomScrollViewEx.sInterpolator.getInterpolation(f1);
                    localLayoutParams = mHeaderContainer.getLayoutParams();
                     MyLog.d( "ScalingRunnable --> f2 = " + f2);
                    if (pullDownToRefresh != null && f2 >= 1.2 && !isRefresh) {
                        isRefresh = true;
                        pullDownToRefresh.onPullDownToRefresh();
                    }
                    // 图片方法比例大于1时，进行缩小动画更新图片大小
                    if (f2 > 1.0F) {
                        localLayoutParams.height = ((int) (f2 * mHeaderHeight));
                        mHeaderContainer.setLayoutParams(localLayoutParams);
                        if (isCustomHeaderHeight) {
                            ViewGroup.LayoutParams zoomLayoutParams;
                            zoomLayoutParams = mZoomView.getLayoutParams();
                            zoomLayoutParams.height = ((int) (f2 * mHeaderHeight));
                            mZoomView.setLayoutParams(zoomLayoutParams);
                        }
                        post(this);
                        return;
                    } else {
                        mZoomView.stop();
                        isStarted = false;
                        isRefresh = false;
                    }
                    mIsFinished = true;
                }
            }
        }

        /**
         * 开始动画
         */
        public void startAnimation(long paramLong) {
            if (mZoomView != null) {
                mStartTime = SystemClock.currentThreadTimeMillis();
                mDuration = paramLong;
                mScale = ((float) (mHeaderContainer.getBottom()) / mHeaderHeight);
                mIsFinished = false;
                post(this);
            }
        }
    }

    protected class InternalScrollView extends ScrollView {
        private OnScrollViewChangedListener onScrollViewChangedListener;
        private boolean isFirst = true;

        public InternalScrollView(Context context) {
            this(context, null);
        }

        public InternalScrollView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public void setOnScrollViewChangedListener(OnScrollViewChangedListener onScrollViewChangedListener) {
            this.onScrollViewChangedListener = onScrollViewChangedListener;
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            super.onScrollChanged(l, t, oldl, oldt);
            if (isFirst) {
                isFirst = false;
                scrollTo(0, 0);
            }
            if (onScrollViewChangedListener != null) {
                onScrollViewChangedListener.onInternalScrollChanged(l, t, oldl, oldt);
            }
        }
    }

    /**
     * 滑动改变监听接口
     */
    protected interface OnScrollViewChangedListener {
        void onInternalScrollChanged(int left, int top, int oldLeft, int oldTop);
    }

    /**
     * 下拉刷新接口
     */
    public interface PullDownToRefresh {
        void onPullDownToRefresh();
    }
}
