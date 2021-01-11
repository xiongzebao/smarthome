package com.ihome.base.network.api;



import com.ihome.base.model.NoticeListRec;

import retrofit2.Call;
import retrofit2.http.GET;


/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/11/17 15:44
 * <p/>
 * Description: 用户接口
 * (@Url: 不要以 / 开头)
 */
public interface HomeService {

    /**
     * 获取公告列表   /
     */
    @GET("AppUserCenter/GetNotices")
    Call<NoticeListRec> getNotices();



}
