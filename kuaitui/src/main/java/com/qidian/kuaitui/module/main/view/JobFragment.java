package com.qidian.kuaitui.module.main.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.qidian.base.base.BaseFragment;
import com.qidian.base.network.NetworkUtil;
import com.qidian.kuaitui.R;
import com.qidian.kuaitui.api.ApiService;
import com.qidian.kuaitui.api.STClient;
import com.qidian.kuaitui.base.KTRequestCallBack;
import com.qidian.kuaitui.base.ResBase;
import com.qidian.kuaitui.databinding.FragHomeBinding;
import com.qidian.kuaitui.databinding.FragJobBinding;
import com.qidian.kuaitui.module.main.model.DataShowBean;
import com.qidian.kuaitui.module.main.model.HomeStaticBean;
import com.qidian.kuaitui.module.main.model.JobHomeBean;
import com.qidian.kuaitui.module.main.vm.JobFragmentVM;
import com.qidian.kuaitui.utils.CommenSetUtils;

import retrofit2.Call;
import retrofit2.Response;

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
        tv.setCompoundDrawables(null,null,null,null);
        tv.setText(title);
    }






}
