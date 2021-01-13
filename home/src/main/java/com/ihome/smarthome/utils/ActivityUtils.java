package com.ihome.smarthome.utils;

import android.app.Activity;
import android.content.Intent;

import com.ihome.base.common.Constant;
import com.ihome.base.utils.DialogUtils;
import com.ihome.base.utils.SharedInfo;
import com.ihome.smarthome.R;
import com.ihome.smarthome.common.KTConstant;
import com.ihome.smarthome.module.job.view.ReceiptDataActivity;
import com.ihome.smarthome.module.mine.view.LoginActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @author xiongbin
 * @description:
 * @date : 2020/12/30 9:35
 */

public class ActivityUtils {

    public static void startLoginActivity(Activity activity){

        Intent intent = new Intent(activity, LoginActivity.class);
       // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
       // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);

    }

    public static void startInterViewActvity(Activity activity ,int type){
        Intent intent = new Intent(activity, ReceiptDataActivity.class);
        intent.putExtra(KTConstant.TYPE,type);
        activity.startActivity(intent);
    }


    public static void signOutloginNoTip(final Activity activity){
        SharedInfo.getInstance().remove(Constant.TOKEN);
        Intent intent=new Intent(activity, LoginActivity.class);
        activity.startActivityForResult(intent,KTConstant.LOGIN);
    }

    public static void signOutToLogin(final Activity activity) {

        DialogUtils.showDialog(activity, R.string.user_login_out, new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog dialog) {
                  CommenSetUtils.signOut();
                Intent intent=new Intent(activity, LoginActivity.class);
                activity.startActivityForResult(intent,KTConstant.LOGIN);
                dialog.dismiss();
            }
        });
    }
}
