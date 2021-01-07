package com.qidian.kuaitui.api;



import com.qidian.kuaitui.base.Page;
import com.qidian.kuaitui.base.ResBase;
import com.qidian.kuaitui.module.home.model.ProjectItem;
import com.qidian.kuaitui.module.job.model.AddNewUserModel;
import com.qidian.kuaitui.module.job.model.ChannelBean;
import com.qidian.kuaitui.module.job.model.ReceiptListBean;
import com.qidian.kuaitui.module.job.model.ReqInterViewParam;
import com.qidian.kuaitui.module.main.model.DataShowBean;
import com.qidian.kuaitui.module.main.model.HomeStaticBean;
import com.qidian.kuaitui.module.main.model.JobHomeBean;
import com.qidian.kuaitui.module.mine.model.ExceptionBean;
import com.qidian.kuaitui.module.mine.model.ExceptionModel;
import com.qidian.kuaitui.module.mine.model.FeedBackModel;
import com.qidian.kuaitui.module.mine.model.LoginBean;
import com.qidian.kuaitui.module.mine.model.UserInfoBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
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

    @GET("/Api/OnSite/GetLogin")
    Call<ResBase<LoginBean>> login(@Query("name") String name, @Query("password") String password);

    @GET("/Api/OnSite/GetUserInfo")
    Call<ResBase<UserInfoBean>> getUserInfo();

    @POST("/Api/OnSite/SearchRecruitList")
    Call<ResBase<List<ProjectItem>>> getProjectList(@Body Page page);

    @GET("/Api/OnSite/HomePageStatisticsInfo")
    Call<ResBase<HomeStaticBean>> getHomePageStatics(@Query("recruitId") String recruitId);

    //DataShowActivity
    @GET("/Api/OnSite/HomePageReceptionInfo")
    Call<ResBase<DataShowBean>> getReceptionInfo(@Query("recruitId") String recruitId);

    @GET("/Api/OnSite/HomePageInterviewInfo")
    Call<ResBase<DataShowBean>> getInterviewInfo(@Query("recruitId") String recruitId);


    @GET("/Api/OnSite/HomePageEntryInfo")
    Call<ResBase<DataShowBean>> getEntryInfo(@Query("recruitId") String recruitId);


    @GET("/Api/OnSite/HomePageLeaveInfo")
    Call<ResBase<DataShowBean>> getLeaveInfo(@Query("recruitId") String recruitId);

    //在职页面数据统计
    @GET("/Api/OnSite/HomePageJobDataSumInfo")
    Call<ResBase<JobHomeBean>> getJobDataSumInfo(@Query("recruitId") String recruitId);

    //面试列表
    @POST("/Api/OnSite/SearchInterviewList")
    Call<ResBase< List<ReceiptListBean>>> getInterViewList(@Body ReqInterViewParam page);




    @POST("/Api/OnSite/HistoryDataInfo")
    Call<ResBase< List<ReceiptListBean>>> getHistoryDataInfo(@Body ReqInterViewParam page);

    @POST("/Api/OnSite/EntryDataInfo")
    Call<ResBase< List<ReceiptListBean>>> getEntryDataInfo(@Body ReqInterViewParam page);

    @POST("/Api/OnSite/CreateJobMember")
    Call<ResBase> addNewUser(@Body AddNewUserModel addNewUserModel);


    @GET("/Api/OnSite/GetChannelDataInfo")
    Call<ResBase<List<ChannelBean>>> getChannel();


    @GET("/Api/OnSite/ModifyReceptionStatus")
    Call<ResBase>modifyReceptionStatus(@Query("interviewId") String interviewId,@Query("status") String status);

    @GET("/Api/OnSite/ModifyLeaveStatus")
    Call<ResBase>modifyLeaveStatus(@Query("interviewId") String interviewId);

    @GET(" /Api/OnSite/ModifyEntryStatus")
    Call<ResBase>modifyEntryStatus(@Query("interviewId") String interviewId,@Query("status") String status);

    @GET("/Api/OnSite/ModifyInterviewStatus")
    Call<ResBase>modifyInterviewStatus(@Query("interviewId") String interviewId,@Query("status") String status);


    @GET("/Api/OnSite/GetAnomalyTypeInfo")
    Call<ResBase<List<ExceptionBean>>> getAnomalyTypeInfo();

    @POST("/Api/OnSite/CreateAnomalyInfo")
    Call<ResBase>createAnomalyInfo(@Body ExceptionModel model);

    @POST("/Api/OnSite/CreateVisitInfo")
    Call<ResBase>createVisitInfo(@Body FeedBackModel model);


    @GET("/Api/OnSite/RichScanInfo")
    Call<ResBase> getUploadMemUserId(@Query("memUserId") String memUserId );
}
