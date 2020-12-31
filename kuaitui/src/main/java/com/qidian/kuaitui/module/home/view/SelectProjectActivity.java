package com.qidian.kuaitui.module.home.view;

import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.qidian.base.base.BaseActivity;
import com.qidian.base.common.ui.ListFragment;
import com.qidian.base.network.NetworkUtil;
import com.qidian.base.network.RequestCallBack;
import com.qidian.base.utils.SharedInfo;
import com.qidian.kuaitui.R;
import com.qidian.kuaitui.api.ApiService;
import com.qidian.kuaitui.api.STClient;
import com.qidian.kuaitui.base.KTRequestCallBack;
import com.qidian.kuaitui.base.Page;
import com.qidian.kuaitui.base.ResBase;
import com.qidian.kuaitui.module.home.model.ProjectItem;
import com.qidian.kuaitui.module.mine.model.UserInfoBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SelectProjectActivity extends BaseActivity {

    ListFragment listFragment;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_select_project);
        setTitle("选择项目");
        listFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.list_fragment);
        listFragment.setLoadRefreshEnable(false);

        listFragment.setAdapter(new SelectProjectAdapter(R.layout.layout__list_project_item));

        listFragment.getAdapter().setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                ProjectItem item = (ProjectItem) adapter.getData().get(position);
                SharedInfo.getInstance().saveEntity(item);
                finish();

            }
        });


        requestData();

    }


    private void requestData() {
        Call<ResBase<List<ProjectItem>>> login = STClient.getService(ApiService.class).getProjectList(Page.getPage());
        NetworkUtil.showCutscenes(login);
        login.enqueue(new KTRequestCallBack<ResBase<List<ProjectItem>>>() {
            @Override
            public void onSuccess(Call<ResBase<List<ProjectItem>>> call, Response<ResBase<List<ProjectItem>>> response) {
                MyLog.e(response.body().resultdata.size()+"");
                listFragment.getAdapter().setNewData(response.body().resultdata);

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
}


class SelectProjectAdapter extends BaseQuickAdapter<ProjectItem, BaseViewHolder> {

    public SelectProjectAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ProjectItem item) {
        helper.setText(R.id.tv_type,item.getPostType());
        helper.setText(R.id.tv_project_name,item.getProjectName());
    }
}
