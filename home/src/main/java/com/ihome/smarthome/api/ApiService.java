package com.ihome.smarthome.api;



import com.ihome.smarthome.base.Page;
import com.ihome.smarthome.base.ResBase;




import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/11/17 15:44
 * <p/>
 * Description: 用户接口
 * (@Url: 不要以 / 开头)
 */
public interface ApiService {


    @GET("/Api/OnSite/ModifyReceptionStatus")
    Call<ResBase>modifyReceptionStatus(@Query("interviewId") String interviewId,@Query("status") String status);

    @GET("/Api/OnSite/ModifyLeaveStatus")
    Call<ResBase>modifyLeaveStatus(@Query("interviewId") String interviewId,@Query("leaveDate") String leaveDate);

    @GET(" /Api/OnSite/ModifyEntryStatus")
    Call<ResBase>modifyEntryStatus(@Query("interviewId") String interviewId,@Query("status") String status);

    @GET("/Api/OnSite/ModifyInterviewStatus")
    Call<ResBase>modifyInterviewStatus(@Query("interviewId") String interviewId,@Query("status") String status);



    @GET("/Api/OnSite/RichScanInfo")
    Call<ResBase> getUploadMemUserId(@Query("memUserId") String memUserId );
}
