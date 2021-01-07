package com.qidian.kuaitui.module.job.vm;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.erongdu.wireless.network.entity.PageMo;
import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.tools.utils.ActivityManager;
import com.erongdu.wireless.tools.utils.ToastUtil;
import com.erongdu.wireless.views.PlaceholderLayout;
import com.qidian.base.base.BaseVM;

import com.qidian.base.common.ui.OnLoadListener;
import com.qidian.base.common.ui.SlideListFragment;
import com.qidian.base.network.NetworkUtil;
import com.qidian.base.views.EditText_Clear;
import com.qidian.kuaitui.R;
import com.qidian.kuaitui.api.ApiService;
import com.qidian.kuaitui.api.STClient;
import com.qidian.kuaitui.base.KTRequestCallBack;
import com.qidian.kuaitui.base.Page;
import com.qidian.kuaitui.base.ResBase;
import com.qidian.kuaitui.common.KTConstant;
import com.qidian.kuaitui.databinding.ActReceptDataBinding;
import com.qidian.kuaitui.module.job.adapter.InventoryAdapter;
import com.qidian.kuaitui.module.job.model.ReceiptListBean;
import com.qidian.kuaitui.module.job.model.ReqInterViewParam;
import com.qidian.kuaitui.module.job.view.AddNewUserActivity;
import com.qidian.kuaitui.module.job.view.ReceiptDataActivity;
import com.qidian.kuaitui.module.job.view.ReceiptDataFragment;
import com.qidian.kuaitui.module.main.model.JobHomeBean;
import com.qidian.kuaitui.utils.CommenSetUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

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
