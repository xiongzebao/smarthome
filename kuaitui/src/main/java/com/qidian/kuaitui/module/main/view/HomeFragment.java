package com.qidian.kuaitui.module.main.view;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ActivityManager;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.qidian.base.base.BaseFragment;
import com.qidian.base.network.NetworkUtil;
import com.qidian.base.utils.SharedInfo;
import com.qidian.kuaitui.R;
import com.qidian.kuaitui.api.ApiService;
import com.qidian.kuaitui.api.STClient;
import com.qidian.kuaitui.base.KTRequestCallBack;
import com.qidian.kuaitui.base.Page;
import com.qidian.kuaitui.base.ResBase;
import com.qidian.kuaitui.common.GlobalData;
import com.qidian.kuaitui.databinding.FragHomeBinding;
import com.qidian.kuaitui.module.home.model.ProjectItem;
import com.qidian.kuaitui.module.home.view.DataShowActivity;
import com.qidian.kuaitui.module.home.view.SelectProjectActivity;
import com.qidian.kuaitui.module.main.model.HomeStaticBean;
import com.qidian.kuaitui.utils.CommenSetUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class HomeFragment extends BaseFragment {

    FragHomeBinding binding;
    DataFragment dataFragment;

    public static HomeFragment newInstance() {
        HomeFragment homeFrag = new HomeFragment();
        return homeFrag;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_home, null, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataFragment = (DataFragment) getChildFragmentManager().findFragmentById(R.id.frag_main_list_data);
        setOnClickListener();
        dataFragment.setTitles(getTitles());
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


    private void requestData(){
        ProjectItem item =  SharedInfo.getInstance().getEntity(ProjectItem.class);
        if(item!=null&&item.getRecruitID()!=null){
            String topTitle = CommenSetUtils.getProjectTitle();
            setTitle(topTitle);
            requestHomeStatics(item.getRecruitID());
        }else{
            requestProjectData();
        }

    }

    public void requestProjectData() {
        Call<ResBase<List<ProjectItem>>> login = STClient.getService(ApiService.class).getProjectList(Page.getPage());
       // NetworkUtil.showCutscenes(login);
        login.enqueue(new KTRequestCallBack<ResBase<List<ProjectItem>>>() {
            @Override
            public void onSuccess(Call<ResBase<List<ProjectItem>>> call, Response<ResBase<List<ProjectItem>>> response) {

                GlobalData.recruits = response.body().resultdata;
                ProjectItem item = response.body().resultdata.get(0);
                SharedInfo.getInstance().saveEntity(item);

                setTitle(item.getPostType() + "\n" + item.getProjectName());

                requestHomeStatics(item.getRecruitID());
            }

            @Override
            public void onFailed(Call<ResBase<List<ProjectItem>>> call, Response<ResBase<List<ProjectItem>>> response) {
                super.onFailed(call, response);

            }

            @Override
            public void onFailure(Call<ResBase<List<ProjectItem>>> call, Throwable t) {
                super.onFailure(call, t);
                ToastUtil.toast(t.getMessage());
            }
        });
    }


    private void parseData(HomeStaticBean bean){
        dataFragment.setListDatas(getLists(bean.getChannelDateList()));
        binding.tv.setText(bean.getTomorrowPlanReceptionNum());
        binding.tv0.setText(bean.getToDayPlanReceptionNum());
        binding.tv1.setText(bean.getTodayReceptionNum());
        binding.tv2.setText(bean.getTodayInterviewNum());
        binding.tv3.setText(bean.getTodayEntryNum());
        binding.tv4.setText(bean.getTodayLeaveNum());
    }


private void requestHomeStatics(final String recruitId){
    Call<ResBase<HomeStaticBean> > login = STClient.getService(ApiService.class).getHomePageStatics(recruitId);
   // NetworkUtil.showCutscenes(login);
    login.enqueue(new KTRequestCallBack<ResBase<HomeStaticBean>>() {
        @Override
        public void onSuccess(Call<ResBase<HomeStaticBean>> call, Response<ResBase<HomeStaticBean>> response) {
                parseData(response.body().resultdata);
        }

        @Override
        public void onFailed(Call<ResBase<HomeStaticBean> > call, Response<ResBase<HomeStaticBean>> response) {
            super.onFailed(call, response);

        }

        @Override
        public void onFailure(Call<ResBase<HomeStaticBean> >call, Throwable t) {
            super.onFailure(call, t);
            ToastUtil.toast(t.getMessage());
        }
    });
}






    private void setOnClickListener() {
        binding.llView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DataShowActivity.class);
                intent.putExtra(DataShowActivity.DATA_TYPE, DataShowActivity.INTERVIEW);
                startActivity(intent);
            }
        });

        binding.llView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DataShowActivity.class);
                intent.putExtra(DataShowActivity.DATA_TYPE, DataShowActivity.PASSED);
                startActivity(intent);
            }
        });

        binding.llView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DataShowActivity.class);
                intent.putExtra(DataShowActivity.DATA_TYPE, DataShowActivity.ENTRY);
                startActivity(intent);
            }
        });
        binding.llView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DataShowActivity.class);
                intent.putExtra(DataShowActivity.DATA_TYPE, DataShowActivity.SEPARATE);
                startActivity(intent);
            }
        });

        binding.header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.startActivity(SelectProjectActivity.class);
            }
        });

    }


    public void setTitle(String title) {
        TextView tv = binding.header.findViewById(R.id.tv_title);
        tv.setText(title);
    }


    private List<List<String>> getLists(List<HomeStaticBean.ChannelDateListBean> listBeans) {
        List<List<String>> lists = new ArrayList<>();
        for (int i = 0; i < listBeans.size(); i++) {
            ArrayList list = new ArrayList();
            HomeStaticBean.ChannelDateListBean bean = listBeans.get(i);
            list.add(bean.getChannelSource());
            list.add(bean.getChannelNum1());
            list.add(bean.getChannelNum2());
            list.add(bean.getChannelNum3());
            list.add(bean.getChannelNum4());
            lists.add(list);
        }
        return lists;
    }


    private List<String> getTitles() {
        ArrayList list = new ArrayList();
        list.add("渠道名称");
        list.add("当日面试");
        list.add("当日通过");
        list.add("当日入职");
        list.add("当日离职");
        return list;
    }


    public void onClick(View v) {

    }
}
