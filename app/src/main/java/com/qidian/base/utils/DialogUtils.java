package com.qidian.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;


import com.qidian.base.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Author: Chenming
 * E-mail: cm1@erongdu.com
 * Date: 2016/11/25 下午5:37
 * <p/>
 * Description: dialog 弹出框util
 */
public class DialogUtils {
    public static void showDialog(Context context, String contentText) {
        showDialog(context, SweetAlertDialog.NORMAL_TYPE, contentText, null);
    }

    public static void showDialog(Context context, int contentText) {
        showDialog(context, SweetAlertDialog.NORMAL_TYPE, context.getString(contentText), null);
    }

    public static void showDialog(Context context, int type, String contentText) {
        showDialog(context, type, contentText, null);
    }

    public static void showDialog(Context context, int type, int contentText) {
        showDialog(context, type, context.getString(contentText), null);
    }

    public static void showDialog(Context context, int type, String contentText, SweetAlertDialog.OnSweetClickListener confirmClick) {
        showDialog(context, type, contentText, confirmClick, null);
    }

    public static void showDialog(Context context, int type, int contentText, SweetAlertDialog.OnSweetClickListener confirmClick) {
        showDialog(context, type, context.getString(contentText), confirmClick, null);
    }

    public static void showDialog(Context context, String contentText, SweetAlertDialog.OnSweetClickListener confirmClick, SweetAlertDialog.OnSweetClickListener cancelClick) {
        showDialog(context, SweetAlertDialog.NORMAL_TYPE, contentText, confirmClick, cancelClick);
    }

    public static void showDialog(Context context, int contentText, SweetAlertDialog.OnSweetClickListener confirmClick, SweetAlertDialog.OnSweetClickListener cancelClick) {
        showDialog(context, SweetAlertDialog.NORMAL_TYPE, context.getString(contentText), confirmClick, cancelClick);
    }

    public static void showDialog(Context context, String contentText, SweetAlertDialog.OnSweetClickListener confirmClick) {
        showDialog(context, SweetAlertDialog.NORMAL_TYPE, contentText, confirmClick, null);
    }

    public static void showDialog(Context context, String contentText, SweetAlertDialog.OnSweetClickListener confirmClick,String sureText) {
        showDialog(context, SweetAlertDialog.NORMAL_TYPE, contentText, confirmClick, null,sureText);
    }

    public static void showDialog(Context context, int contentText, SweetAlertDialog.OnSweetClickListener confirmClick) {
        showDialog(context, SweetAlertDialog.NORMAL_TYPE, context.getString(contentText), confirmClick, null);
    }

    public static void showDialog(Context context, int contentText, SweetAlertDialog.OnSweetClickListener confirmClick,String sureText) {
        showDialog(context, SweetAlertDialog.NORMAL_TYPE, context.getString(contentText), confirmClick, null,sureText);
    }

    public static void showDialog(Context context, String cancelText, String confirmText, String contentText, SweetAlertDialog.OnSweetClickListener cancelCLick) {
        showDialog(context, cancelText, confirmText, contentText, null, cancelCLick);
    }

    public static void showDialog(Context context, int confirmText, int contentText, SweetAlertDialog.OnSweetClickListener confirmClick,SweetAlertDialog. OnSweetClickListener cancelClick) {
        showDialog(context, context.getString(R.string.dialog_cancel), context.getString(confirmText), context.getString(contentText), confirmClick,
                cancelClick);
    }

    public static void showDialog(Context context, int cancelText, int confirmText, int contentText, SweetAlertDialog.OnSweetClickListener confirmClick) {
        showDialog(context, context.getString(cancelText), context.getString(confirmText), context.getString(contentText), confirmClick, null);
    }

    /*public static void showDialog(Context context, String cancelText, String confirmText, String contentText, OnSweetClickListener confirmClick) {
        showDialog(context, cancelText, confirmText, contentText, confirmClick, null);
    }*/

