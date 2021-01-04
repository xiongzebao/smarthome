package com.qidian.kuaitui.utils;

import android.app.Activity;
import android.content.Intent;

import com.qidian.kuaitui.common.KTConstant;
import com.qidian.kuaitui.module.job.view.ReceiptDataActivity;
import com.qidian.kuaitui.module.mine.view.LoginActivity;

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
}
