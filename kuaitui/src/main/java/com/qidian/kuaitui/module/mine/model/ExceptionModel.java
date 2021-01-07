package com.qidian.kuaitui.module.mine.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.Observable;

import com.qidian.kuaitui.BR;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/4 15:21
 */

public class ExceptionModel  extends BaseObservable {

    /**
     * AnomalyTypeValue : string
     * AnomalyTypeName : string
     * AnomalyDate : 2021-01-04T08:34:17.403Z
     * AnomalyTitle : string
     * AnomalyDesc : string
     */

    public String AnomalyTypeValue;
    public String AnomalyTypeName;
    public String AnomalyDate;
    public String AnomalyTitle;
    public String AnomalyDesc;

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
    public String getAnomalyTypeValue() {
        return AnomalyTypeValue;
    }

    public void setAnomalyTypeValue(String AnomalyTypeValue) {
        this.AnomalyTypeValue = AnomalyTypeValue;
        notifyPropertyChanged(BR.anomalyTypeValue);
    }

    @Bindable
    public String getAnomalyTypeName() {
        return AnomalyTypeName;
    }

    public void setAnomalyTypeName(String AnomalyTypeName) {
        this.AnomalyTypeName = AnomalyTypeName;
        notifyPropertyChanged(BR.anomalyTypeName);
    }

    @Bindable
    public String getAnomalyDate() {
        return AnomalyDate;
    }

    public void setAnomalyDate(String AnomalyDate) {
        this.AnomalyDate = AnomalyDate;
        notifyPropertyChanged(BR.anomalyDate);
    }

    @Bindable
    public String getAnomalyTitle() {
        return AnomalyTitle;
    }

    public void setAnomalyTitle(String AnomalyTitle) {
        this.AnomalyTitle = AnomalyTitle;
        notifyPropertyChanged(BR.anomalyTitle);
    }

    @Bindable
    public String getAnomalyDesc() {
        return AnomalyDesc;
    }

    public void setAnomalyDesc(String AnomalyDesc) {
        this.AnomalyDesc = AnomalyDesc;
        notifyPropertyChanged(BR.anomalyDesc);
    }
}
