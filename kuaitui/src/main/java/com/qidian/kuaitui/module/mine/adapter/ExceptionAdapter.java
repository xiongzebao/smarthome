package com.qidian.kuaitui.module.mine.adapter;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qidian.base.views.SelectRecyclerView;
import com.qidian.kuaitui.R;
import com.qidian.kuaitui.module.mine.model.ExceptionBean;

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
        helper.setText(R.id.tv,bean.Name);
        if(item.isSelect()){
            helper.setTextColor(R.id.tv, Color.BLUE);
            helper.setBackgroundRes(R.id.tv,R.drawable.bg_text_item_select);
        }else{
            helper.setTextColor(R.id.tv, Color.RED);
            helper.setBackgroundRes(R.id.tv,R.drawable.bg_text_item_unselect);
        }
    }
}
