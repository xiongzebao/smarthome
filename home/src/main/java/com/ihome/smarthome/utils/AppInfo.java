package com.ihome.smarthome.utils;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/26 10:05
 */



import android.graphics.drawable.Drawable;

public class AppInfo {
    //应用图标
    private Drawable icon;
    //应用的名字
    private String appName;
    //应用程序的大小
    private String apkSize;
    //表示用户程序
    private boolean isUserApp;
    //存储的位置.
    private boolean isSD;
    private String packageName;
    public String getPackageName() {
        return packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public Drawable getIcon() {
        return icon;
    }
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
    public String getAppName() {
        return appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public String getApkSize() {
        return apkSize;
    }
    public void setApkSize(String apkSize) {
        this.apkSize = apkSize;
    }
    public boolean isUserApp() {
        return isUserApp;
    }
    public void setUserApp(boolean isUserApp) {
        this.isUserApp = isUserApp;
    }
    public boolean isSD() {
        return isSD;
    }
    public void setSD(boolean isSD) {
        this.isSD = isSD;
    }
    @Override
    public String toString() {
        return "AppInfo{" +
                "appName='" + appName + '\'' +
                ", apkSize='" + apkSize + '\'' +
                ", isUserApp=" + isUserApp +
                ", isSD=" + isSD +
                '}';
    }
}