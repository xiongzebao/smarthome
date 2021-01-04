package com.qidian.kuaitui.module.mine.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qidian.base.views.SelectRecyclerView;
import com.qidian.kuaitui.R;
import com.qidian.kuaitui.module.mine.adapter.ExceptionAdapter;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/4 15:41
 */

public class ExceptionRecyclerView extends SelectRecyclerView {


    public ExceptionRecyclerView(@NonNull Context context) {
        super(context);
    }

    public ExceptionRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExceptionRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected BaseQuickAdapter getMyAdapter() {
        return new ExceptionAdapter(R.layout.layout_exception_item,list);

    }
}
