package com.qidian.base;



import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.erongdu.wireless.tools.utils.ActivityManager;
import com.qidian.base.base.BaseActivity;
import com.qidian.base.model.NoticeListRec;
import com.qidian.base.network.RDClient;
import com.qidian.base.network.RequestCallBack;
import com.qidian.base.network.api.HomeService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.push(this);
        setContentView(R.layout.activity_main);
        reqNotices();
    }



    public void reqNotices() {
        Call<NoticeListRec> xueli = RDClient.getService(HomeService.class).getNotices();
//        NetworkUtil.showCutscenes(xueli);
        xueli.enqueue(new RequestCallBack<NoticeListRec>() {
            @Override
            public void onSuccess(Call<NoticeListRec> call, Response<NoticeListRec> response) {
                List<NoticeListRec.ResultdataBean> list = response.body().getResultdata();
                Log.e("xiong","list size:"+list.size());
            }

            @Override
            public void onFailed(Call<NoticeListRec> call, Response<NoticeListRec> response) {
                super.onFailed(call, response);

            }

            @Override
            public void onFailure(Call<NoticeListRec> call, Throwable t) {
                super.onFailure(call, t);
                Log.e("xiong",t.getMessage());

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.pop();
    }
}