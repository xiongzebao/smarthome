package com.ihome.smarthome.module.adapter.provider;

import android.widget.Toast;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.ihome.smarthome.R;
import com.ihome.smarthome.module.adapter.DeviceListAdapter;
import com.ihome.smarthome.module.base.DeviceItem;
import com.ihome.smarthome.module.model.ImageMessage;

/**
 * @author ChayChan
 * @description 图片消息条目的provider
 * @date 2018/3/21  14:43
 */
public class DeviceItemProvider extends BaseItemProvider<DeviceItem, BaseViewHolder> {

    @Override
    public int viewType() {
        return DeviceListAdapter.TYPE_DEVICE_BASE;
    }

    @Override
    public int layout() {
        return R.layout.layout_device_list_item;
    }

    @Override
    public void convert(BaseViewHolder helper, DeviceItem data, int position) {
        //处理相关业务逻辑
     /*   ImageView iv = helper.getView(R.id.iv_img);
        Glide.with(mContext).load(data.imgUrl).into(iv);*/
    }

    @Override
    public void onClick(BaseViewHolder helper, DeviceItem data, int position) {
        //单击事件
        Toast.makeText(mContext, "Click: " + data.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(BaseViewHolder helper, DeviceItem data, int position) {
        //长按事件
        Toast.makeText(mContext, "longClick: " + data.getName(), Toast.LENGTH_SHORT).show();
        return true;
    }
}
