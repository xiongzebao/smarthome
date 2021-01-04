package com.qidian.kuaitui.module.mine.view;

import android.graphics.Color;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.qidian.base.base.BaseActivity;
import com.qidian.base.network.NetworkUtil;
import com.qidian.kuaitui.R;
import com.qidian.kuaitui.api.ApiService;
import com.qidian.kuaitui.api.STClient;
import com.qidian.kuaitui.base.KTRequestCallBack;
import com.qidian.kuaitui.base.ResBase;
import com.qidian.kuaitui.databinding.ActivityExceptionUploadBinding;
import com.qidian.kuaitui.module.mine.adapter.ExceptionAdapter;
import com.qidian.kuaitui.module.mine.model.ExceptionBean;
import com.qidian.kuaitui.module.mine.model.ExceptionModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/4 15:22
 */

public class ExceptionActivity extends BaseActivity {

    ActivityExceptionUploadBinding binding;
    ExceptionAdapter adapter;
    ExceptionModel model = new ExceptionModel();

    @Override
    protected void bindView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exception_upload);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);

        setTitle("异常确认");
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.setModel(model);
        requestExceptionData();


        binding.tvExceptionTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestUploadException();
            }
        });

    }


    private void showTimePicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(2013, 0, 1);
        endDate.set(2020, 11, 31);

        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // tvTime.setText(getTime(date));
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("Cancel")//取消按钮文字
                .setSubmitText("Sure")//确认按钮文字
                // .setContentSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleText("Title")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
                .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();
        pvTime.show();
    }


    private void requestExceptionData() {
        Call<ResBase<List<ExceptionBean>>> login = STClient.getService(ApiService.class).getAnomalyTypeInfo();
        NetworkUtil.showCutscenes(login);
        login.enqueue(new KTRequestCallBack<ResBase<List<ExceptionBean>>>() {
            @Override
            public void onSuccess(Call<ResBase<List<ExceptionBean>>> call, Response<ResBase<List<ExceptionBean>>> response) {

                binding.recyclerView.setData(response.body().resultdata);
            }

            @Override
            public void onFailed(Call<ResBase<List<ExceptionBean>>> call, Response<ResBase<List<ExceptionBean>>> response) {
                super.onFailed(call, response);

            }

            @Override
            public void onFailure(Call<ResBase<List<ExceptionBean>>> call, Throwable t) {
                super.onFailure(call, t);
                ToastUtil.toast(t.getMessage());
            }
        });
    }


    private void requestUploadException() {
        Call<ResBase> login = STClient.getService(ApiService.class).createAnomalyInfo(model);
        NetworkUtil.showCutscenes(login);
        login.enqueue(new KTRequestCallBack<ResBase>() {
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
