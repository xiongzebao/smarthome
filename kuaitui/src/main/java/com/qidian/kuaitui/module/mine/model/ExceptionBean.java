package com.qidian.kuaitui.module.mine.model;

import com.qidian.base.views.SelectRecyclerView;

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