    public static void showDialog(Context context, int type, String contentText,
                                  SweetAlertDialog.OnSweetClickListener confirmClick, SweetAlertDialog.OnSweetClickListener cancelCLick) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, type)
                .setContentText(contentText)
                .setConfirmText(context.getString(R.string.dialog_confirm))
                .setCancelText("取消")
                .setConfirmClickListener(confirmClick)
                .setCancelClickListener(cancelCLick);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        sweetAlertDialog .getButton(DialogInterface.BUTTON_POSITIVE).setBackground(context.getResources().getDrawable(R.drawable.button_enabled1));
        sweetAlertDialog .getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(14);
        sweetAlertDialog .getButton(DialogInterface.BUTTON_POSITIVE).setAlpha(0.8f);

        if(sweetAlertDialog .getButton(DialogInterface.BUTTON_NEGATIVE)!=null){
            sweetAlertDialog .getButton(DialogInterface.BUTTON_NEGATIVE).setBackground(context.getResources().getDrawable(R.drawable.button_enabled2));
            sweetAlertDialog .getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(14);
            sweetAlertDialog .getButton(DialogInterface.BUTTON_NEGATIVE).setAlpha(0.8f);
        }
    }




    public static void showDialog(Context context, int type, String contentText,
                                  SweetAlertDialog.OnSweetClickListener confirmClick, SweetAlertDialog.OnSweetClickListener cancelCLick,String sureText) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, type)
                .setContentText(contentText)
                .setConfirmText(sureText)
                .setCancelText("取消")
                .setConfirmClickListener(confirmClick)
                .setCancelClickListener(cancelCLick);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        sweetAlertDialog .getButton(DialogInterface.BUTTON_POSITIVE).setBackground(context.getResources().getDrawable(R.drawable.button_enabled1));
        sweetAlertDialog .getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(14);
        sweetAlertDialog .getButton(DialogInterface.BUTTON_POSITIVE).setAlpha(0.8f);

        if(sweetAlertDialog .getButton(DialogInterface.BUTTON_NEGATIVE)!=null){
            sweetAlertDialog .getButton(DialogInterface.BUTTON_NEGATIVE).setBackground(context.getResources().getDrawable(R.drawable.button_enabled2));
            sweetAlertDialog .getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(14);
            sweetAlertDialog .getButton(DialogInterface.BUTTON_NEGATIVE).setAlpha(0.8f);
        }
    }



    public static void showDialog(Context context, String cancelText, String confirmText, String contentText,
                                  SweetAlertDialog.OnSweetClickListener confirmClick, SweetAlertDialog.OnSweetClickListener cancelCLick) {

        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                .setContentText(contentText)
                .setCancelText(cancelText)
                .setConfirmText(confirmText)
                .setConfirmClickListener(confirmClick)
                .setCancelClickListener(cancelCLick);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        sweetAlertDialog .getButton(DialogInterface.BUTTON_POSITIVE).setBackground(context.getResources().getDrawable(R.drawable.button_enabled1));
        sweetAlertDialog .getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(14);
        sweetAlertDialog .getButton(DialogInterface.BUTTON_POSITIVE).setAlpha(0.8f);

        if(sweetAlertDialog .getButton(DialogInterface.BUTTON_NEGATIVE)!=null){
            sweetAlertDialog .getButton(DialogInterface.BUTTON_NEGATIVE).setBackground(context.getResources().getDrawable(R.drawable.button_enabled2));
            sweetAlertDialog .getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(14);
            sweetAlertDialog .getButton(DialogInterface.BUTTON_NEGATIVE).setAlpha(0.8f);
        }
    }

    public static void showDialog(Context context, int type, String contentText,
                                  SweetAlertDialog.OnSweetClickListener confirmClick, boolean isCancel) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, type)
                .setContentText(contentText)
                .setConfirmText(context.getString(R.string.dialog_confirm))

                .setConfirmClickListener(confirmClick)
                .showCancelButton(isCancel);
        if(isCancel){
            sweetAlertDialog.setCancelText("取消");
        }
        sweetAlertDialog.setCancelable(isCancel);
        sweetAlertDialog.show();
        //.setTitleTextColor()设置标题颜色
        //.setCancelText()设置取消文字颜色
        //.setConfirmTextColor()设置确认文字颜色
        //.setBottomButton() 设置底部按钮样式View

        sweetAlertDialog .getButton(DialogInterface.BUTTON_POSITIVE).setBackground(context.getResources().getDrawable(R.drawable.button_enabled1));
        sweetAlertDialog .getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(14);
        sweetAlertDialog .getButton(DialogInterface.BUTTON_POSITIVE).setAlpha(0.8f);

        if(sweetAlertDialog .getButton(DialogInterface.BUTTON_NEGATIVE)!=null){
            sweetAlertDialog .getButton(DialogInterface.BUTTON_NEGATIVE).setBackground(context.getResources().getDrawable(R.drawable.button_enabled2));
            sweetAlertDialog .getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(14);
            sweetAlertDialog .getButton(DialogInterface.BUTTON_NEGATIVE).setAlpha(0.8f);
        }
    }

    public static void showToastDialog(Context context, String contentText) {
        showDialog(context, SweetAlertDialog.NORMAL_TYPE, contentText, new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        }, false);
        //.setTitleTextColor()设置标题颜色
        //.setCancelText()设置取消文字颜色
        //.setConfirmTextColor()设置确认文字颜色
        //.setBottomButton() 设置底部按钮样式View

    }

    public static void showDialog(Context context, String contentText,
                                  SweetAlertDialog.OnSweetClickListener confirmClick, boolean isCancel) {
        SweetAlertDialog sweetAlertDialog= new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                .setContentText(contentText)
                .setConfirmText(context.getString(R.string.dialog_confirm))
                .setConfirmClickListener(confirmClick)
                .showCancelButton(isCancel);
        if(isCancel){
            sweetAlertDialog.setCancelText("取消");
        }
        sweetAlertDialog.show();


        sweetAlertDialog .getButton(DialogInterface.BUTTON_POSITIVE).setBackground(context.getResources().getDrawable(R.drawable.button_enabled1));
        sweetAlertDialog .getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(14);
        sweetAlertDialog .getButton(DialogInterface.BUTTON_POSITIVE).setAlpha(0.8f);

        if(sweetAlertDialog .getButton(DialogInterface.BUTTON_NEGATIVE)!=null){
            sweetAlertDialog .getButton(DialogInterface.BUTTON_NEGATIVE).setBackground(context.getResources().getDrawable(R.drawable.button_enabled2));
            sweetAlertDialog .getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(14);
            sweetAlertDialog .getButton(DialogInterface.BUTTON_NEGATIVE).setAlpha(0.8f);
        }
    }

//    public static void showDialog(Context context, Spanned contentText,
//                                  SweetAlertDialog.OnSweetClickListener confirmClick, boolean isCancel) {
//        new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
//                .setContentText(contentText)
//                .setConfirmText(context.getString(R.string.dialog_confirm))
//                .setConfirmClickListener(confirmClick)
//                .showCancelButton(isCancel)
//                .show();
//
//    }

    ///////////////////////////////////////////////////////////////////////////
    // 特殊样式
    ///////////////////////////////////////////////////////////////////////////
    public static void showEditDialog(Context context, int type, String contentText, String confirmText, SweetAlertDialog.OnSweetClickListener confirmClick) {
        if (!activityIsRunning(context)) {
            return;
        }
        new SweetAlertDialog(context, type)
                .setContentText(contentText)
                .setConfirmText(confirmText)
                .setConfirmClickListener(confirmClick)
                .showCancelButton(true)
                .show();
    }

    /**
     * Activity是否可用
     */
    private static boolean activityIsRunning(Context context) {
        return !(context instanceof Activity) || !((Activity) context).isFinishing();
    }
}
