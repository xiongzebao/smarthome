package com.ihome.smarthome.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


import com.cc.library.BaseSmartDialog;
import com.cc.library.BindViewListener;
import com.cc.library.SmartDialog;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.erongdu.wireless.views.NoDoubleClickButton;
import com.ihome.base.R;
import com.ihome.base.model.VersionNewRec;
import com.ihome.base.network.NetworkUtil;
import com.ihome.base.network.RDClient;
import com.ihome.base.network.RequestCallBack;
import com.ihome.base.network.api.UserService;
import com.ihome.base.utils.DeviceInfoUtils;
import com.ihome.base.utils.DownloadUtil;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Response;

import static androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale;

public class AppUpdateUtils {
    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 100;

    private static final int REQUEST_CODE_APP_INSTALL = 2333;
    private static final int REQUEST_SYSTEM_ALERT_WINDOW = 0114;
    boolean isLoad;


    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isHasInstallPermissionWithO(Context context) {
        if (context == null) {
            return false;
        }
        return context.getPackageManager().canRequestPackageInstalls();
    }


    /**
     * 开启设置安装未知来源应用权限界面
     *
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity(final Activity context) {
        if (context == null) {
            return;
        }
        ToastUtil.toast("请打开应用安装权限");
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                context.startActivityForResult(intent, REQUEST_CODE_APP_INSTALL);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 2000);//2秒后执行TimeTask的run方法
    }

    private void checkUpdate(final Activity act, String forceUpdate, String remark, String url) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(act)) {
                //若未授权则请求权限
                ToastUtil.toast("请打开应用显示或悬浮窗权限");
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        intent.setData(Uri.parse("package:" + act.getPackageName()));
                        act.startActivityForResult(intent, REQUEST_SYSTEM_ALERT_WINDOW);
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 2000);//2秒后执行TimeTask的run方法
            } else {
                if (isLoad) {
                    checkAppUpdate(act);
                    updateJudge(act, forceUpdate, remark, url);
                    isLoad = false;
                }
            }
        } else {
            if (isLoad) {
                checkAppUpdate(act);
                updateJudge(act, forceUpdate, remark, url);
                isLoad = false;
            }
        }
    }


    private void updateJudge(Activity act, String forceUpdate, String remark, String url) {
        if ("0".equals(forceUpdate)) {
            showUpdateDialog(act, remark, url, false);
        } else if ("1".equals(forceUpdate)) {
            showUpdateDialog(act, remark, url, true);
        }
    }




    private void checkAppUpdate(final Activity context) {

        Call<VersionNewRec> call = RDClient.getService(UserService.class).getAppUpdate("0");  //0安卓 1苹果
        call.enqueue(new RequestCallBack<VersionNewRec>() {
            @Override
            public void onSuccess(Call<VersionNewRec> call, Response<VersionNewRec> response) {
                VersionNewRec.ResultdataBean data = response.body().getResultdata();
                if (data.getVersionName() != null) {
                    String url = data.getUpdateUrl();
                    int versionCode = data.getVersionCode();
                    String remark = data.getRemark();
                    String forceUpdate = data.getForceUpdate();

                    if (versionCode > DeviceInfoUtils.getVersionCode(context)) {
                        //todo 2.需要更新的时候，再来做权限校验
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //26
                            boolean hasInstallPermission = isHasInstallPermissionWithO(context);
                            if (!hasInstallPermission) {
                                startInstallPermissionSettingActivity(context);
                                return;
                            } else {
                                checkUpdate(context, forceUpdate, remark, url);
                            }
                        } else {
                            checkUpdate(context, forceUpdate, remark, url);
                        }
                    } else {
                        // somethingToDo();
                    }
                }
            }
        });
    }


    private static void upDateAndInstall(final Activity context, String url) {
        DownloadUtil.get().download(url, "bigstage", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(final File file) {
                NetworkUtil.dismissCutscenes();
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.toast("下载完成");
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
                            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                            Uri apkUri = FileProvider.getUriForFile(context, "com.qd768626281.ybs.fileprovider", file);
                            //添加这一句表示对目标应用临时授权该Uri所代表的文件
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                        } else {
                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                        }
                        context.startActivity(intent);

                    }
                });
            }

            @Override
            public void onDownloading(final int progress, final long total) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NetworkUtil.showCutscenes("", "下载中(" + progress + "%)...", progress, 100, false);
                        if (progress == 100) {
                            NetworkUtil.dismissCutscenes();
                        }
                    }
                });
            }

            @Override
            public void onDownloadFailed() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NetworkUtil.dismissCutscenes();
                        ToastUtil.toast("下载失败,请到应用商城更新。");
                    }
                });
            }
        });
    }


    private static void doUpdate(Activity context, String url) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                }
                context.requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                upDateAndInstall(context, url);
            }
        } else {
            upDateAndInstall(context, url);
        }
    }








    protected static void showUpdateDialog(final Activity context, final String remark, final String url, final boolean force) {
        new SmartDialog().init(context).layoutRes(R.layout.update_pop)
                // 为自定义布局的子控件设监听
                .bindViewListener(new BindViewListener() {
                    @Override
                    public void bind(View dialogView, final BaseSmartDialog dialog) {
                        TextView tv1 = (TextView) dialogView.findViewById(R.id.tv1);
                        tv1.setText(remark);

                        NoDoubleClickButton bt1 = (NoDoubleClickButton) dialogView.findViewById(R.id.bt1);
                        if (force) {
                            bt1.setVisibility(View.GONE);
                        } else {
                            bt1.setVisibility(View.VISIBLE);
                        }

                        dialogView.findViewById(R.id.bt1).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                // somethingToDo();
                            }
                        });
                        dialogView.findViewById(R.id.bt2).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                doUpdate(context, url);
                            }
                        });
                    }
                })
                .padding(60)
                .animEnable(false)
                .cancelableOutside(false)
                .gravity(Gravity.CENTER)
                .animDuration(400)
                .display()
                .setCancelable(false);//不允许物理返回键退出
    }







}
