package com.ihome.smarthome.module.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ihome.base.common.ui.ListFragment;
import com.ihome.smarthome.R;
import com.ihome.smarthome.database.showlog.DbController;
import com.ihome.smarthome.database.showlog.ShowLogEntity;

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


    @Override
    public void initData() {
         setAdapter(new MyAdapter(R.layout.layout_log_item,list));

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


}
