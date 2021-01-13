package com.ihome.smarthome.module.job.vm;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihome.base.base.BaseVM;

import com.ihome.base.views.EditText_Clear;
import com.ihome.smarthome.R;
import com.ihome.smarthome.databinding.ActReceptDataBinding;
import com.ihome.smarthome.module.job.view.AddNewUserActivity;
import com.ihome.smarthome.module.job.view.ReceiptDataActivity;
import com.ihome.smarthome.module.job.view.ReceiptDataFragment;

public class ReceiptViewModel extends BaseVM  {

    ActReceptDataBinding binding;
    ReceiptDataFragment listFragment;
    ImageView iv_right;
    EditText_Clear searchView;

    public ReceiptViewModel(final ActReceptDataBinding binding, final ReceiptDataActivity act) {
        this.binding = binding;
        searchView = binding.etSearch;
        listFragment = (ReceiptDataFragment) act.getSupportFragmentManager().findFragmentById(R.id.receipt_list_fragment);
        iv_right = binding.layoutHeader.findViewById(R.id.iv_right);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(act,AddNewUserActivity.class);
                act.startActivityForResult(intent,1);

            }
        });

        this.binding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== 3){
                 search();
                }
                return false;
            }
        });
    }

    public void search(){
        listFragment.search(searchView.getText().toString());
    }

}
