package com.erongdu.wireless.network.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/4/5 15:23
 * <p/>
 * Description: 网络返回消息，最外层解析实体
 */
@SuppressWarnings("unused")
public class HttpResult<T> {
//    /** 错误码 */
//    @SerializedName(Params.RES_CODE)
//    private int    code;
//    /** 错误信息 */
//    @SerializedName(Params.RES_MSG)
//    private String msg;
//    /** 消息响应的主体 */
//    @SerializedName(Params.RES_DATA)
//    private T      data;
////    @SerializedName(Params.RES_PAGE)
//    private PageMo page;
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public T getData() {
//        return data;
//    }
//
//    public void setData(T data) {
//        this.data = data;
//    }
//
//    public PageMo getPage() {
//        return page;
//    }
//
//    public void setPage(PageMo page) {
//        this.page = page;
//    }

    private int    type;
    /** 错误码 */
    private int    errorcode;
    /** 错误信息 */
    private String message;
    /** 消息响应的主体 */
    private T      resultdata;
    //    @SerializedName(Params.RES_PAGE)
    private PageMo page;

    public int getType() {
        return type;
    }

    public int getCode() {
        return errorcode;
    }

    public String getMsg() {
        return message;
    }

    public T getData() {
        return resultdata;
    }

    public PageMo getPage() {
        return page;
    }
}
