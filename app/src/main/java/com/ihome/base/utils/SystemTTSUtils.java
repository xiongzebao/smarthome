package com.ihome.base.utils;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/19 9:29
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import com.erongdu.wireless.tools.utils.ToastUtil;

import java.util.Locale;

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
            synchronized (SystemTTS.class) {
                if (singleton == null) {
                    singleton = new SystemTTSUtils(context);
                }
            }
        }
        return singleton;
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
                    }
                }else{
                    ToastUtil.toast("init failed");
                }

            }
        },"com.iflytek.speechcloud");
        // c o m . i f l y t e k . s p e e c h c l o u d
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