package com.qidian.kuaitui.base;

import android.util.Log;


import com.erongdu.wireless.network.entity.HttpResult;
import com.erongdu.wireless.tools.utils.ActivityManager;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.qidian.base.common.SingletonDialog;
import com.qidian.base.model.OauthTokenMo;
import com.qidian.base.utils.SharedInfo;


import cn.pedant.SweetAlert.SweetAlertDialog;

//import cn.pedant.SweetAlert.OnSweetClickListener;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/5/30 11:53
 * <p/>
 * Description: 异常处理
 */
@SuppressWarnings("unchecked")
final class ExceptionHandling {


    static void operate(final HttpResult result) {
        switch (result.getCode()) {
            case AppResultCode.TOKEN_TIMEOUT:
                OauthTokenMo tokenMo = SharedInfo.getInstance().getEntity(OauthTokenMo.class);
                if (null != tokenMo) {
                } else {
 /*                   UserLogic.signOut();
                    Routers.openForResult(ActivityManage.peek(), RouterUrl.getRouterUrl(String.format(RouterUrl.UserInfoManage_Login, Constant.STATUS_3)), 0);*/
                }
                break;

            case AppResultCode.TOKEN_REFRESH_TIMEOUT:
   /*             UserLogic.signOut();
                Routers.openForResult(ActivityManage.peek(), RouterUrl.getRouterUrl(String.format(RouterUrl.UserInfoManage_Login, Constant.STATUS_3)), 0);
*/                break;
            case AppResultCode.TOKEN_NOT_UNIQUE:
            case AppResultCode.TOKEN_NOT_EXIT_NEW:
            case AppResultCode.TOKEN_NOT_EXIT:

                SingletonDialog.showDialog(ActivityManager.peek(), "账号过期，请重新登录", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                    /*    UserLogic.signOut();
                        Routers.openForResult(ActivityManage.peek(), RouterUrl.getRouterUrl(String.format(RouterUrl.UserInfoManage_Login, Constant.STATUS_3))
                                , 0);*/
                        //sweetAlertDialog.dismissWithAnimation();
                        sweetAlertDialog.dismiss();
                    }
                }, false);
                Log.d("ExceptionHandling", "TOKEN_NOT_EXIT:" + AppResultCode.TOKEN_NOT_EXIT);
                break;
            case AppResultCode.WECHAT_NOT_BIND:
                SingletonDialog.showDialog(ActivityManager.peek(), "尚未绑定微信账号，即刻绑定", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                        OauthTokenMo tokenMo = SharedInfo.getInstance().getEntity(OauthTokenMo.class);
//                        if (tokenMo != null) {
//                            if (!MyApplication.api.isWXAppInstalled()) {
//                                ToastUtil.toast("您的设备未安装微信客户端");
//                            } else {
//                                final SendAuth.Req req = new SendAuth.Req();
//                                req.scope = "snsapi_userinfo";
//                                req.state = tokenMo.getTicket();
//                                MyApplication.api.sendReq(req);
//
//                            }
//                        }
//                        sweetAlertDialog.dismiss();
                    }
                }, true);
                break;
            case AppResultCode.BASEINFO_NOT_BIND:
                SingletonDialog.showDialog(ActivityManager.peek(), "您未完成基本信息认证，请先完善个人资料", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        //todo 基本信息界面
//                        ActivityManage.peek().startActivity(new Intent(ActivityManage.peek(), BaseInfoSetup1Act.class));
//                        sweetAlertDialog.dismiss();
                    }
                }, true);
                break;

            default:
                break;
        }
        if (result.getCode() != 410 && result.getCode() != 413 && result.getCode() != 412 && result.getCode() != 411 && result.getCode() != 5001 && result.getCode() != 5002
                &&result.getCode() != 5003) {
            ToastUtil.toast(result.getMsg());
        }
    }


}
