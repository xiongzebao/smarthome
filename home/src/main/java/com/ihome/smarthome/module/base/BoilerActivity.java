package com.ihome.smarthome.module.base;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.JsonUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.ihome.base.base.BaseActivity;
import com.ihome.base.utils.RecyclerViewUtils;
import com.ihome.smarthome.R;
import com.ihome.smarthome.base.ResBase;
import com.ihome.smarthome.module.base.communicate.MyBluetoothManager;
import com.ihome.smarthome.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/11 11:14
 */

public class BoilerActivity extends BaseActivity {
    Button btn_open;
    Button btn_close;
    Button btn_start;

    EventAdapter eventAdapter;
    RecyclerView recyclerView;
    List<Event> data = new ArrayList<>();
    @Override
    protected void bindView() {
        setContentView(R.layout.layout_boiler);
        btn_open = findViewById(R.id.btn_open);
        btn_close = findViewById(R.id.btn_close);
        btn_start = findViewById(R.id.btn_start);
        recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewUtils.setVerticalLayout(this ,recyclerView);

        eventAdapter = new EventAdapter(R.layout.layout_event_item,data);
         btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Base<List> listBase = new Base<>();
                 listBase.result_data = data;
                String JsonStr = GsonUtils.toJson(listBase);
                ToastUtil.toast(JsonStr);
                MyLog.e(JsonStr+"@");
                MyBluetoothManager.getInstance().sendMessage("cooker",JsonStr+"@");
            }
        });
        recyclerView.setAdapter(eventAdapter);
         btn_open.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 addOpenEvent();
             }
         });

         btn_close.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                addCloseEvent();
             }
         });

         eventAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener(){

             @Override
             public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                 eventAdapter.remove(position);
             }
         });

    }



    private void addOpenEvent(){
        ViewUtils.showTimePicker(this, "请选择开启时间", true, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Event event = new Event();
                event.name = "开启";
                event.date=date;
                event.type = Event.ON;
                long count = TimeUtils.getTimeSpan(date,new Date(), TimeConstants.SEC);
                event.time = count;
                data.add(event);
                eventAdapter.setNewData(data);
            }
        });
    }

    private void addCloseEvent(){
        ViewUtils.showTimePicker(this, "请选择关闭时间", true, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Event event = new Event();
                event.name = "关闭";
                event.date=date;
                event.type = Event.OFF;
                long count = TimeUtils.getTimeSpan(date,new Date(), TimeConstants.SEC);
                event.time = count;
                data.add(event);
                eventAdapter.setNewData(data);
            }
        });
    }




}
