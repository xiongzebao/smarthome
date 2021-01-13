package com.ihome.smarthome.module.home.view;

import androidx.databinding.DataBindingUtil;

import android.os.Handler;

import com.erongdu.wireless.tools.utils.ToastUtil;
import com.ihome.base.base.BaseActivity;
import com.ihome.base.network.NetworkUtil;
import com.ihome.base.views.MyProgressView;
import com.ihome.smarthome.R;

import com.ihome.smarthome.api.ApiService;
import com.ihome.smarthome.api.STClient;
import com.ihome.smarthome.base.KTRequestCallBack;
import com.ihome.smarthome.base.ResBase;
import com.ihome.smarthome.common.StaticData;
import com.ihome.smarthome.databinding.ActivityDataShowBinding;
import com.ihome.smarthome.module.main.model.DataShowBean;
import com.ihome.smarthome.module.main.view.DataFragment;
import com.ihome.smarthome.utils.CommenSetUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class DataShowActivity extends BaseActivity {

    public static final String  DATA_TYPE="data_type";

    public static final int INTERVIEW=1;
    public static final int PASSED=2;
    public static final int ENTRY=3;
    public static final int SEPARATE=4;

    ActivityDataShowBinding binding;
    DataFragment dataFragment;
    MyProgressView progressView;

    @Override
    protected void bindView() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_data_show);
        progressView = binding.progressView;
        dataFragment = (DataFragment) getSupportFragmentManager().findFragmentById(R.id.data_fragment);
       setTitle(CommenSetUtils.getProjectTitle());
       requestData();
    }






    private void requestData(){
        String recruitId = CommenSetUtils.getRecruitID();
        int type = getIntent().getIntExtra(DATA_TYPE,INTERVIEW);
        switch (type){
            case INTERVIEW:requestReceptionInfo(recruitId);
                progressView.setLabel1("接待总数");
                dataFragment.setTitles(StaticData.getReceiptTitle());
            break;
            case PASSED:requestInterviewInfo(recruitId);
                progressView.setLabel1("面试总数");
                dataFragment.setTitles(StaticData.getInterViewTitle());
            break;
            case ENTRY:requestEntryInfo(recruitId);
                progressView.setLabel1("入职总数");
                dataFragment.setTitles(StaticData.getEntryTitle());
            break;
            case SEPARATE:requestLeaveInfo(recruitId);
                progressView.setLabel1("离职总数");
                dataFragment.setTitles(StaticData.getLeaveTitle());
            break;
        }
    }



    private void requestReceptionInfo(final String recruitId){
        Call<ResBase<DataShowBean>> login = STClient.getService(ApiService.class).getReceptionInfo(recruitId);
        NetworkUtil.showCutscenes(login);
        login.enqueue(new KTRequestCallBack<ResBase<DataShowBean>>() {
            @Override
            public void onSuccess(Call<ResBase<DataShowBean>> call, Response<ResBase<DataShowBean>> response) {
                parseData(response.body().resultdata);
            }

            @Override
            public void onFailed(Call<ResBase<DataShowBean> > call, Response<ResBase<DataShowBean>> response) {
                super.onFailed(call, response);

            }

            @Override
            public void onFailure(Call<ResBase<DataShowBean> >call, Throwable t) {
                super.onFailure(call, t);
                ToastUtil.toast(t.getMessage());
            }
        });
    }


    private void requestInterviewInfo(final String recruitId){
        Call<ResBase<DataShowBean>> login = STClient.getService(ApiService.class).getInterviewInfo(recruitId);
        NetworkUtil.showCutscenes(login);
        login.enqueue(new KTRequestCallBack<ResBase<DataShowBean>>() {
            @Override
            public void onSuccess(Call<ResBase<DataShowBean>> call, Response<ResBase<DataShowBean>> response) {
                parseData(response.body().resultdata);
            }

            @Override
            public void onFailed(Call<ResBase<DataShowBean> > call, Response<ResBase<DataShowBean>> response) {
                super.onFailed(call, response);

            }

            @Override
            public void onFailure(Call<ResBase<DataShowBean> >call, Throwable t) {
                super.onFailure(call, t);
                ToastUtil.toast(t.getMessage());
            }
        });
    }



    private void requestEntryInfo(final String recruitId){
        Call<ResBase<DataShowBean>> login = STClient.getService(ApiService.class).getEntryInfo(recruitId);
        NetworkUtil.showCutscenes(login);
        login.enqueue(new KTRequestCallBack<ResBase<DataShowBean>>() {
            @Override
            public void onSuccess(Call<ResBase<DataShowBean>> call, Response<ResBase<DataShowBean>> response) {
                parseData(response.body().resultdata);
            }

            @Override
            public void onFailed(Call<ResBase<DataShowBean> > call, Response<ResBase<DataShowBean>> response) {
                super.onFailed(call, response);

            }

            @Override
            public void onFailure(Call<ResBase<DataShowBean> >call, Throwable t) {
                super.onFailure(call, t);
                ToastUtil.toast(t.getMessage());
            }
        });
    }



    private void requestLeaveInfo(final String recruitId){
        Call<ResBase<DataShowBean>> login = STClient.getService(ApiService.class).getLeaveInfo(recruitId);
        NetworkUtil.showCutscenes(login);
        login.enqueue(new KTRequestCallBack<ResBase<DataShowBean>>() {
            @Override
            public void onSuccess(Call<ResBase<DataShowBean>> call, Response<ResBase<DataShowBean>> response) {
                parseData(response.body().resultdata);
            }

            @Override
            public void onFailed(Call<ResBase<DataShowBean> > call, Response<ResBase<DataShowBean>> response) {
                super.onFailed(call, response);

            }

            @Override
            public void onFailure(Call<ResBase<DataShowBean> >call, Throwable t) {
                super.onFailure(call, t);
                ToastUtil.toast(t.getMessage());
            }
        });
    }



    private void parseData(final DataShowBean bean){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progressView.setData(bean.getPassNum(),bean.getSumNum());

            }
        },500);
        dataFragment.setListDatas(getLists(bean.getChannelDateList()));
    }


    private List<List<String>> getLists(List<DataShowBean.ChannelDateListBean> listBeans) {
        List<List<String>> lists = new ArrayList<>();
        for (int i = 0; i < listBeans.size(); i++) {
            ArrayList list = new ArrayList();
            DataShowBean.ChannelDateListBean bean = listBeans.get(i);
            list.add(bean.getChannelSource());
            list.add(bean.getChannelNum1());
            list.add(bean.getChannelNum2());
            list.add(bean.getChannelNum3());
            list.add(bean.getChannelNum4());
            lists.add(list);
        }
        return lists;
    }



}