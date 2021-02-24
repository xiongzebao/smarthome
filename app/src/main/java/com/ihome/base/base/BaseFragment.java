package com.ihome.base.base;


import android.view.View;

import androidx.fragment.app.Fragment;

import com.erongdu.wireless.views.PlaceholderLayout;

public class BaseFragment extends Fragment implements PlaceholderLayout.OnReloadListener{



    @Override
    public void onReload(View v) {

    }

    @Override
    public void onApply(View v) {

    }

    protected String getTAG(){
        return this.getClass().getSimpleName();
    }
}
