package com.erongdu.wireless.network.exception;

import com.erongdu.wireless.network.entity.HttpResult;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/4/5 11:06
 * <p/>
 * Description: HTTP请求异常类
 */
@SuppressWarnings("unused")
public class ApiException extends RuntimeException {
    private HttpResult result;

    public ApiException(HttpResult result) {
        this.result = result;
    }

    public HttpResult getResult() {
        return result;
    }

    public void setResult(HttpResult result) {
        this.result = result;
    }
}
