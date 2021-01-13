package com.ihome.smarthome.utils;

import com.erongdu.wireless.tools.utils.ActivityManager;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.ihome.base.network.NetworkUtil;
import com.ihome.base.utils.DialogUtils;
import com.ihome.smarthome.api.ApiService;
import com.ihome.smarthome.api.STClient;
import com.ihome.smarthome.base.KTRequestCallBack;
import com.ihome.smarthome.base.ResBase;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/7 11:41
 */

public class ApiUtils {
    public static void uploadMemUserID(String id){
        Call<ResBase> login = STClient.getService(ApiService.class).getUploadMemUserId(id);
        NetworkUtil.showCutscenes(login);
        login.enqueue(new KTRequestCallBack<ResBase>() {
            @Override
            public void onSuccess(Call<ResBase> call, Response<ResBase> response) {

                if(response.body()==null){
                    return;
                }

                if(response.body().type!=1){
                    ToastUtil.toast(response.body().message);
                    return;
                }
                DialogUtils.showDialog(ActivityManager.peek(), response.body().message, new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                    }
                },false);


            }

            @Override
            public void onFailed(Call<ResBase> call, Response<ResBase> response) {
                super.onFailed(call, response);

                String tip = "失败";
                if(response.body()!=null){
                    tip = response.body().message;
                }
                ToastUtil.toast(tip);
              /*  DialogUtils.showDialog(ActivityManager.peek(), tip, new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                },false);*/
            }

            @Override
            public void onFailure(Call<ResBase> call, Throwable t) {
                super.onFailure(call, t);
                ToastUtil.toast(t.getMessage());
            }
        });
    }
}
