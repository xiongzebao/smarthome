package com.erongdu.wireless.views.pullToZoom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.erongdu.wireless.views.R;

/**
 * Author:    ZhuWenWu
 * Version    V1.0
 * Date:      2014/11/7  14:18.
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2014/11/7        ZhuWenWu            1.0                    1.0
 * Why & What is modified:
 */
public abstract class PullToZoomBase<T extends View> extends LinearLayout implements IPullToZoom<T> {
    private static final float FRICTION = 2.0f;
    // RootView
    protected T        mRootView;
    // 头部View
    protected View     mHeaderView;
    // 内容View
    protected View     mContentView;
    // 缩放拉伸View
    protected WaveView mZoomView;
    // 屏幕宽度
    protected int      mScreenWidth;
    // 屏幕高度
    protected int      mScreenHeight;
    private boolean isZoomEnabled = true;
    private boolean isParallax    = true;
    private boolean isZooming     = false;
    private boolean isHideHeader  = false;
    // 系统所能识别出的被认为是滑动的最小距离
    private int mTouchSlop;
    // 是否在滑动
    private boolean mIsBeingDragged = false;
    // 按下时X轴坐标
    private float              mInitialMotionX;
    // 按下时Y轴坐标
    private float              mInitialMotionY;
    // 最新X轴坐标
    private float              mLastMotionX;
    // 最新Y轴坐标
    private float              mLastMotionY;
    private OnPullZoomListener onPullZoomListener;

    public PullToZoomBase(Context context) {
        this(context, null);
    }

    public PullToZoomBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setGravity(Gravity.CENTER);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(localDisplayMetrics);
        mScreenHeight = localDisplayMetrics.heightPixels;
        mScreenWidth = localDisplayMetrics.widthPixels;

        // Refreshable View
        // By passing the attrs, we can add ListView/GridView params via XML
        mRootView = createRootView(context, attrs);

        if (attrs != null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(getContext());
            //初始化状态View
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PullToZoomView);

            int zoomViewResId = a.getResourceId(R.styleable.PullToZoomView_zoomView, 0);
            if (zoomViewResId > 0) {
                mZoomView = (WaveView) mLayoutInflater.inflate(zoomViewResId, null, false);
            }

            int headerViewResId = a.getResourceId(R.styleable.PullToZoomView_headerView, 0);
            if (headerViewResId > 0) {
                mHeaderView = mLayoutInflater.inflate(headerViewResId, null, false);
            }

            isParallax = a.getBoolean(R.styleable.PullToZoomView_isHeaderParallax, true);

            // Let the derivative classes have a go at handling attributes, then recycle them...
            handleStyledAttributes(a);
            a.recycle();
        }
        addView(mRootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void setOnPullZoomListener(OnPullZoomListener onPullZoomListener) {
        this.onPullZoomListener = onPullZoomListener;
    }

    @Override
    public T getPullRootView() {
        return mRootView;
    }

    @Override
    public View getZoomView() {
        return mZoomView;
    }

    @Override
    public View getHeaderView() {
        return mHeaderView;
    }

    @Override
    public boolean isPullToZoomEnabled() {
        return isZoomEnabled;
    }

    @Override
    public boolean isZooming() {
        return isZooming;
    }

    @Override
    public boolean isParallax() {
        return isParallax;
    }

    @Override
    public boolean isHideHeader() {
        return isHideHeader;
    }

    public void setZoomEnabled(boolean isZoomEnabled) {
        this.isZoomEnabled = isZoomEnabled;
    }

    public void setParallax(boolean isParallax) {
        this.isParallax = isParallax;
    }

    public void setHideHeader(boolean isHideHeader) {//header显示才能Zoom
        this.isHideHeader = isHideHeader;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!isPullToZoomEnabled() || isHideHeader()) {
            return false;
        }

        final int action = event.getAction();

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mIsBeingDragged = false;
            return false;
        }

        if (action != MotionEvent.ACTION_DOWN && mIsBeingDragged) {
            return true;
        }
        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                if (isReadyForPullStart()) {
                    final float y = event.getY();
                    final float x = event.getX();
                    final float diffX, diffY, absDiffX, absDiffY;

                    // We need to use the correct values, based on scroll direction
                    diffX = x - mLastMotionX;
                    diffY = y - mLastMotionY;
                    absDiffX = Math.abs(diffX);
                    absDiffY = Math.abs(diffY);

                    if (absDiffY > mTouchSlop && absDiffY > absDiffX) {
                        if (diffY >= 1f && isReadyForPullStart()) {
                            mLastMotionX = x;
                            mLastMotionY = y;
                            mIsBeingDragged = true;
                        }
                    }
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                if (isReadyForPullStart()) {
                    mLastMotionX = mInitialMotionX = event.getX();
                    mLastMotionY = mInitialMotionY = event.getY();
                    mIsBeingDragged = false;
                }
                break;
            }
        }
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isPullToZoomEnabled() || isHideHeader()) {
            return false;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN && event.getEdgeFlags() != 0) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                if (mIsBeingDragged) {
                    mLastMotionX = event.getX();
                    mLastMotionY = event.getY();
                    pullEvent();
                    isZooming = true;
                    return true;
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                if (isReadyForPullStart()) {
                    mLastMotionX = mInitialMotionX = event.getX();
                    mLastMotionY = mInitialMotionY = event.getY();
                    return true;
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (mIsBeingDragged) {
                    mIsBeingDragged = false;
                    // If we're already refreshing, just scroll icon_back to the top
                    if (isZooming()) {
                        smoothScrollToTop();
                        if (onPullZoomListener != null) {
                            onPullZoomListener.onPullZoomEnd();
                        }
                        isZooming = false;
                        return true;
                    }
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private void pullEvent() {
        final int   newScrollValue;
        final float initialMotionValue, lastMotionValue;

        initialMotionValue = mInitialMotionY;
        lastMotionValue = mLastMotionY;

        newScrollValue = Math.round(Math.min(initialMotionValue - lastMotionValue, 0) / FRICTION);

        pullHeaderToZoom(newScrollValue);
        if (onPullZoomListener != null) {
            onPullZoomListener.onPullZooming(newScrollValue);
        }
    }

    protected abstract void pullHeaderToZoom(int newScrollValue);

    public abstract void setHeaderView(View headerView);

    public abstract void setZoomView(WaveView zoomView);

    protected abstract T createRootView(Context context, AttributeSet attrs);

    protected abstract void smoothScrollToTop();

    protected abstract boolean isReadyForPullStart();

    public interface OnPullZoomListener {
        void onPullZooming(int newScrollValue);

        void onPullZoomEnd();
    }
}
