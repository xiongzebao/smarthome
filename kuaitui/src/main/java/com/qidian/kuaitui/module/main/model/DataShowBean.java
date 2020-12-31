package com.qidian.kuaitui.module.main.model;

import java.util.List;

/**
 * @author xiongbin
 * @description:
 * @date : 2020/12/30 14:18
 */

public class DataShowBean {

    /**
     * PassNum : 0
     * SumNum : 0
     * ChannelDateList : [{"ChannelSource":"平台","ChannelNum1":"0人","ChannelNum2":"0人","ChannelNum3":"0人","ChannelNum4":"0.00%"},{"ChannelSource":"农村电商","ChannelNum1":"0人","ChannelNum2":"0人","ChannelNum3":"0人","ChannelNum4":"0.00%"},{"ChannelSource":"自媒体","ChannelNum1":"0人","ChannelNum2":"0人","ChannelNum3":"0人","ChannelNum4":"0.00%"},{"ChannelSource":"高校","ChannelNum1":"0人","ChannelNum2":"0人","ChannelNum3":"0人","ChannelNum4":"0.00%"},{"ChannelSource":"代理","ChannelNum1":"0人","ChannelNum2":"0人","ChannelNum3":"0人","ChannelNum4":"0.00%"},{"ChannelSource":"融媒体","ChannelNum1":"0人","ChannelNum2":"0人","ChannelNum3":"0人","ChannelNum4":"0.00%"}]
     */

    private int PassNum;
    private int SumNum;
    private List<ChannelDateListBean> ChannelDateList;

    public int getPassNum() {
        return PassNum;
    }

    public void setPassNum(int PassNum) {
        this.PassNum = PassNum;
    }

    public int getSumNum() {
        return SumNum;
    }

    public void setSumNum(int SumNum) {
        this.SumNum = SumNum;
    }

    public List<ChannelDateListBean> getChannelDateList() {
        return ChannelDateList;
    }

    public void setChannelDateList(List<ChannelDateListBean> ChannelDateList) {
        this.ChannelDateList = ChannelDateList;
    }

    public static class ChannelDateListBean {


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
