package com.qidian.kuaitui.module.main.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.qidian.kuaitui.BR;

public class JobFragmentModel extends BaseObservable {


    /**
     * PredictNum : 0
     * PredictDcNum : 0
     * InterviewNum : 0
     * EntryNum : 0
     * HistoryNum : 0
     */

    public String PredictNum="0";
    public String PredictDcNum="0";
    public String InterviewNum="0";
    public String EntryNum="0";
    public String HistoryNum="0";
     public String EntrySumNum="0";

    @Bindable
    public String getEntrySumNum() {
        return EntrySumNum;
    }

    public void setEntrySumNum(String entrySumNum) {
        EntrySumNum = entrySumNum;
        notifyPropertyChanged(BR.entrySumNum);
    }

    @Bindable
    public String getPredictNum() {
        return PredictNum;
    }

    public void setPredictNum(String PredictNum) {
        this.PredictNum = PredictNum;
        notifyPropertyChanged(BR.predictNum);
    }

    @Bindable
    public String getPredictDcNum() {
        return PredictDcNum;
    }

    public void setPredictDcNum(String PredictDcNum) {
        this.PredictDcNum = PredictDcNum;
        notifyPropertyChanged(BR.predictDcNum);
    }

    @Bindable
    public String getInterviewNum() {
        return InterviewNum;
    }

    public void setInterviewNum(String InterviewNum) {
        this.InterviewNum = InterviewNum;
        notifyPropertyChanged(BR.interviewNum);
    }

    @Bindable
    public String getEntryNum() {
        return EntryNum;
    }

    public void setEntryNum(String EntryNum) {
        this.EntryNum = EntryNum;
        notifyPropertyChanged(BR.entryNum);
    }

    @Bindable
    public String getHistoryNum() {
        return HistoryNum;
    }

    public void setHistoryNum(String HistoryNum) {
        this.HistoryNum = HistoryNum;
        notifyPropertyChanged(BR.historyNum);
    }
}
