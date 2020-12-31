package com.qidian.kuaitui.module.job.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import com.qidian.kuaitui.R;
import com.qidian.kuaitui.module.job.model.ReceiptListBean;

import java.util.List;

/**
 * 清单列表adapter
 * <p>
 * Created by DavidChen on 2018/5/30.
 */

public class InventoryAdapter extends BaseRecyclerViewAdapter<ReceiptListBean> {

    private OnDeleteClickLister mDeleteClickListener;

    public InventoryAdapter(Context context, List<ReceiptListBean> data) {
        super(context, data, R.layout.layout_receipt_list_item);
    }

    @Override
    protected void onBindData(RecyclerViewHolder holder, ReceiptListBean bean, int position) {


    }

    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }
}
