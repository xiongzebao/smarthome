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
        TextView tv_project_name = (TextView) holder.getView(R.id.tv_project_name);
        TextView phone = (TextView) holder.getView(R.id.phone);
        TextView name = (TextView) holder.getView(R.id.name);
        TextView receiptTime = (TextView) holder.getView(R.id.receiptTime);
        TextView tv_recruit_channel = (TextView) holder.getView(R.id.tv_recruit_channel);

        tv_project_name.setText(bean.getFunctionName());
        phone.setText(bean.getMemCellPhone());
        name.setText(bean.getMemRealName());
        receiptTime.setText(bean.getInterviewDate());
        tv_recruit_channel.setText(bean.getChannelName());


    }

    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position);
    }
}
