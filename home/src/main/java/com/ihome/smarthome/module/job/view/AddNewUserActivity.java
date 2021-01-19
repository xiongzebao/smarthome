package com.ihome.smarthome.module.job.view;

import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.erongdu.wireless.tools.utils.ActivityManager;
import com.erongdu.wireless.tools.utils.TextUtil;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.ihome.base.base.BaseActivity;
import com.ihome.base.network.NetworkUtil;

import com.ihome.base.utils.BottomSheetDialogUtils;
import com.ihome.smarthome.R;
import com.ihome.smarthome.api.ApiService;
import com.ihome.smarthome.api.STClient;
import com.ihome.smarthome.base.KTRequestCallBack;
import com.ihome.smarthome.base.Page;
import com.ihome.smarthome.base.ResBase;
import com.ihome.smarthome.databinding.ActivityAddUserBinding;
import com.ihome.smarthome.module.home.model.ProjectItem;
import com.ihome.smarthome.module.job.model.AddNewUserModel;
import com.ihome.smarthome.module.job.model.ChannelBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author xiongbin
 * @description:
 * @date : 2020/12/30 16:34
 */

public class AddNewUserActivity extends BaseActivity {

    ActivityAddUserBinding binding;
    AddNewUserModel model = new AddNewUserModel();

    @Override
    protected void bindView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_user);
        binding.setModel(model);
        setTitle("新增用户信息");
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!check()){
                    return;
                }
                addUserInfo();
            }
        });
        binding.tvQudao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChannelData();
            }
        });
        binding.tvProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     requestProjectData();
            }
        });
    }


    private boolean check(){
        if(TextUtil.isEmpty_new(model.getMemRealName())){
            ToastUtil.toast("请输入姓名");
            return false;
        }
        if(TextUtil.isEmpty_new(model.getMemCellPhone())){
            ToastUtil.toast("请输入电话");
            return false;
        }

        if(TextUtil.isEmpty_new(model.getChannelSource())){
            ToastUtil.toast("请选择渠道");
            return false;
        }


        if(TextUtil.isEmpty_new(model.getRecruitID())){
            ToastUtil.toast("请选择项目");
            return false;
        }

        return true;
    }

    private void showChannel(List data) {
        BottomSheetDialogUtils.showSelectSheetDialog(ActivityManager.peek(), "请选择渠道类型",  data, new BottomSheetDialogUtils.onClickListener() {
            @Override
            public void onConfirm(List data) {
                if(data==null||data.isEmpty()){
                    return;
                }
                ChannelBean value =  (ChannelBean)data.get(0);
                model.setChannelSource(value.ChannelValue);
                model.setChannelName(value.ChannelName);

            }
        });
    }


    public void requestProjectData() {
        Call<ResBase<List<ProjectItem>>> login = STClient.getService(ApiService.class).getProjectList(Page.getPage());
        NetworkUtil.showCutscenes(login);
        login.enqueue(new KTRequestCallBack<ResBase<List<ProjectItem>>>() {
            @Override
            public void onSuccess(Call<ResBase<List<ProjectItem>>> call, Response<ResBase<List<ProjectItem>>> response) {

                 showRecruit(response.body().resultdata);

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





    private void showRecruit(List data) {

        BottomSheetDialogUtils.showSelectSheetDialog(ActivityManager.peek(), "请选择项目类型", data, new BottomSheetDialogUtils.onClickListener() {
            @Override
            public void onConfirm(List data) {
                if(data==null||data.isEmpty()){
                    return;
                }
                ProjectItem value = (ProjectItem)data.get(0);
                 model.setRecruitID(value.getRecruitID());
                 model.setRecruitName(value.getProjectName());
            }
        });
    }

    private void getChannelData(){
        Call<ResBase<List<ChannelBean>>> login = STClient.getService(ApiService.class).getChannel();
        NetworkUtil.showCutscenes(login);
        login.enqueue(new KTRequestCallBack<ResBase<List<ChannelBean>>>() {
            @Override
            public void onSuccess(Call<ResBase<List<ChannelBean>>> call, Response<ResBase<List<ChannelBean>>> response) {
                showChannel(response.body().resultdata);

            }

            @Override
            public void onFailed(Call<ResBase<List<ChannelBean>>> call, Response<ResBase<List<ChannelBean>>> response) {
                super.onFailed(call, response);

            }

            @Override
            public void onFailure(Call<ResBase<List<ChannelBean>>> call, Throwable t) {
                super.onFailure(call, t);
                ToastUtil.toast(t.getMessage());
            }
        });
    }

    private void addUserInfo() {

        Call<ResBase> login = STClient.getService(ApiService.class).addNewUser(model);
        NetworkUtil.showCutscenes(login);
        login.enqueue(new KTRequestCallBack<ResBase>() {
            @Override
            public void onSuccess(Call<ResBase> call, Response<ResBase> response) {
                 ToastUtil.toast(response.body().message);
                 setResult(ReceiptDataActivity.REFRESH);

                 finish();

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
}
