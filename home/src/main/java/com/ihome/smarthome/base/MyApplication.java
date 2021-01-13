package com.ihome.smarthome.base;

import android.app.Application;
import android.content.Context;



import com.erongdu.wireless.tools.log.MyLog;
import com.ihome.smarthome.MainActivity;


public class MyApplication extends Application {

   public static MainActivity mainActivity;
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        MyLog.init(KTAppConfig.isDebug,"xiong",false,2,3);

    }


    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
      //  MultiDex.install(this);

    }

}
