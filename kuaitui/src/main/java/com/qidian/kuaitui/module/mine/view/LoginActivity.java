package com.qidian.kuaitui.module.mine.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ActivityManager;
import com.erongdu.wireless.tools.utils.TextUtil;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.qidian.base.network.NetworkUtil;
import com.qidian.base.network.RequestCallBack;
import com.qidian.base.utils.SharedInfo;
import com.qidian.base.utils.SystemBarUtils;
import com.qidian.kuaitui.MainActivity;
import com.qidian.kuaitui.R;
import com.qidian.kuaitui.api.ApiService;
import com.qidian.kuaitui.api.STClient;
import com.qidian.kuaitui.base.ResBase;
import com.qidian.kuaitui.common.KTConstant;
import com.qidian.kuaitui.module.mine.model.LoginBean;
import com.qidian.kuaitui.module.mine.model.UserInfoBean;
import com.qidian.kuaitui.utils.ActivityUtils;

import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText userNameView;
    EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SystemBarUtils.setAndroidNativeLightStatusBar(this,true);
        userNameView = findViewById(R.id.username);
        passwordView = findViewById(R.id.password);
        userNameView.setText( (String)SharedInfo.getInstance().getValue(KTConstant.LOGIN_NAME,"") );
        ActivityManager.push(this);
    }

    public void onClick(View view){
       String username =  userNameView.getText().toString();
       String password = passwordView.getText().toString();
        if(TextUtil.isEmpty_new(username)||TextUtil.isEmpty_new(password)){
            ToastUtil.toast("请输入用户名或密码");
            return;
        }
       login(username,password);
    }




    private void getUserInfo(){
        Call<ResBase<UserInfoBean>> login = STClient.getService(ApiService.class).getUserInfo();
        NetworkUtil.showCutscenes(login);
        login.enqueue(new RequestCallBack<ResBase<UserInfoBean>>() {
            @Override
            public void onSuccess(Call<ResBase<UserInfoBean>> call, Response<ResBase<UserInfoBean>> response) {
                MyLog.e(response.body().toString());

            }

            @Override
            public void onFailed(Call<ResBase<UserInfoBean>> call, Response<ResBase<UserInfoBean>> response) {
                super.onFailed(call, response);

            }

            @Override
            public void onFailure(Call<ResBase<UserInfoBean>> call, Throwable t) {
                super.onFailure(call, t);
                ToastUtil.toast(t.getMessage());
            }
        });
    }



    public void login(final String username, String password) {
        Call<ResBase<LoginBean>> login = STClient.getService(ApiService.class).login(username.trim(),password.trim());
        NetworkUtil.showCutscenes(login);
        login.enqueue(new RequestCallBack<ResBase<LoginBean>>() {
            @Override
            public void onSuccess(Call<ResBase<LoginBean>> call, Response<ResBase<LoginBean>> response) {
                if(!response.body().resultdata.isSuccess()){
                    ToastUtil.toast(response.body().resultdata.getMessage());
                    return;
                }
                SharedInfo.getInstance().saveValue(KTConstant.TOKEN,response.body().resultdata.getToken());
                SharedInfo.getInstance().saveEntity(response.body());
                SharedInfo.getInstance().saveValue(KTConstant.LOGIN_NAME,username);
                STClient.reCreate();
                ToastUtil.toast("登录成功");

                ActivityManager.startActivity(MainActivity.class);
            }

            @Override
            public void onFailed(Call<ResBase<LoginBean>> call, Response<ResBase<LoginBean>> response) {
                super.onFailed(call, response);
                ToastUtil.toast(response.body().resultdata.getMessage());
                MyLog.e(response.body().toString());
            }

            @Override
            public void onFailure(Call<ResBase<LoginBean>> call, Throwable t) {
                super.onFailure(call, t);
                ToastUtil.toast(t.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.pop();
    }
}