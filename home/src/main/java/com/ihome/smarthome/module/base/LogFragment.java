package com.ihome.smarthome.module.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.erongdu.wireless.network.entity.PageMo;
import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.ihome.base.common.ui.ListFragment;
import com.ihome.base.common.ui.OnLoadListener;
import com.ihome.base.utils.ScenceUtils;
import com.ihome.smarthome.R;
import com.ihome.smarthome.database.showlog.DbController;
import com.ihome.smarthome.database.showlog.ShowLogEntity;
import com.ihome.smarthome.module.base.communicate.ICommunicate;
import com.ihome.smarthome.module.base.communicate.MySocketManager;
import com.ihome.smarthome.module.base.eventbusmodel.BTMessageEvent;
import com.ihome.smarthome.module.base.eventbusmodel.BaseMessageEvent;
import com.ihome.smarthome.module.base.eventbusmodel.LogEvent;
import com.ihome.smarthome.utils.EventBusUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/2/5 11:06
 */

public class LogFragment  extends ListFragment {


    int LogType;

    public LogFragment(int logType) {
        LogType = logType;
    }

    ArrayList<ShowLogEntity> list =  new ArrayList<>();

    class  MyAdapter extends BaseQuickAdapter<ShowLogEntity, BaseViewHolder>{

        public MyAdapter(int layoutResId, @Nullable List<ShowLogEntity> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, ShowLogEntity item) {
            helper.setText(R.id.tv_message,item.getMsg());
            helper.setText(R.id.tv_date,item.getDate());
            if(item.getEvent()!=null){
                helper.setText(R.id.tv_event,item.getEvent());
                helper.setVisible(R.id.tv_event,true);
            }else{
                helper.setVisible(R.id.tv_event,false);
            }
        }
    }






    public static LogFragment newInstance(int logType){
        LogFragment fragment = new LogFragment(logType);
        return fragment;
    }


    @Override
    public void initData() {
         setAdapter(new MyAdapter(R.layout.layout_log_item,list));


         setOnLoadListener(new OnLoadListener() {
             @Override
             public void onLoadPage(PageMo pageNo) {
                 refresh();
             }
         });

        EventBus.getDefault().register(this);

    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            refresh();
            scrollToBottom();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BTMessageEvent event) {
        switch (event.getMessageType()) {
            case BTMessageEvent.REFRESH_NOTICE:
                refresh();
                scrollToBottom();
                break;
            default:
                break;
        }
    }


    public void refresh(){
        if(LogType==0){
            List<ShowLogEntity> data =  DbController.getInstance(getContext()).searchAll();
            if(getAdapter()!=null){
                getAdapter().setNewData(data);
            }
            return;
        }
      List<ShowLogEntity> data =  DbController.getInstance(getContext()).searchByWhere(LogType);
      if(getAdapter()!=null){
          getAdapter().setNewData(data);
      }
   }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }
}
