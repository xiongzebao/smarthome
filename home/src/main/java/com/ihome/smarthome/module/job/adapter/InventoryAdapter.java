package com.ihome.smarthome.module.job.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.erongdu.wireless.tools.utils.ActivityManager;
import com.erongdu.wireless.tools.utils.DensityUtil;
import com.ihome.smarthome.R;
import com.ihome.smarthome.module.job.model.ReceiptListBean;

import java.util.List;


public class InventoryAdapter extends BaseRecyclerViewAdapter<ReceiptListBean> {

    private OnDeleteClickLister mDeleteClickListener;
    private int type = 1;

    public InventoryAdapter(Context context, List<ReceiptListBean> data, int type) {
        super(context, data, R.layout.layout_receipt_list_item);
        this.type = type;
    }

    @Override
    protected void onBindData(RecyclerViewHolder holder, ReceiptListBean bean, final int position) {
        TextView tv_project_name = (TextView) holder.getView(R.id.tv_project_name);
        TextView phone = (TextView) holder.getView(R.id.phone);
        TextView name = (TextView) holder.getView(R.id.name);
        TextView receiptTime = (TextView) holder.getView(R.id.receiptTime);
        TextView tv_status = (TextView) holder.getView(R.id.tv_status);
        TextView tv_recruit_channel = (TextView) holder.getView(R.id.tv_recruit_channel);

        TextView tv_btn1 = (TextView) holder.getView(R.id.tv_btn1);
        TextView tv_btn2 = (TextView) holder.getView(R.id.tv_btn2);

        switch (type) {
            case 1: {
                tv_btn1.setText("已到场");
                tv_btn2.setText("未到场");
                break;
            }
            case 2: {
                tv_btn1.setText("已通过");
                tv_btn2.setText("未通过");
                break;
            }
            case 3: {
                tv_btn1.setText("已入职");
                tv_btn2.setText("未入职");
                break;
            }

            case 4://历史汇总
                tv_btn1.setVisibility(View.GONE);
                tv_btn2.setVisibility(View.GONE);
                LinearLayout ll1 = (LinearLayout) holder.getView(R.id.ll_slide_bar);
                LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) ll1.getLayoutParams();
                lp1.width = DensityUtil.dp2px(ActivityManager.peek(), 0);
                ll1.setLayoutParams(lp1);
                break;
            case 5: {//离职
                LinearLayout ll = (LinearLayout) holder.getView(R.id.ll_slide_bar);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ll.getLayoutParams();
                lp.width = DensityUtil.dp2px(ActivityManager.peek(), 100);
                ll.setLayoutParams(lp);
                tv_btn1.setVisibility(View.GONE);
                tv_btn2.setVisibility(View.VISIBLE);
                tv_btn2.setText("离职");

                break;
            }
        }
        tv_project_name.setText(bean.getFunctionName());
        phone.setText(bean.getMemCellPhone());
        name.setText(bean.getMemRealName());
        receiptTime.setText(bean.getChannelName());
        if (type == 4) {
            tv_status.setVisibility(View.VISIBLE);
            tv_status.setText(bean.getStatusName());
            switch (bean.getStatusName()) {
                case "已到场":
                    tv_status.setBackgroundResource(R.mipmap.ic_yidaochang);
                    break;
                case "已通过":
                    tv_status.setBackgroundResource(R.mipmap.ic_yitonguo);
                    break;
                case "已入职":
                    tv_status.setBackgroundResource(R.mipmap.ic_yiruzhi);
                    break;
                case "已离职":
                    tv_status.setBackgroundResource(R.mipmap.ic_yilizhi);
                    break;
                default:
                    tv_status.setBackgroundResource(R.mipmap.ic_status_default);
                    break;
            }
        } else {
            tv_status.setVisibility(View.GONE);
        }
        tv_recruit_channel.setText(bean.getInterviewDate());
        tv_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDeleteClickListener != null) {

                    mDeleteClickListener.onDeleteClick(v, position, 1);

                }

            }
        });

        tv_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDeleteClickListener != null) {
                    mDeleteClickListener.onDeleteClick(v, position, 2);
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
