package com.ihome.smarthome.utils;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/26 10:05
 */


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.format.Formatter;

import com.erongdu.wireless.tools.log.MyLog;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AppInfoParser {
    private static String tag = "AppInfoParser";
    public static List<AppInfo> getAppInfos(Context context){
        //首先获取到包的管理者
        PackageManager packageManager = context.getPackageManager();
        //获取到所有的安装包
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        ArrayList<AppInfo> appInfos = new ArrayList<>();
        for (PackageInfo installedPackage : installedPackages) {
            AppInfo appInfo = new AppInfo();
            //程序包名
            String packageName = installedPackage.packageName;
            appInfo.setPackageName(packageName);
            //获取到图标
            Drawable icon = installedPackage.applicationInfo.loadIcon(packageManager);
            appInfo.setIcon(icon);
            //获取到应用的名字
            String appName = installedPackage.applicationInfo.loadLabel(packageManager).toString();
            appInfo.setAppName(appName);
            //获取到安装包的路径
            String sourceDir = installedPackage.applicationInfo.sourceDir;
            File file = new File(sourceDir);
            //获取到安装apk的大小
            long apkSize = file.length();
            //格式化apk的大小
            appInfo.setApkSize(Formatter.formatFileSize(context,apkSize));
            int flags = installedPackage.applicationInfo.flags;
            //判断当前是否是系统app
            if((flags & ApplicationInfo.FLAG_SYSTEM) !=0){
                //那么就是系统app
                appInfo.setUserApp(false);
            }else{
                //那么就是用户app
                appInfo.setUserApp(true);
            }
            if((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE)!=0){
                //那么当前安装的就是sd卡
                appInfo.setSD(true);
            }else{
                //那么就是手机内存
                appInfo.setSD(false);
            }
            appInfos.add(appInfo);
        }

        MyLog.d(appInfos.toString());
        return appInfos;
    }
}