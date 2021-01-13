package com.ihome.smarthome.module.job.model;

import com.ihome.base.views.SelectRecyclerView;

/**
 * @author xiongbin
 * @description:
 * @date : 2020/12/31 10:58
 */

public class ChannelBean extends SelectRecyclerView.BaseModel {
    public String ChannelName;
    public String ChannelValue;

    @Override
    protected String getText() {
        return  ChannelName;
    }
}
