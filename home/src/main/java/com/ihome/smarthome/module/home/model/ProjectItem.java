package com.ihome.smarthome.module.home.model;

import com.ihome.base.views.SelectRecyclerView;

/**
 * @author xiongbin
 * @description:
 * @date : 2020/12/29 17:31
 */

public class ProjectItem extends SelectRecyclerView.BaseModel {



    /**
     * SiteRecruitId : a245a58f-16e6-455c-a9ae-e22eb8a8e690
     * SiteStaffId : af660497-22d4-475a-803a-1f627af95799
     * RecruitID : 07a7aaa6-5071-4113-8a17-7d5ccb7a374b
     * CreateDate : 2020-11-20 15:31:42
     * SiteStaffName : 张三
     * SiteStaffPhone : 15307169100
     * FunctionName : 业务员
     * CompanyName : 武汉科诺生物科技股份有限公司
     * ProjectName : 武汉科诺生物科技股份有限公司劳务派遣项目
     * PostType : 全职
     * ProvinceName : 湖北省
     * CityName : 武汉市
     * WorkPlace : 武汉市东西湖区金银湖路18号财富大厦9楼
     */

    private String SiteRecruitId;
    private String SiteStaffId;
    private String RecruitID;
    private String CreateDate;
    private String SiteStaffName;
    private String SiteStaffPhone;
    private String FunctionName;
    private String CompanyName;
    private String ProjectName;
    private String PostType;
    private String ProvinceName;
    private String CityName;
    private String WorkPlace;



    public String getSiteRecruitId() {
        return SiteRecruitId;
    }

    public void setSiteRecruitId(String SiteRecruitId) {
        this.SiteRecruitId = SiteRecruitId;
    }

    public String getSiteStaffId() {
        return SiteStaffId;
    }

    public void setSiteStaffId(String SiteStaffId) {
        this.SiteStaffId = SiteStaffId;
    }

    public String getRecruitID() {
        return RecruitID;
    }

    public void setRecruitID(String RecruitID) {
        this.RecruitID = RecruitID;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String CreateDate) {
        this.CreateDate = CreateDate;
    }

    public String getSiteStaffName() {
        return SiteStaffName;
    }

    public void setSiteStaffName(String SiteStaffName) {
        this.SiteStaffName = SiteStaffName;
    }

    public String getSiteStaffPhone() {
        return SiteStaffPhone;
    }

    public void setSiteStaffPhone(String SiteStaffPhone) {
        this.SiteStaffPhone = SiteStaffPhone;
    }

    public String getFunctionName() {
        return FunctionName;
    }

    public void setFunctionName(String FunctionName) {
        this.FunctionName = FunctionName;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String CompanyName) {
        this.CompanyName = CompanyName;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String ProjectName) {
        this.ProjectName = ProjectName;
    }

    public String getPostType() {
        return PostType;
    }

    public void setPostType(String PostType) {
        this.PostType = PostType;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String ProvinceName) {
        this.ProvinceName = ProvinceName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
    }

    public String getWorkPlace() {
        return WorkPlace;
    }

    public void setWorkPlace(String WorkPlace) {
        this.WorkPlace = WorkPlace;}

    @Override
    protected String getText() {
        return getProjectName();
    }
}
