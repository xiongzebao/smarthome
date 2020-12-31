package com.erongdu.wireless.tools.log;

import androidx.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

public class MyLog {

    public static void init(final boolean isLoggable,String tag,boolean showThreadInfo,int methodCount,int methodOffset){

        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(showThreadInfo)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(methodCount)         // (Optional) How many method line to show. Default 2
                .methodOffset(methodOffset)        // (Optional) Hides internal method calls up to offset. Default 5

                .tag(tag)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy){
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return  isLoggable;
            }
        });
    }

    public static void d(Object o){
        Logger.d(o);
    }

    public static void d(String format_message,Object ...args){
        Logger.d(format_message,args);
    }

    public static void e(String format_message,Object ...args){
        Logger.e(format_message,args);
    }

    public static void e(Throwable throwable,String format_message,Object ...args){
        Logger.e(throwable,format_message,args);
    }

    public static void json(String json){
        Logger.json(json);
    }

    public static void w(String msg){
        Logger.w(msg);
    }

    public static void e(String msg){
        Logger.e(msg);
    }


    public static void i(String msg){
        Logger.i(msg);
    }


}
