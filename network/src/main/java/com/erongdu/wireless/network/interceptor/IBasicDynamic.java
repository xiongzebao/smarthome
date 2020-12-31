package com.erongdu.wireless.network.interceptor;

import java.util.Map;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/5/17 13:44
 * <p/>
 * Description: 添加动态参数接口
 */
public interface IBasicDynamic {
    String signParams(String postBodyString);
    Map signParams(Map map);
    Map signHeadParams(Map headMap);
}
