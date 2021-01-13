package com.ihome.smarthome.module.mine.model;

import com.ihome.base.views.SelectRecyclerView;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/4 15:31
 */

public class ExceptionBean extends SelectRecyclerView.BaseModel {
    public String Value;
    public String Name;

    @Override
    protected String getText() {
        return Name;
    }
}
