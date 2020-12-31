package com.qidian.kuaitui.base;

import android.content.Context;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.erongdu.wireless.tools.log.MyLog;


public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        MyLog.init(true,"xiong",false,1,2);

    }


    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
