package com.erongdu.wireless.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/4/22 14:24
 * <p>
 * Description: 不允许短时间内双击响应
 */
public class NoDoubleClickTextView extends TextView {
    private long previousTime;

    public NoDoubleClickTextView(Context context) {
        super(context);
    }

    public NoDoubleClickTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoDoubleClickTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @param event
     *         touch事件
     *
     * @return 是否消耗点击事件
     * true - 消耗点击事件
     * false - 不消耗点击事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                long currentTime = System.currentTimeMillis();
                if (currentTime - previousTime < 2000) {
                    return true;
                }
                previousTime = currentTime;
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
