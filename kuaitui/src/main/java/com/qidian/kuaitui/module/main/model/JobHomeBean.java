package com.qidian.kuaitui.module.main.model;

/**
 * @author xiongbin
 * @description:
 * @date : 2020/12/30 15:03
 */

public class JobHomeBean {

    /**
     * PredictNum : 0
     * PredictDcNum : 0
     * InterviewNum : 0
     * EntryNum : 0
     * HistoryNum : 0
     */

    private String PredictNum;
    private String PredictDcNum;
    private String InterviewNum;
    private String EntryNum;
    private String HistoryNum;
    private String EntrySumNum;

    public String getEntrySumNum() {
        return EntrySumNum;
    }

    public void setEntrySumNum(String entrySumNum) {
        EntrySumNum = entrySumNum;
    }

    public String getPredictNum() {
        return PredictNum;
    }

    public void setPredictNum(String PredictNum) {
        this.PredictNum = PredictNum;
    }

    public String getPredictDcNum() {
        return PredictDcNum;
    }

    public void setPredictDcNum(String PredictDcNum) {
        this.PredictDcNum = PredictDcNum;
    }

    public String getInterviewNum() {
        return InterviewNum;
    }

    public void setInterviewNum(String InterviewNum) {
        this.InterviewNum = InterviewNum;
    }

    public String getEntryNum() {
        return EntryNum;
    }

    public void setEntryNum(String EntryNum) {
        this.EntryNum = EntryNum;
    }

    public String getHistoryNum() {
        return HistoryNum;
    }

    public void setHistoryNum(String HistoryNum) {
        this.HistoryNum = HistoryNum;
    }
}
