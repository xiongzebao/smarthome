package com.qidian.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


import com.qidian.base.R;
import com.qidian.base.common.Constant;
import com.qidian.base.model.OauthTokenMo;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class UserLogic {
    /**
     * 用户登录
     */
    public static void login(Activity activity, OauthTokenMo oauthTokenMo) {
        /** 登录逻辑处理 */
        SharedInfo.getInstance().saveValue(Constant.IS_LAND, true);
        SharedInfo.getInstance().saveEntity(oauthTokenMo);
        //删除头像图片
        SharedInfo.getInstance().remove("headImg"); //因为是缓存的
     /*   Routers.open(activity, RouterUrl.getRouterUrl(String.format(RouterUrl.AppCommon_Main, Constant.NUMBER_0)));
        activity.setResult(Activity.RESULT_OK);
        activity.finish();*/
    }

    /**
     * 登出必须执行的操作
     */
    public static void signOut() {
        OauthTokenMo tokenMo = SharedInfo.getInstance().getEntity(OauthTokenMo.class);

        SharedInfo.getInstance().remove(Constant.IS_LAND);
        // 删除保存的OauthToken信息
        SharedInfo.getInstance().remove(OauthTokenMo.class);

        //删除头像图片
        SharedInfo.getInstance().remove("headImg");

    }

    /**
     * 用户被动登出
     */
    public static void signOutForcibly(Activity activity) {
        signOut();

    }

    /**
     * 用户主动登出(到主界面)
     */
    public static void signOutToMain(final Activity activity) {
   /*     DialogUtils.showDialog(activity, R.string.user_login_out, new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog dialog) {
                dialog.dismiss();
                SharedInfo.getInstance().remove("borrowId");
                signOutForcibly(activity);
            }
        });*/
    }




}
