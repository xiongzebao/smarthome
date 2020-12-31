package com.qidian.base.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/4/27 17:26
 * <p/>
 * Description: 手机设备信息手机工具类
 */
public class DeviceInfoUtils {
    /**
     * 获得IMEI号
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            String imei = tm.getDeviceId() != null ? tm.getDeviceId() : "";
            if (imei.equals("0")) {
                imei = "000000000000000";
            }
            int len = 15 - imei.length();
            for (int i = 0; i < len; i++) {
                imei += "0";
            }
            return imei;
        }
        return "";
    }

    /**
     * 获取IP地址
     */
    public static String getIP(Context context) {
        int    WIFI_IP = getWIFIIP(context);
        String GPRS_IP = getGPRSIP();
        String ip      = "0.0.0.0";
        if (WIFI_IP != 0) {
            ip = intToIP(WIFI_IP);
        } else if (!TextUtils.isEmpty(GPRS_IP)) {
            ip = GPRS_IP;
        }
        return ip;
    }

    /**
     * 返回当前版本号名称，例如：V1.0
     */
    public static String getVersionName(Context context) {
        // 默认为1.0
        String ret_val = "V1.0";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo    info    = manager.getPackageInfo(context.getPackageName(), 0);
            ret_val = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret_val;
    }

    /**
     * 返回当前版本号的值，例如：1
     */
    public static int getVersionCode(Context context) {
        // 默认为1
        int ret_val = 1;
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo    info    = manager.getPackageInfo(context.getPackageName(), 0);
            ret_val = info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret_val;
    }

    /**
     * 获取AndroidManifest.xml中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值, 或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context context, String key) {
        if (context == null || TextUtils.isEmpty(key)) {
            return "";
        }
        String resultData = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        if (applicationInfo.metaData.get(key) != null) {
                            resultData = applicationInfo.metaData.get(key).toString();
                        }
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resultData;
    }

    /**
     * 获得wifi的IP地址
     */
    private static int getWIFIIP(Context context) {
        // 获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        // 判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getIpAddress();
    }


    /**
     * 整型IP地址转成String的
     */
    private static String intToIP(int IPAddress) {
        return (IPAddress & 0xFF) + "." + ((IPAddress >> 8) & 0xFF) + "." + ((IPAddress >> 16) & 0xFF) + "." + (IPAddress >> 24 & 0xFF);
    }

    /**
     * 获取数据网络的IP地址
     */
    private static String getGPRSIP() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> addresses = networkInterface.getInetAddresses(); addresses.hasMoreElements(); ) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String[] platforms = {
            "http://pv.sohu.com/cityjson",
            "http://pv.sohu.com/cityjson?ie=utf-8",
            "http://ip.chinaz.com/getip.aspx"
    };

    /**
     * 请求公网ip地址获取本机公网ip地址
     *
     * @param context 上下文
     * @param index   公网ip地址列表中的index
     * @return        本机公网ip地址
     */
    public static String getOutNetIP(Context context, int index) {
        if (index < platforms.length) {
            BufferedReader buff = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(platforms[index]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(5000);//读取超时
                urlConnection.setConnectTimeout(5000);//连接超时
                urlConnection.setDoInput(true);
                urlConnection.setUseCaches(false);

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {//找到服务器的情况下,可能还会找到别的网站返回html格式的数据
                    InputStream is = urlConnection.getInputStream();
                    buff = new BufferedReader(new InputStreamReader(is, "UTF-8"));//注意编码，会出现乱码
                    StringBuilder builder = new StringBuilder();
                    String line = null;
                    while ((line = buff.readLine()) != null) {
                        builder.append(line);
                    }

                    buff.close();//内部会关闭 InputStream
                    urlConnection.disconnect();

                    Log.e("xiaoman", builder.toString());
                    if (index == 0 || index == 1) {
                        //截取字符串
                        int satrtIndex = builder.indexOf("{");//包含[
                        int endIndex = builder.indexOf("}");//包含]
                        String json = builder.substring(satrtIndex, endIndex + 1);//包含[satrtIndex,endIndex)
                        JSONObject jo = new JSONObject(json);
                        String ip = jo.getString("cip");

                        return ip;
                    } else if (index == 2) {
                        JSONObject jo = new JSONObject(builder.toString());
                        return jo.getString("ip");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return getIP(context);
        }
        return getOutNetIP(context, ++index);
    }
}
