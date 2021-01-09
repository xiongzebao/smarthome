package com.qidian.kuaitui.base;

import android.app.Application;
import android.content.Context;



import com.erongdu.wireless.tools.log.MyLog;
import com.qidian.kuaitui.MainActivity;


public class MyApplication extends Application {

   public static MainActivity mainActivity;
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        MyLog.init(KTAppConfig.isDebug,"xiong",false,1,2);

    }


    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
      //  MultiDex.install(this);

    }

}
