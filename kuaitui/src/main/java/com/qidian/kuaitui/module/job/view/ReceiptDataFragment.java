package com.qidian.kuaitui.module.job.view;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.erongdu.wireless.network.entity.PageMo;
import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ActivityManager;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.erongdu.wireless.views.PlaceholderLayout;
import com.qidian.base.common.ui.OnLoadListener;
import com.qidian.base.common.ui.SlideListFragment;
import com.qidian.base.network.NetworkUtil;
import com.qidian.base.views.SlideRecyclerView;
import com.qidian.kuaitui.R;
import com.qidian.kuaitui.api.ApiService;
import com.qidian.kuaitui.api.STClient;
import com.qidian.kuaitui.base.KTRequestCallBack;
import com.qidian.kuaitui.base.ResBase;
import com.qidian.kuaitui.common.KTConstant;
import com.qidian.kuaitui.module.job.adapter.InventoryAdapter;
import com.qidian.kuaitui.module.job.model.ReceiptListBean;
import com.qidian.kuaitui.module.job.model.ReqInterViewParam;
import com.qidian.kuaitui.utils.CommenSetUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author xiongbin
 * @description:
 * @date : 2020/12/31 11:33
 */

public class ReceiptDataFragment extends SlideListFragment implements OnLoadListener {

    List<ReceiptListBean> list = new ArrayList();
    private String searchKey;

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAdapter(new InventoryAdapter(getContext(), list));
        setOnLoadListener(this);
        requestData(1);
    }


    public void search(String searchKey){
        this.searchKey = searchKey;
        MyLog.e(this.searchKey);
        requestData(1);

    }

    private void requestData(final int pageIndex) {
        int type = ActivityManager.peek().getIntent().getIntExtra(KTConstant.TYPE, 1);
        final ReqInterViewParam param = new ReqInterViewParam();
         String recruitId  =  CommenSetUtils.getRecruitID();
        param.RecruitID = recruitId;
        param.TypeStatus = type;
        param.pageIndex = pageIndex;
        param.KeyValue = searchKey;

        Call<ResBase<List<ReceiptListBean>>> login = STClient.getService(ApiService.class).getInterViewList(param);
        NetworkUtil.showCutscenes(login);
        login.enqueue(new KTRequestCallBack<ResBase<List<ReceiptListBean>>>(binding.swipe,getPlaceHolderState()) {
            @Override
            public void onSuccess(Call<ResBase<List<ReceiptListBean>>> call, Response<ResBase<List<ReceiptListBean>>> response) {
                List<ReceiptListBean> listData = response.body().resultdata;

                    if(pageIndex==1) {
                        if(listData.size()==0){
                            setLoadRefreshEnable(false);
                            getPlaceHolderState().set(PlaceholderLayout.EMPTY);
                            return;
                        }
                        list.clear();
                    }
                    if(pageIndex>1&&listData.size()==0){
                        setLoadEnable(false);
                        ToastUtil.toast("没有更多数据了");
                        return;
                    }

                setLoadRefreshEnable(true);
                list.addAll(response.body().resultdata);
                getAdapter().notifyDataSetChanged();
                loadFinish();
            }

            @Override
            public void onFailed(Call<ResBase<List<ReceiptListBean>>> call, Response<ResBase<List<ReceiptListBean>>> response) {
                super.onFailed(call, response);

            }

            @Override
            public void onFailure(Call<ResBase<List<ReceiptListBean>>> call, Throwable t) {
                super.onFailure(call, t);
                ToastUtil.toast(t.getMessage());
            }
        });
    }

    @Override
    public void onLoadPage(PageMo pageNo) {
            requestData(pageNo.getCurrent());
    }
}
