package com.ihome.base.common.binding;

import androidx.databinding.BindingAdapter;

import com.erongdu.wireless.views.PlaceholderLayout;

public class BindingAdapters {
    @BindingAdapter(value = {"placeholderState", "placeholderListener"}, requireAll = false)
    public static void placeholderConfig(PlaceholderLayout layout, int state, PlaceholderLayout.OnReloadListener listener) {
        layout.setStatus(state);
        if (null != listener) {
            layout.setOnReloadListener(listener);
        }
    }

}
