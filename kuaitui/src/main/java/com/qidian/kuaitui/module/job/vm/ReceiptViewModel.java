package com.qidian.kuaitui.module.job.vm;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.erongdu.wireless.network.entity.PageMo;
import com.erongdu.wireless.tools.utils.ActivityManager;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.qidian.base.base.BaseVM;

import com.qidian.base.common.ui.OnLoadListener;
import com.qidian.base.common.ui.SlideListFragment;
import com.qidian.base.network.NetworkUtil;
import com.qidian.base.views.EditText_Clear;
import com.qidian.kuaitui.R;
import com.qidian.kuaitui.api.ApiService;
import com.qidian.kuaitui.api.STClient;
import com.qidian.kuaitui.base.KTRequestCallBack;
import com.qidian.kuaitui.base.ResBase;
import com.qidian.kuaitui.common.KTConstant;
import com.qidian.kuaitui.databinding.ActReceptDataBinding;
import com.qidian.kuaitui.module.job.adapter.InventoryAdapter;
import com.qidian.kuaitui.module.job.model.ReceiptListBean;
import com.qidian.kuaitui.module.job.model.ReqInterViewParam;
import com.qidian.kuaitui.module.job.view.AddNewUserActivity;
import com.qidian.kuaitui.module.job.view.ReceiptDataActivity;
import com.qidian.kuaitui.module.main.model.JobHomeBean;
import com.qidian.kuaitui.utils.CommenSetUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ReceiptViewModel extends BaseVM implements OnLoadListener {

    ActReceptDataBinding binding;
    SlideListFragment listFragment ;
    ImageView iv_right;
    EditText_Clear searchView;

    public ReceiptViewModel(ActReceptDataBinding binding,ReceiptDataActivity act){
        this.binding = binding;
        searchView = binding.etSearch;
        listFragment = (SlideListFragment) act.getSupportFragmentManager().findFragmentById(R.id.receipt_list_fragment);
       // listFragment.setAdapter(new ReceiptListAdapter(R.layout.layout_receipt_list_item,null));
        listFragment.setAdapter(new InventoryAdapter( act,getData()));
        listFragment.setOnLoadListener(this);
       iv_right =  binding.layoutHeader.findViewById(R.id.iv_right);
       iv_right.setVisibility(View.VISIBLE);

       iv_right.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                ActivityManager.startActivity(AddNewUserActivity.class);
           }
       });

        startRequest();
    }






    public void startRequest(){
            requestData();
    }




    private void requestData(){

        int type = ActivityManager.peek().getIntent().getIntExtra(KTConstant.TYPE,1);
        ReqInterViewParam param = new ReqInterViewParam();
        param.RecruitID = CommenSetUtils.getRecruitID();
        param.TypeStatus = type;

        Call<ResBase<JobHomeBean>> login = STClient.getService(ApiService.class).getInterViewList(param);
        NetworkUtil.showCutscenes(login);
        login.enqueue(new KTRequestCallBack<ResBase<JobHomeBean>>() {
            @Override
            public void onSuccess(Call<ResBase<JobHomeBean>> call, Response<ResBase<JobHomeBean>> response) {

            }

            @Override
            public void onFailed(Call<ResBase<JobHomeBean> > call, Response<ResBase<JobHomeBean>> response) {
                super.onFailed(call, response);

            }

            @Override
            public void onFailure(Call<ResBase<JobHomeBean> >call, Throwable t) {
                super.onFailure(call, t);
                ToastUtil.toast(t.getMessage());
            }
        });
    }




    @Override
    public void onLoadPage(final PageMo pageNo) {
/*        new Handler().postDelayed(new Runnable() {



            @Override
            public void run() {
                if(pageNo.getCurrent()==0){
                    listFragment.getAdapter().setNewData(getData());

                }else{
                    listFragment.getAdapter().addData(getData());
                }
                  listFragment.loadFinish();
            }
        },500);*/

    }

    private List<ReceiptListBean> getData(){
        ArrayList<ReceiptListBean> list = new ArrayList<>();
        for (int i=0;i<10;i++){
            list.add(new ReceiptListBean());
        }

        return list;
    }





}
