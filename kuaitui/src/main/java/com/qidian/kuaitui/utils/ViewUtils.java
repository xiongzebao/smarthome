package com.qidian.kuaitui.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.erongdu.wireless.tools.utils.DateUtil;
import com.qidian.kuaitui.R;

import java.util.Calendar;
import java.util.Date;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/8 15:58
 */

public class ViewUtils {

    public static void showTimePicker(Context context, String title,boolean isDialog,OnTimeSelectListener listener) {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(new Date());
        //endDate.set(2020,1,1);
         startDate.add(Calendar.DATE,-60);
        //正确设置方式 原因：注意事项有说明
       // startDate.set(2019, 0, 1);




       // endDate.set(2022, 11, 31);


        TimePickerView pvTime = new TimePickerBuilder(context,listener)
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                // .setContentSize(18)//滚轮文字大小
                .setContentTextSize(18)
                .setSubCalSize(14)
                .setTitleColor(context.getResources().getColor(R.color.color99))
                .setTitleSize(14)//标题文字大小
                .setTitleText(title)//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(context.getResources().getColor(R.color.color33))//标题文字颜色
                .setSubmitColor(context.getResources().getColor(R.color.colorPrimaryDark))//确定按钮文字颜色
                .setCancelColor(context.getResources().getColor(R.color.colorPrimaryDark))//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(isDialog)//是否显示为对话框样式
                .build();
        pvTime.show();
    }
}
