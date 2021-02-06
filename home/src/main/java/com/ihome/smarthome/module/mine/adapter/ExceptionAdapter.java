package com.ihome.smarthome.module.mine.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.erongdu.wireless.tools.utils.ActivityManager;
import com.ihome.base.views.SelectRecyclerView;
import com.ihome.smarthome.R;
import com.ihome.smarthome.module.mine.model.ExceptionBean;

import java.util.List;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/4 15:25
 */

public class ExceptionAdapter extends BaseQuickAdapter<SelectRecyclerView.BaseModel, BaseViewHolder> {


    public ExceptionAdapter(int layoutResId, @Nullable List<SelectRecyclerView.BaseModel> data) {
        super(layoutResId, data);
    }

    public ExceptionAdapter(@Nullable List<SelectRecyclerView.BaseModel> data) {

        super(data);
    }

    public ExceptionAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SelectRecyclerView.BaseModel item) {
        ExceptionBean bean = (ExceptionBean) item;
        helper.setText(R.id.tv_message,bean.Name);
        if(item.isSelect()){
            helper.setTextColor(R.id.tv_message, ActivityManager.peek().getResources().getColor(R.color.color_4e));
            helper.setBackgroundRes(R.id.tv_message,R.drawable.bg_text_item_select);
        }else{
            helper.setTextColor(R.id.tv_message, ActivityManager.peek().getResources().getColor(R.color.color66));
            helper.setBackgroundRes(R.id.tv_message,R.drawable.bg_text_item_unselect);
        }
    }
}
