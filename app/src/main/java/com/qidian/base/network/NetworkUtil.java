package com.qidian.base.network;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ActivityManager;
import com.qidian.base.views.CutscenesProgress;


/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/4/19 16:11
 * <p/>
 * Description: 网络请求工具类
 */
public class NetworkUtil {

    private static CutscenesProgress cutscenesProgress = null;
    /**
     * 请求返回后是否自动关闭
     */
    private static boolean automatic = true;

    /**
     * @param title      提示框的标题，如果不想要标题，传递null
     * @param content    提示框的提示内容，可传递null
     * @param cancelable 该dialog是否可以cancel
     */
    public static CutscenesProgress init(Context context, String title, String content, boolean cancelable, DialogInterface.OnCancelListener listener) {
        CutscenesProgress dialog = CutscenesProgress.createDialog(context);
        if (!TextUtils.isEmpty(title)) {
            dialog.setTitle(title);
        }
        if (!TextUtils.isEmpty(content)) {
            dialog.setMessage(content);
        }
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(listener);
        return dialog;
    }

    /**
     * @param title      提示框的标题，如果不想要标题，传递null
     * @param content    提示框的提示内容，可传递null
     * @param cancelable 该dialog是否可以cancel
     */
    public static CutscenesProgress init(Context context, String title, String content, boolean cancelable, DialogInterface.OnCancelListener listener,int progress,long total) {
        CutscenesProgress dialog = CutscenesProgress.createDialog(context);
        if (!TextUtils.isEmpty(title)) {
            dialog.setTitle(title);
        }
        if (!TextUtils.isEmpty(content)) {
            dialog.setMessage(content);
        }
        if(progress>0&&total>0){
//            dialog.setProgress(progress,total);
        }
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(listener);
        return dialog;
    }

    public static void showCutscenes(String title, String content) {
        showCutscenes(title, content, true, true, null);
    }

    public static void showCutscenes(String title, String content,int progress,long total) {
        showCutscenes(title, content, true, true, null,progress,total);
    }

    public static void showCutscenes(String title, String content,int progress,long total,boolean cancelable) {
        showCutscenes(title, content, cancelable, true, null,progress,total);
    }

    public static void showCutscenes(String title, String content, boolean cancelable, boolean automatic) {
        showCutscenes(title, content, cancelable, automatic, null);
    }

    /**
     * 取消显示的同时，取消网络请求
     *
     * @param call retrofit2网络请求
     */
    public static void showCutscenes(final retrofit2.Call call) {
        showCutscenes(null, null, true, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cutscenesProgress.dismiss();
               // cutscenesProgress = null;
                if (null != call) {
                    call.cancel();
                }
            }
        });
    }

    /**
     * 取消显示的同时，取消网络请求
     *
     * @param call retrofit2网络请求
     */
    public static void showCutscenes(final retrofit2.Call call,Context context) {
        showCutscenes(null, null, true, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cutscenesProgress.dismiss();
                //cutscenesProgress = null;
                if (null != call) {
                    call.cancel();
                }
            }
        },context);
    }

    /**
     * 显示加载dialog
     *
     * @param title      标题
     * @param content    内容
     * @param cancelable 是否可以取消显示
     * @param listener   取消显示后的监听
     */
    public static void showCutscenes(String title, String content, boolean cancelable, boolean automatic, DialogInterface.OnCancelListener listener) {
        try {
            NetworkUtil.automatic = automatic;
            Context context = ActivityManager.peek();
            if (null != context) {
                if (null == cutscenesProgress) {
                    cutscenesProgress = init(context, title, content, cancelable, listener);
                    cutscenesProgress.show();
                } else {
                /*    if (cutscenesProgress.isShowing()) {
                        return;
                    } else {*/
                        cutscenesProgress.show();
                    }
                }
           // }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示加载dialog
     *
     * @param title      标题
     * @param content    内容
     * @param cancelable 是否可以取消显示
     * @param listener   取消显示后的监听
     */
    public static void showCutscenes(String title, String content, boolean cancelable, boolean automatic, DialogInterface.OnCancelListener listener,Context context) {
        try {
            NetworkUtil.automatic = automatic;
//            Context context = ActivityManage.peek();
            if (null != context) {
                if (null == cutscenesProgress) {
                    cutscenesProgress = init(context, title, content, cancelable, listener);
                    cutscenesProgress.show();
                } else {
                    if (cutscenesProgress.isShowing()) {
                        return;
                    } else {
                        cutscenesProgress.show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示加载dialog
     *
     * @param title      标题
     * @param content    内容
     * @param cancelable 是否可以取消显示
     * @param listener   取消显示后的监听
     */
    public static void showCutscenes(String title, String content, boolean cancelable, boolean automatic, DialogInterface.OnCancelListener listener,int progress,long total) {
        try {
            NetworkUtil.automatic = automatic;
            Context context = ActivityManager.peek();
            if (null != context) {
                if (null == cutscenesProgress) {
                    MyLog.e("create cutscenesProgress");
                    cutscenesProgress = init(context, title, content, cancelable, listener,progress,total);
                    cutscenesProgress.show();
                } else {
                    if (cutscenesProgress.isShowing()) {
                        cutscenesProgress.setMessage(content);
//                        cutscenesProgress.setProgress(progress,total);
                        return;
                    } else {
                        cutscenesProgress.show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示加载dialog
     *
     * @param title     标题
     * @param content   内容
     * @param canCancel 是否可以取消显示
     * @param listener  取消显示后的监听
     */
    public static void showCutscenes(String title, String content, boolean canCancel, DialogInterface.OnCancelListener listener) {
        Context context = ActivityManager.peek();
        if (null != context) {
            if (null == cutscenesProgress) {
                cutscenesProgress = init(context, title, content, canCancel, listener);
                cutscenesProgress.show();
            } else {
                if (cutscenesProgress.isShowing()) {
                    return;
                } else {
                    cutscenesProgress.show();
                }
            }
        }
    }

    /**
     * 显示加载dialog
     *
     * @param listener 取消显示后的监听
     */
    public static void showCutscenes(Context context, DialogInterface.OnCancelListener listener) {
        if (null != context) {
            if (null == cutscenesProgress) {
                cutscenesProgress = init(context, "", "", true, listener);
                cutscenesProgress.show();
            } else {
                if (cutscenesProgress.isShowing()) {
                    return;
                } else {
                    cutscenesProgress.show();
                }
            }
        }
    }

    /**
     * 隐藏加载dialog
     */
    public static void hideCutscenes() {
        if (null != cutscenesProgress && cutscenesProgress.isShowing() && automatic) {
            cutscenesProgress.hide();
        }
    }

    /**
     * 释放加载dialog
     */
    public static void dismissCutscenes() {
        Log.d("dismissCutscenes", "dismissCutscenes");
        if (null != cutscenesProgress && automatic) {
            Log.d("automatic", "dismissCutscenes");
            cutscenesProgress.dismiss();

        }
    }

    /**
     * 释放加载dialog
     */
    public static void dismissCutscenes(boolean automatic) {
        NetworkUtil.automatic = automatic;
        dismissCutscenes();
    }

    public static void showLoading() {
        CutscenesProgress dialog = CutscenesProgress.createDialog(ActivityManager.peek());
        dialog.setTitle("");
        dialog.setMessage("");
        dialog.setCancelable(true);
    }
}
