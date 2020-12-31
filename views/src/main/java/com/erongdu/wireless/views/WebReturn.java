package com.erongdu.wireless.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.webkit.JavascriptInterface;

import com.erongdu.wireless.tools.utils.ToastUtil;

/**
 * Email: hbh@erongdu.com
 * Created by hebihe on 6/1/16.
 */
public class WebReturn {
    private Context context;

    public WebReturn(Context context) {
        this.context = context;
    }

    Handler mHandler = new Handler();

    @JavascriptInterface
    public void webReturn(final String status, final String remsg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (status.equals("1")) {
                    Activity activity = (Activity) context;
                    Intent   intent   = new Intent();
                    activity.setResult(Activity.RESULT_OK, intent);
                    activity.finish();
                } else {
                    ToastUtil.toast(remsg);
                }
            }
        });
    }
}
