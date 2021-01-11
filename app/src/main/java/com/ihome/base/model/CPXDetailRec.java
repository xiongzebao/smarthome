package com.ihome.base.model;

import java.util.List;

public class CPXDetailRec {

    /**
     * type : 1
     * errorcode : 0
     * message : 获取成功
     * resultdata : [{"id":"bd62d649-baff-4b5b-97da-e970fe6a622e","entryname":"20201109新经济项目","customername":"20201109新经济客户","signingtime":"2020-11-09 00:00:00","followstatus":0,"customertype":"2","companyname":"起点人力","linkman":"韩非","linkway":"15872434210","scale":"大于500人","isfinished":0,"projecttype":2,"state":0,"createdate":"2020-11-17 13:54:57","activityname":"产品线审批","industryname":"新经济行业","producttypename":"招聘与灵活用工"}]
     */

    private int type;
    private int errorcode;
    private String message;
    private List<ResultdataBean> resultdata;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResultdataBean> getResultdata() {
        return resultdata;
    }

    public void setResultdata(List<ResultdataBean> resultdata) {
        this.resultdata = resultdata;
    }

    public static class ResultdataBean {
        /**
         * id : bd62d649-baff-4b5b-97da-e970fe6a622e
         * entryname : 20201109新经济项目
         * customername : 20201109新经济客户
         * signingtime : 2020-11-09 00:00:00
         * followstatus : 0
         * customertype : 2
         * companyname : 起点人力
         * linkman : 韩非
         * linkway : 15872434210
         * scale : 大于500人
         * isfinished : 0
         * projecttype : 2
         * state : 0
         * createdate : 2020-11-17 13:54:57
         * activityname : 产品线审批
         * industryname : 新经济行业
         * producttypename : 招聘与灵活用工
         */

        private String id;
        private String entryname;
        private String customername;
        private String signingtime;
        private int followstatus;
        private String customertype;
        private String companyname;
        private String linkman;
        private String linkway;
        private String scale;
        private int isfinished;
        private Integer projecttype;
        private int state;
        private String createdate;
        private String activityname;
        private String industryname;
        private String producttypename;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEntryname() {
            return entryname;
        }

        public void setEntryname(String entryname) {
            this.entryname = entryname;
        }

        public String getCustomername() {
            return customername;
        }

        public void setCustomername(String customername) {
            this.customername = customername;
        }

        public String getSigningtime() {
            return signingtime;
        }

        public void setSigningtime(String signingtime) {
            this.signingtime = signingtime;
        }

        public int getFollowstatus() {
            return followstatus;
        }

        public void setFollowstatus(int followstatus) {
            this.followstatus = followstatus;
        }

        public String getCustomertype() {
            return customertype;
        }

        public void setCustomertype(String customertype) {
            this.customertype = customertype;
        }

        public String getCompanyname() {
            return companyname;
        }

        public void setCompanyname(String companyname) {
            this.companyname = companyname;
        }

        public String getLinkman() {
            return linkman;
        }

        public void setLinkman(String linkman) {
            this.linkman = linkman;
        }

        public String getLinkway() {
            return linkway;
        }

        public void setLinkway(String linkway) {
            this.linkway = linkway;
        }

        public String getScale() {
            return scale;
        }

        public void setScale(String scale) {
            this.scale = scale;
        }

        public int getIsfinished() {
            return isfinished;
        }

        public void setIsfinished(int isfinished) {
            this.isfinished = isfinished;
        }

        public Integer getProjecttype() {
            return projecttype;
        }

        public void setProjecttype(Integer projecttype) {
            this.projecttype = projecttype;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getCreatedate() {
            return createdate;
        }

        public void setCreatedate(String createdate) {
            this.createdate = createdate;
        }

        public String getActivityname() {
            return activityname;
        }

        public void setActivityname(String activityname) {
            this.activityname = activityname;
        }

        public String getIndustryname() {
            return industryname;
        }

        public void setIndustryname(String industryname) {
            this.industryname = industryname;
        }

        public String getProducttypename() {
            return producttypename;
        }

        public void setProducttypename(String producttypename) {
            this.producttypename = producttypename;
        }
    }
}
