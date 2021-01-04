package com.qidian.kuaitui.module.job.view;
import androidx.databinding.DataBindingUtil;
import com.qidian.base.base.BaseActivity;
import com.qidian.kuaitui.R;
import com.qidian.kuaitui.databinding.ActReceptDataBinding;
import com.qidian.kuaitui.module.job.adapter.ReceiptListAdapter;
import com.qidian.kuaitui.module.job.vm.ReceiptViewModel;
import com.qidian.kuaitui.utils.CommenSetUtils;

public class ReceiptDataActivity extends BaseActivity {


    public static final int RECEIPT=1;
    public static final int INTERVIEW=2;
    public static final int ENTRY=3;
    public static final int HISTORY=4;

    ActReceptDataBinding binding;

    @Override
    protected void bindView() {
        binding =    DataBindingUtil.setContentView(this, R.layout.act_recept_data);


        binding.setViewModel(new ReceiptViewModel(binding,this));
        setTitle(CommenSetUtils.getProjectTitle());

    }





}
