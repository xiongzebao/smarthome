package com.ihome.smarthome.module.main.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.ihome.base.base.BaseFragment;
import com.ihome.smarthome.R;
import com.ihome.smarthome.databinding.FragHomeBinding;
import com.ihome.smarthome.databinding.FragJobBinding;
import com.ihome.smarthome.module.main.vm.JobFragmentVM;
import com.ihome.smarthome.utils.CommenSetUtils;

public class JobFragment extends BaseFragment {

    FragJobBinding binding;

    public static JobFragment newInstance() {
        JobFragment homeFrag = new JobFragment();
        return homeFrag;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_job, null, false);
        binding.setViewModel(new JobFragmentVM());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle(CommenSetUtils.getProjectTitle());

        requestData();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            requestData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        requestData();
    }


    public void requestData(){


        binding.getViewModel().startRequest();
    }



    public void setTitle(String title) {
        TextView tv = binding.header.findViewById(R.id.tv_title);
        ImageView iv_right = binding.header.findViewById(R.id.iv_right);
        iv_right.setVisibility(View.GONE);
        tv.setText(title);
    }






}
