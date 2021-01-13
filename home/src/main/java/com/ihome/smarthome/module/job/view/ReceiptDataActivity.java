package com.ihome.smarthome.module.job.view;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import com.ihome.base.base.BaseActivity;
import com.ihome.smarthome.R;
import com.ihome.smarthome.databinding.ActReceptDataBinding;
import com.ihome.smarthome.module.job.vm.ReceiptViewModel;
import com.ihome.smarthome.utils.CommenSetUtils;

public class ReceiptDataActivity extends BaseActivity {


    public static final int RECEIPT=1;
    public static final int INTERVIEW=2;
    public static final int ENTRY=3;
    public static final int HISTORY=4;
    public static final int SUMENTRY=5;//在职
    public static final int REFRESH=4;

    ActReceptDataBinding binding;

    @Override
    protected void bindView() {
        binding =    DataBindingUtil.setContentView(this, R.layout.act_recept_data);

        binding.setViewModel(new ReceiptViewModel(binding,this));
        setTitle(CommenSetUtils.getProjectTitle());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==REFRESH){
            binding.getViewModel().search();
        }
    }
}
