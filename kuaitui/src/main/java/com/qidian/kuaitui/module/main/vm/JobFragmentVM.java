package com.qidian.kuaitui.module.main.vm;

import android.view.View;

import com.erongdu.wireless.tools.utils.ActivityManager;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.qidian.base.base.BaseVM;
import com.qidian.base.network.NetworkUtil;
import com.qidian.kuaitui.api.ApiService;
import com.qidian.kuaitui.api.STClient;
import com.qidian.kuaitui.base.KTRequestCallBack;
import com.qidian.kuaitui.base.ResBase;
import com.qidian.kuaitui.databinding.FragJobBinding;
import com.qidian.kuaitui.module.job.view.ReceiptDataActivity;
import com.qidian.kuaitui.module.main.model.JobFragmentModel;
import com.qidian.kuaitui.module.main.model.JobHomeBean;
import com.qidian.kuaitui.utils.ActivityUtils;
import com.qidian.kuaitui.utils.CommenSetUtils;

import retrofit2.Call;
import retrofit2.Response;

public class JobFragmentVM extends BaseVM {
    FragJobBinding binding;
    public JobFragmentModel model;

    public JobFragmentVM() {
        model = new JobFragmentModel();
    }


    public void onLookReceptPeople(View v) {
        ActivityUtils.startInterViewActvity(ActivityManager.peek(), ReceiptDataActivity.RECEIPT);
    }


    public void onLookInterViewPeople(View v) {
        ActivityUtils.startInterViewActvity(ActivityManager.peek(), ReceiptDataActivity.INTERVIEW);
    }


    public void onLookEntryPeople(View v) {

        ActivityUtils.startInterViewActvity(ActivityManager.peek(), ReceiptDataActivity.ENTRY);

    }

    public void onLookSumEntryPeople(View v) {

        ActivityUtils.startInterViewActvity(ActivityManager.peek(), ReceiptDataActivity.SUMENTRY);

    }

    public void onLookHistoryPeople(View v) {
        ActivityUtils.startInterViewActvity(ActivityManager.peek(), ReceiptDataActivity.HISTORY);
    }


    public void startRequest() {
        requestData(CommenSetUtils.getRecruitID());
    }

    private void requestData(final String recruitId) {
        Call<ResBase<JobHomeBean>> login = STClient.getService(ApiService.class).getJobDataSumInfo(recruitId);
       // NetworkUtil.showCutscenes(login);
        login.enqueue(new KTRequestCallBack<ResBase<JobHomeBean>>() {
            @Override
            public void onSuccess(Call<ResBase<JobHomeBean>> call, Response<ResBase<JobHomeBean>> response) {

                JobHomeBean bean = response.body().resultdata;
                if(bean==null){
                    return;
                }
                model.setPredictNum(bean.getPredictNum());
                model.setPredictDcNum(bean.getPredictDcNum());
                model.setInterviewNum(bean.getInterviewNum());
                model.setEntryNum(bean.getEntryNum());
                model.setHistoryNum(bean.getHistoryNum());
                model.setEntrySumNum(bean.getEntrySumNum());
            }

            @Override
            public void onFailed(Call<ResBase<JobHomeBean>> call, Response<ResBase<JobHomeBean>> response) {
                super.onFailed(call, response);

            }

            @Override
            public void onFailure(Call<ResBase<JobHomeBean>> call, Throwable t) {
                super.onFailure(call, t);
                ToastUtil.toast(t.getMessage());
            }
        });
    }
}
