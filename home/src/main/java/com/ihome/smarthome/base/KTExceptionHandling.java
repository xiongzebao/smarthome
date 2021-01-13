package com.ihome.smarthome.base;

import com.erongdu.wireless.tools.utils.ActivityManager;
import com.ihome.base.common.SingletonDialog;
import com.ihome.smarthome.utils.ActivityUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @author xiongbin
 * @description:
 * @date : 2020/12/30 9:56
 */

public class KTExceptionHandling {
    public final   static int TOKEN_EXPIRY = 401;

    public static boolean operate(int code){
        switch (code){
            case TOKEN_EXPIRY:

                SingletonDialog.showDialog(ActivityManager.peek(), "账号过期，请重新登录", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        ActivityUtils.signOutloginNoTip(ActivityManager.peek());
                        sweetAlertDialog.dismiss();
                    }
                }, false);


                return true;
        }

        return false;
    }
}
