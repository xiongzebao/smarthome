package com.ihome.base.network.api;

import com.ihome.base.model.VersionNewRec;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/11/17 15:44
 * <p/>
 * Description: 用户接口
 * (@Url: 不要以 / 开头)
 */
public interface UserService {

    /**
     * 获取app信息
     */
    @GET("AppIndex/GetAppUpdate")
    Call<VersionNewRec> getAppUpdate(@Query("type") String type);










}
