package com.qidian.kuaitui.module.job.view;

import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ActivityManager;
import com.erongdu.wireless.tools.utils.TextUtil;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.qidian.base.base.BaseActivity;
import com.qidian.base.network.NetworkUtil;
import com.qidian.base.network.RequestCallBack;
import com.qidian.base.utils.BottomSheetDialogUtils;
import com.qidian.base.views.SelectRecyclerView;
import com.qidian.kuaitui.R;
import com.qidian.kuaitui.api.ApiService;
import com.qidian.kuaitui.api.STClient;
import com.qidian.kuaitui.base.ResBase;
import com.qidian.kuaitui.common.GlobalData;
import com.qidian.kuaitui.common.StaticData;
import com.qidian.kuaitui.databinding.ActivityAddUserBinding;
import com.qidian.kuaitui.module.home.model.ProjectItem;
import com.qidian.kuaitui.module.job.model.AddNewUserModel;
import com.qidian.kuaitui.module.job.model.ChannelBean;

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
                    showRecruit();
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

    private void showRecruit() {
        BottomSheetDialogUtils.showSelectSheetDialog(ActivityManager.peek(), "请选择项目类型", GlobalData.recruits, new BottomSheetDialogUtils.onClickListener() {
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
        login.enqueue(new RequestCallBack<ResBase<List<ChannelBean>>>() {
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
        login.enqueue(new RequestCallBack<ResBase>() {
            @Override
            public void onSuccess(Call<ResBase> call, Response<ResBase> response) {
                 ToastUtil.toast(response.body().message);

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
