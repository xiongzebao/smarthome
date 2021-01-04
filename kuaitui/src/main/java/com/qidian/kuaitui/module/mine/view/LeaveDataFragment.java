package com.qidian.kuaitui.module.mine.view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.erongdu.wireless.network.entity.PageMo;
import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ActivityManager;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.erongdu.wireless.views.PlaceholderLayout;
import com.qidian.base.common.ui.OnLoadListener;
import com.qidian.base.common.ui.SlideListFragment;
import com.qidian.base.network.NetworkUtil;
import com.qidian.base.network.RequestCallBack;
import com.qidian.kuaitui.api.ApiService;
import com.qidian.kuaitui.api.STClient;
import com.qidian.kuaitui.base.KTRequestCallBack;
import com.qidian.kuaitui.base.ResBase;
import com.qidian.kuaitui.common.KTConstant;
import com.qidian.kuaitui.module.job.adapter.InventoryAdapter;
import com.qidian.kuaitui.module.job.model.ReceiptListBean;
import com.qidian.kuaitui.module.job.model.ReqInterViewParam;
import com.qidian.kuaitui.module.job.view.ReceiptDataActivity;
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

public class LeaveDataFragment extends SlideListFragment implements OnLoadListener {

    List<ReceiptListBean> list = new ArrayList();
    private String searchKey;
    int type = 1;

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        type = ActivityManager.peek().getIntent().getIntExtra(KTConstant.TYPE, 1);
        setSlidable();

        InventoryAdapter adapter = new InventoryAdapter(getContext(), list, type);
        adapter.setOnDeleteClickListener(new InventoryAdapter.OnDeleteClickLister() {
            @Override
            public void onDeleteClick(View view, int position, int status) {
                String id = list.get(position).getInterviewId();
                requestOperation(id, status);
            }
        });
        setAdapter(adapter);
        setOnLoadListener(this);
        requestData(1);
    }


    public void requestOperation(String id, int status) {
        switch (type) {
            case ReceiptDataActivity.RECEIPT: {
                requestModifyReceptionStatus(id, status);
                break;
            }
            case ReceiptDataActivity.INTERVIEW: {
                requestModifyInterViewStatus(id, status);
                break;
            }

            case ReceiptDataActivity.ENTRY: {
                requestModifyEntryStatus(id, status);
                break;
            }
        }
    }


    public void setSlidable() {
        if ( type == ReceiptDataActivity.HISTORY) {
            binding.swipeTarget.setCanSlide(false);
        } else {
            binding.swipeTarget.setCanSlide(true);
        }
    }


    public void requestModifyReceptionStatus(String id, int status) {
        Call<ResBase> login = STClient.getService(ApiService.class).modifyReceptionStatus(id, status + "");
        NetworkUtil.showCutscenes(login);
        login.enqueue(new RequestCallBack<ResBase>() {
            @Override
            public void onSuccess(Call<ResBase> call, Response<ResBase> response) {
                ToastUtil.toast(response.body().message);
                requestData(1);
            }

            @Override
            public void onFailed(Call<ResBase> call, Response<ResBase> response) {
                super.onFailed(call, response);

            }

            @Override
            public void onFailure(Call<ResBase> call, Throwable t) {
                super.onFailure(call, t);
                ToastUtil.toast(t.getMessage());
            }
        });
    }


    public void requestModifyInterViewStatus(String id, int status) {
        Call<ResBase> login = STClient.getService(ApiService.class).modifyInterviewStatus(id, status + "");
        NetworkUtil.showCutscenes(login);
        login.enqueue(new RequestCallBack<ResBase>() {
            @Override
            public void onSuccess(Call<ResBase> call, Response<ResBase> response) {
                ToastUtil.toast(response.body().message);
                requestData(1);
            }

            @Override
            public void onFailed(Call<ResBase> call, Response<ResBase> response) {
                super.onFailed(call, response);

            }

            @Override
            public void onFailure(Call<ResBase> call, Throwable t) {
                super.onFailure(call, t);
                ToastUtil.toast(t.getMessage());
            }
        });
    }


    public void requestModifyEntryStatus(String id, int status) {
        Call<ResBase> login = STClient.getService(ApiService.class).modifyEntryStatus(id, status + "");
        NetworkUtil.showCutscenes(login);
        login.enqueue(new RequestCallBack<ResBase>() {
            @Override
            public void onSuccess(Call<ResBase> call, Response<ResBase> response) {
                ToastUtil.toast(response.body().message);
                requestData(1);
            }

            @Override
            public void onFailed(Call<ResBase> call, Response<ResBase> response) {
                super.onFailed(call, response);

            }

            @Override
            public void onFailure(Call<ResBase> call, Throwable t) {
                super.onFailure(call, t);
                ToastUtil.toast(t.getMessage());
            }
        });
    }


    public void search(String searchKey) {
        this.searchKey = searchKey;
        MyLog.e(this.searchKey);
        requestData(1);

    }

    private void requestData(final int pageIndex) {

        final ReqInterViewParam param = new ReqInterViewParam();
        String recruitId = CommenSetUtils.getRecruitID();
        param.RecruitID = recruitId;
        param.TypeStatus = type;
        param.pageIndex = pageIndex;
        param.KeyValue = searchKey;

        Call<ResBase<List<ReceiptListBean>>> login = STClient.getService(ApiService.class).getInterViewList(param);
        NetworkUtil.showCutscenes(login);
        login.enqueue(new KTRequestCallBack<ResBase<List<ReceiptListBean>>>(binding.swipe, getPlaceHolderState()) {
            @Override
            public void onSuccess(Call<ResBase<List<ReceiptListBean>>> call, Response<ResBase<List<ReceiptListBean>>> response) {
                List<ReceiptListBean> listData = response.body().resultdata;

                if (pageIndex == 1) {
                    if (listData.size() == 0) {
                        setLoadRefreshEnable(false);
                        getPlaceHolderState().set(PlaceholderLayout.EMPTY);
                        return;
                    }
                    list.clear();
                }
                if (pageIndex > 1 && listData.size() == 0) {
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
