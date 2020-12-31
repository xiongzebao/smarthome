package com.qidian.kuaitui.module.job.view;

import android.os.Handler;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ActivityManager;
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
import com.qidian.kuaitui.module.job.model.AddNewUserModel;
import com.qidian.kuaitui.module.mine.model.UserInfoBean;

import java.util.ArrayList;
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

                addUserInfo();
            }
        });


        binding.tvQudao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChannel();
            }
        });

        binding.tvProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showRecruit();
            }
        });
    }


    private void showChannel() {
        BottomSheetDialogUtils.showSelectSheetDialog(ActivityManager.peek(), "请选择渠道类型", StaticData.getChannelData(), new BottomSheetDialogUtils.onClickListener() {
            @Override
            public void onConfirm(List data) {
                MyLog.e(data.size() + "");
            }
        });
    }

    private void showRecruit() {
        BottomSheetDialogUtils.showSelectSheetDialog(ActivityManager.peek(), "请选择项目类型", GlobalData.getRecruitData(), new BottomSheetDialogUtils.onClickListener() {
            @Override
            public void onConfirm(List data) {
                MyLog.e(data.size() + "");
            }
        });
    }


    private void addUserInfo() {
        Call<ResBase> login = STClient.getService(ApiService.class).addNewUser(model);
        NetworkUtil.showCutscenes(login);
        login.enqueue(new RequestCallBack<ResBase>() {
            @Override
            public void onSuccess(Call<ResBase> call, Response<ResBase> response) {
                MyLog.e(response.body().toString());

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
