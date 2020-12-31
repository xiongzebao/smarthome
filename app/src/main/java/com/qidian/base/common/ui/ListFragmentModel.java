package com.qidian.base.common.ui;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableInt;

import com.erongdu.wireless.views.PlaceholderLayout;
import com.qidian.base.BR;
import com.qidian.base.base.BaseModel;

public class ListFragmentModel  extends BaseModel {

    public ListFragmentModel(PlaceholderLayout.OnReloadListener onReloadPage) {
        super(onReloadPage);
    }
}
