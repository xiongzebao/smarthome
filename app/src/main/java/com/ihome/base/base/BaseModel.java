package com.ihome.base.base;

import androidx.databinding.ObservableInt;

import com.erongdu.wireless.views.PlaceholderLayout;

public class BaseModel {
    public ObservableInt placeholderState = new ObservableInt(PlaceholderLayout.SUCCESS);
    public PlaceholderLayout.OnReloadListener placeholderListener;


    public BaseModel(PlaceholderLayout.OnReloadListener onReloadPage) {
        this.placeholderListener = onReloadPage;
    }

}
