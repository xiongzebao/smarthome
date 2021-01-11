package com.ihome.base.network;


import com.erongdu.wireless.network.interceptor.BasicParamsInterceptor;
import com.erongdu.wireless.network.interceptor.IBasicDynamic;
import com.erongdu.wireless.tools.utils.ContextHolder;
import com.ihome.base.common.BaseParams;
import com.ihome.base.common.Constant;
import com.ihome.base.model.OauthTokenMo;
import com.ihome.base.utils.DeviceInfoUtils;
import com.ihome.base.utils.SharedInfo;


import java.util.Map;
import java.util.TreeMap;

import okhttp3.Interceptor;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/4/5 17:59
 * <p/>
 * Description: 拦截器 - 用于添加签名参数
 */
public class BasicParamsInject {
    private BasicParamsInterceptor interceptor;

    BasicParamsInject() {
        // 设置静态参数
        if(SharedInfo.getInstance().getEntity(OauthTokenMo.class)==null){
            interceptor = new BasicParamsInterceptor.Builder()
//                .addBodyParam(Constant.APP_KEY, BaseParams.APP_KEY)
                    .addBodyParam(Constant.MOBILE_TYPE, BaseParams.MOBILE_TYPE)
                    .addBodyParam(Constant.VERSION_NUMBER, DeviceInfoUtils.getVersionName(ContextHolder.getContext()))
                    .build();
        }else{
            interceptor = new BasicParamsInterceptor.Builder()
//                .addBodyParam(Constant.APP_KEY, BaseParams.APP_KEY)
                    .addBodyParam(Constant.MOBILE_TYPE, BaseParams.MOBILE_TYPE)
                    .addBodyParam(Constant.VERSION_NUMBER, DeviceInfoUtils.getVersionName(ContextHolder.getContext()))
                    .addBodyParam("ticket", SharedInfo.getInstance().getEntity(OauthTokenMo.class).getTicket())
                    .build();
        }

        // 设置动态参数
        interceptor.setIBasicDynamic(new IBasicDynamic() {
            @Override
            public String signParams(String postBodyString) {
                //post提交动态添加参数
                return UrlUtils.getInstance().dynamicParams(postBodyString);
            }

            @Override
            public Map signParams(Map map) {
                //get提交动态添加参数
                TreeMap temp = new TreeMap(map);
                return UrlUtils.getInstance().dynamicParams(temp);
            }

            @Override
            public Map signHeadParams(Map headMap) {
                return UrlUtils.getInstance().signParams(headMap);
            }
        });
    }

    Interceptor getInterceptor() {
        return interceptor;
    }
}
