package com.qidian.kuaitui.module.mine.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.qidian.kuaitui.BR;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/4 15:21
 */

public class FeedBackModel extends BaseObservable {

    /**
     * AnomalyTypeValue : string
     * AnomalyTypeName : string
     * AnomalyDate : 2021-01-04T08:34:17.403Z
     * AnomalyTitle : string
     * AnomalyDesc : string
     */

    public String VisitTypeValue;
    public String VisitTypeName;
    public String VisitDate;
    public String VisitTitle;
    public String VisitDesc;

    public String showNumber="0/500";






    @Bindable
    public String getShowNumber() {
        return showNumber;
    }

    public void setShowNumber(String showNumber) {
        this.showNumber = showNumber;
        notifyPropertyChanged(BR.showNumber);
    }

    @Bindable
    public String getVisitTypeValue() {
        return VisitTypeValue;
    }

    public void setVisitTypeValue(String AnomalyTypeValue) {
        this.VisitTypeValue = AnomalyTypeValue;
        notifyPropertyChanged(BR.visitTypeValue);
    }

    @Bindable
    public String getVisitTypeName() {
        return VisitTypeName;
    }

    public void setVisitTypeName(String AnomalyTypeName) {
        this.VisitTypeName = AnomalyTypeName;
        notifyPropertyChanged(BR.visitTypeName);
    }

    @Bindable
    public String getVisitDate() {
        return VisitDate;
    }

    public void setVisitDate(String AnomalyDate) {
        this.VisitDate = AnomalyDate;
        notifyPropertyChanged(BR.visitDate);
    }

    @Bindable
    public String getVisitTitle() {
        return VisitTitle;
    }

    public void setVisitTitle(String AnomalyTitle) {
        this.VisitTitle = AnomalyTitle;
        notifyPropertyChanged(BR.visitTitle);
    }

    @Bindable
    public String getVisitDesc() {
        return VisitDesc;
    }

    public void setVisitDesc(String AnomalyDesc) {
        this.VisitDesc = AnomalyDesc;
        notifyPropertyChanged(BR.visitDesc);
    }
}
