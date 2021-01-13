package com.ihome.smarthome.module.home.view;

import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.ihome.base.base.BaseActivity;
import com.ihome.base.common.ui.ListFragment;
import com.ihome.base.network.NetworkUtil;

import com.ihome.base.utils.SharedInfo;
import com.ihome.smarthome.R;
import com.ihome.smarthome.api.ApiService;
import com.ihome.smarthome.api.STClient;
import com.ihome.smarthome.base.KTRequestCallBack;
import com.ihome.smarthome.base.Page;
import com.ihome.smarthome.base.ResBase;
import com.ihome.smarthome.module.home.model.ProjectItem;

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
        helper.setText(R.id.tv_type,item.getFunctionName());
        helper.setText(R.id.tv_project_name,item.getProjectName());
    }
}
