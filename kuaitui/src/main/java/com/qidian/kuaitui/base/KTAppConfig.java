package com.qidian.kuaitui.base;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/9 13:31
 */

public class KTAppConfig {
    public static boolean isDebug = true;
    public static HOST_TYPE host_type = HOST_TYPE.TEST;

    public static enum HOST_TYPE{TEST,PRODUCT}
    public static String getHost(){
        if(host_type==HOST_TYPE.PRODUCT){
            return "";
        }
        if(host_type==HOST_TYPE.TEST){
            return "http://ktzcapi.qidiandev.cn";
        }

        return "";
    }

}
