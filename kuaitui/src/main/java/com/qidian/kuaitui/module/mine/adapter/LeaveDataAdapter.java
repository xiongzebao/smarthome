package com.qidian.kuaitui.module.mine.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.qidian.kuaitui.R;

import com.qidian.kuaitui.module.job.adapter.BaseRecyclerViewAdapter;
import com.qidian.kuaitui.module.job.adapter.RecyclerViewHolder;
import com.qidian.kuaitui.module.job.model.ReceiptListBean;

import java.util.List;


public class LeaveDataAdapter extends BaseRecyclerViewAdapter<ReceiptListBean> {

    private OnDeleteClickLister mDeleteClickListener;
    private int type =1;

    public LeaveDataAdapter(Context context, List<ReceiptListBean> data, int type) {
        super(context, data, R.layout.layout_leave_item);
        this.type = type;
    }

    @Override
    protected void onBindData(RecyclerViewHolder holder, ReceiptListBean bean, final int position) {
        TextView tv_project_name = (TextView) holder.getView(R.id.project_name);
        TextView phone = (TextView) holder.getView(R.id.phone);
        TextView name = (TextView) holder.getView(R.id.name);
        TextView receiptTime = (TextView) holder.getView(R.id.receiptTime);
        TextView tv_recruit_channel = (TextView) holder.getView(R.id.tv_recruit_channel);

        TextView tv_btn1 = (TextView) holder.getView(R.id.tv_btn1);
        TextView tv_btn2 = (TextView) holder.getView(R.id.tv_btn2);


        tv_project_name.setText(bean.getFunctionName());
        phone.setText(bean.getMemCellPhone());
        name.setText(bean.getMemRealName());
        if(type==4){
           // receiptTime.setText(bean.get);
        }else{
            receiptTime.setText(bean.getInterviewDate());
        }
        tv_recruit_channel.setText(bean.getChannelName());


        tv_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDeleteClickListener!=null){
                    mDeleteClickListener.onDeleteClick(v,position,1);
                }

            }
        });
    }

    public void setOnDeleteClickListener(OnDeleteClickLister listener) {
        this.mDeleteClickListener = listener;
    }

    public interface OnDeleteClickLister {
        void onDeleteClick(View view, int position, int status);
    }
}
