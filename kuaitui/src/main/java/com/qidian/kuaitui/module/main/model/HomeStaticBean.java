package com.qidian.kuaitui.module.main.model;

import java.util.List;

/**
 * @author xiongbin
 * @description:
 * @date : 2020/12/30 11:24
 */

public class HomeStaticBean {


    /**
     * RecruitID : 07a7aaa6-5071-4113-8a17-7d5ccb7a374b
     * TodayReceptionNum : 0
     * TodayInterviewNum : 0
     * TodayEntryNum : 0
     * TodayLeaveNum : 0
     * ChannelDateList : [{"ChannelSource":"平台","ChannelNum1":"0人","ChannelNum2":"0人","ChannelNum3":"0人","ChannelNum4":"0人"},{"ChannelSource":"农村电商","ChannelNum1":"0人","ChannelNum2":"0人","ChannelNum3":"0人","ChannelNum4":"0人"},{"ChannelSource":"自媒体","ChannelNum1":"0人","ChannelNum2":"0人","ChannelNum3":"0人","ChannelNum4":"0人"},{"ChannelSource":"高校","ChannelNum1":"0人","ChannelNum2":"0人","ChannelNum3":"0人","ChannelNum4":"0人"},{"ChannelSource":"代理","ChannelNum1":"0人","ChannelNum2":"0人","ChannelNum3":"0人","ChannelNum4":"0人"},{"ChannelSource":"融媒体","ChannelNum1":"0人","ChannelNum2":"0人","ChannelNum3":"0人","ChannelNum4":"0人"}]
     */

    private String RecruitID;
    private String TodayReceptionNum;
    private String TodayInterviewNum;
    private String TodayEntryNum;
    private String TodayLeaveNum;
    private List<ChannelDateListBean> ChannelDateList;

    public String getRecruitID() {
        return RecruitID;
    }

    public void setRecruitID(String RecruitID) {
        this.RecruitID = RecruitID;
    }

    public String getTodayReceptionNum() {
        return TodayReceptionNum;
    }

    public void setTodayReceptionNum(String TodayReceptionNum) {
        this.TodayReceptionNum = TodayReceptionNum;
    }

    public String getTodayInterviewNum() {
        return TodayInterviewNum;
    }

    public void setTodayInterviewNum(String TodayInterviewNum) {
        this.TodayInterviewNum = TodayInterviewNum;
    }

    public String getTodayEntryNum() {
        return TodayEntryNum;
    }

    public void setTodayEntryNum(String TodayEntryNum) {
        this.TodayEntryNum = TodayEntryNum;
    }

    public String getTodayLeaveNum() {
        return TodayLeaveNum;
    }

    public void setTodayLeaveNum(String TodayLeaveNum) {
        this.TodayLeaveNum = TodayLeaveNum;
    }

    public List<ChannelDateListBean> getChannelDateList() {
        return ChannelDateList;
    }

    public void setChannelDateList(List<ChannelDateListBean> ChannelDateList) {
        this.ChannelDateList = ChannelDateList;
    }

    public static class ChannelDateListBean {
        /**
         * ChannelSource : 平台
         * ChannelNum1 : 0人
         * ChannelNum2 : 0人
         * ChannelNum3 : 0人
         * ChannelNum4 : 0人
         */

        private String ChannelSource;
        private String ChannelNum1;
        private String ChannelNum2;
        private String ChannelNum3;
        private String ChannelNum4;

        public String getChannelSource() {
            return ChannelSource;
        }

        public void setChannelSource(String ChannelSource) {
            this.ChannelSource = ChannelSource;
        }

        public String getChannelNum1() {
            return ChannelNum1;
        }

        public void setChannelNum1(String ChannelNum1) {
            this.ChannelNum1 = ChannelNum1;
        }

        public String getChannelNum2() {
            return ChannelNum2;
        }

        public void setChannelNum2(String ChannelNum2) {
            this.ChannelNum2 = ChannelNum2;
        }

        public String getChannelNum3() {
            return ChannelNum3;
        }

        public void setChannelNum3(String ChannelNum3) {
            this.ChannelNum3 = ChannelNum3;
        }

        public String getChannelNum4() {
            return ChannelNum4;
        }

        public void setChannelNum4(String ChannelNum4) {
            this.ChannelNum4 = ChannelNum4;
        }
    }
}
