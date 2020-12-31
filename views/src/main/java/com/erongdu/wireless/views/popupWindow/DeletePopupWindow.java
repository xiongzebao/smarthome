package com.erongdu.wireless.views.popupWindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.erongdu.wireless.views.NoDoubleClickButton;
import com.erongdu.wireless.views.R;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/3/24 14:07
 * <p>
 * Description: 删除选择窗口
 */
public class DeletePopupWindow extends PopupWindow {
    private Button deleteBtn;

    public DeletePopupWindow(Context context) {
        this(context, null, 0);
    }

    public DeletePopupWindow(Context context, OnClickListener itemsOnClick) {
        this(context, itemsOnClick, 0);
    }

    public DeletePopupWindow(Context context, OnClickListener itemsOnClick, int deleteText) {
        super(context);
        LayoutInflater inflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View           mMenuView = inflater.inflate(R.layout.delete_popup_window, null);
        deleteBtn = (NoDoubleClickButton) mMenuView.findViewById(R.id.deleteBtn);
        if (deleteText != 0) {
            ((NoDoubleClickButton) mMenuView.findViewById(R.id.deleteBtn)).setText(deleteText);
        }
        mMenuView.findViewById(R.id.deleteBtn).setOnClickListener(itemsOnClick);
        mMenuView.findViewById(R.id.cancelBtn).setOnClickListener(itemsOnClick);

        deleteBtn.setOnClickListener(itemsOnClick);

        // 设置PickPopupWindow的View
        this.setContentView(mMenuView);
        // 设置PickPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置PickPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        // 设置PickPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置PickPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        // 设置PickPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = deleteBtn.getTop();
                int y      = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}
