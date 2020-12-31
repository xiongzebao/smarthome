package com.qidian.kuaitui.module.main.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.erongdu.wireless.tools.utils.ActivityManager;
import com.qidian.base.base.BaseFragment;
import com.qidian.kuaitui.R;
import com.qidian.kuaitui.databinding.FragHomeBinding;
import com.qidian.kuaitui.databinding.FragMineBinding;
import com.qidian.kuaitui.module.mine.model.LoginBean;
import com.qidian.kuaitui.module.mine.view.LoginActivity;

public class MineFragment extends BaseFragment {

    FragMineBinding binding;

    public static MineFragment newInstance() {
        MineFragment homeFrag = new MineFragment();
        return homeFrag;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_mine, null, false);

        setBinding();

        return binding.getRoot();
    }

    private void setBinding(){
        binding.rlAvator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.startActivity(LoginActivity.class);
            }
        });
    }




}
