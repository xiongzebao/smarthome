package com.ihome.smarthome.utils;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/19 9:29
 */

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ActivityManager;
import com.erongdu.wireless.tools.utils.PackageUtils;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.ihome.base.utils.DialogUtils;
import com.ihome.base.utils.TTS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 系统播报类,部分手机不支持中文播报
 */
@SuppressLint("NewApi")
public class SystemTTSUtils extends UtteranceProgressListener implements TTS, TextToSpeech.OnUtteranceCompletedListener {
    private Context mContext;
    private static SystemTTSUtils singleton;
    private TextToSpeech textToSpeech; // 系统语音播报类
    private boolean isSuccess = true;


    
    public static SystemTTSUtils getInstance(Context context) {


        if (singleton == null) {
            installiFlyEngin(context);
            synchronized (SystemTTSUtils.class) {
                if (singleton == null) {
                    singleton = new SystemTTSUtils(context);
                }
            }
        }
        return singleton;
    }

    public  static    void installiFlyEngin(Context context){
        if(!PackageUtils.isAppInstalled(context,"com.iflytek.speechcloud")){
            //EventBusUtils.sendFailLog("未安装讯飞引擎");

            DialogUtils.showDialog(context,"您未安装讯飞语音引擎，请安装讯飞语音引擎",new SweetAlertDialog.OnSweetClickListener(){
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    Uri uri = copyAssetsFile(context,"ifly_engine.mp3",isExistDir("bigstage"));
                    openApk(uri,context);

                }
            });
        }else{
          //  EventBusUtils.sendSucessLog("讯飞引擎已安装");
        }
    }

    private static void installApk(Context context ){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri apkUri = copyAssetsFile(context,"ifly_engine.mp3",isExistDir("bigstage"));

        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
           // Uri apkUri = FileProvider.getUriForFile(context, "com.ihome.smarthome.fileprovider", file);

            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {


            intent.setDataAndType(Uri.fromFile( new File( apkUri.getPath())), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }


    public static void openApk(Uri uri, Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= 24) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    private static String isExistDir(String saveDir)   {
        // 下载位置
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
            if (!downloadFile.mkdirs()) {
                try {
                    downloadFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    MyLog.e("isExistDir createNewFile failed");
                }
            }
            String savePath = downloadFile.getAbsolutePath();
            Log.e("savePath", savePath);
            return savePath;
        }
        return null;
    }




    public static Uri copyAssetsFile(Context context, String fileName, String path) {

        InputStream mInputStream = null;
        try {
            mInputStream = context.getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            MyLog.e("context.getAssets().open failed:"+e.getMessage());
        }
        File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            File mFile = new File(path + File.separator + "test.apk");
            if (!mFile.exists()) {
                try {
                    mFile.createNewFile();
                } catch (IOException e) {
                     MyLog.e("createNewFile failed:"+e.getMessage());
                }
            }
            MyLog.e("开始拷贝");
        FileOutputStream mFileOutputStream = null;
        try {
            mFileOutputStream = new FileOutputStream(mFile);
        } catch (FileNotFoundException e) {
            ToastUtil.toast("new FileOutputStream failed:"+e.getMessage());
            MyLog.e("new FileOutputStream failed:"+e.getMessage());
        }
        byte[] mbyte = new byte[1024];
            int i = 0;
            while (true) {
                try {
                    if (!((i = mInputStream.read(mbyte)) > 0)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                    ToastUtil.toast(" mInputStream.read failed:"+e.getMessage());
                    MyLog.e(" mInputStream.read failed:"+e.getMessage());
                }
                try {
                    mFileOutputStream.write(mbyte, 0, i);
                } catch (IOException e) {
                    e.printStackTrace();
                    ToastUtil.toast("mFileOutputStream.write failed:"+e.getMessage());
                    MyLog.e(" mFileOutputStream.write failed:"+e.getMessage());
                }
            }
        try {
            mInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.toast(" mInputStream.close failed:"+e.getMessage());
            MyLog.e(" mInputStream.close failed:"+e.getMessage());
        }
        try {
            mFileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.toast(" mInputStream.close failed:"+e.getMessage());
            MyLog.e(" mInputStream.close failed:"+e.getMessage());
        }
        Uri uri = null;
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //包名.fileprovider
                    uri = FileProvider.getUriForFile(context, "com.ihome.smarthome.fileprovider", mFile);
                } else {
                    uri = Uri.fromFile(mFile);
                }
            } catch (ActivityNotFoundException anfe) {
                MyLog.e( anfe.getMessage());
            }
            MediaScannerConnection.scanFile(context, new String[]{mFile.getAbsolutePath()}, null, null);
            MyLog.e( "拷贝完毕：" + uri);
            return uri;

    }



    private SystemTTSUtils(Context context) {
        this.mContext = context.getApplicationContext();


        textToSpeech = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                //系统语音初始化成功

                if (i == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.CHINA);
                    textToSpeech.setPitch(100.5f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
                    textToSpeech.setSpeechRate(1.0f);
                    textToSpeech.setOnUtteranceProgressListener(SystemTTSUtils.this);
                    textToSpeech.setOnUtteranceCompletedListener(SystemTTSUtils.this);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        //系统不支持中文播报
                        isSuccess = false;
                        ToastUtil.toast("系统不支持中文播报");
                    }
                    ToastUtil.toast("TextToSpeech init success");
                }else{
                    ToastUtil.toast("init failed");
                }

            }
        },"com.iflytek.speechcloud");

    }

    public void playText(String playText) {
        if (!isSuccess) {
            return;
        }
        if (textToSpeech != null) {
            textToSpeech.speak(playText,
                    TextToSpeech.QUEUE_ADD, null, null);
        }
    }

    public void stopSpeak() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }


    //播报完成回调
    @Override
    public void onUtteranceCompleted(String utteranceId) {
    }

    @Override
    public void onStart(String utteranceId) {

    }

    @Override
    public void onDone(String utteranceId) {
    }

    @Override
    public void onError(String utteranceId) {
        ToastUtil.toast(utteranceId);

    }
}