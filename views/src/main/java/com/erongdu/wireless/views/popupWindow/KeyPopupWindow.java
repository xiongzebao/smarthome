package com.erongdu.wireless.views.popupWindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.KeyboardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.erongdu.wireless.views.R;

/**
 * Author: Chenming
 * E-mail: cm1@erongdu.com
 * Date: 2016/11/29 下午5:57
 * <p/>
 * Description:
 */
public class KeyPopupWindow extends PopupWindow {
    public KeyboardView keyboardView;

    public KeyPopupWindow(Context context) {
        this(context, null, 0);
    }

    public KeyPopupWindow(Context context, View.OnClickListener itemsOnClick) {
        this(context, itemsOnClick, 0);
    }

    public KeyPopupWindow(Context context, View.OnClickListener itemsOnClick, int deleteText) {
        super(context);
        LayoutInflater inflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View           mMenuView = inflater.inflate(R.layout.key_board_view, null);
        keyboardView = (KeyboardView) mMenuView.findViewById(R.id.keyboard_view);

        // 设置PickPopupWindow的View
        this.setContentView(mMenuView);
        // 设置PickPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置PickPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置PickPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置PickPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        // 设置PickPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
       /* mMenuView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
re
            }
        });*/
    }
}
