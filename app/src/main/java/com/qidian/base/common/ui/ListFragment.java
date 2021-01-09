package com.qidian.base.common.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeRefreshHeaderLayout;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.erongdu.wireless.network.entity.PageMo;
import com.erongdu.wireless.tools.log.MyLog;
import com.erongdu.wireless.views.PlaceholderLayout;
import com.qidian.base.R;
import com.qidian.base.base.BaseFragment;


import com.qidian.base.databinding.FragListBinding;
import com.qidian.base.utils.RecyclerViewUtils;


public class ListFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener {


      FragListBinding binding;
    BaseQuickAdapter adapter;
    SwipeToLoadLayout swipeToLoadLayout;
    OnLoadListener loadListener;
    PageMo pageMo = new PageMo();
    ListFragmentModel model = new ListFragmentModel(this);




    public static ListFragment newInstance(BaseQuickAdapter adapter) {
        ListFragment fragment = new ListFragment();
        fragment.adapter = adapter;
        return fragment;
    }



    public RecyclerView getRecyclerView(){
        return   binding.swipeTarget;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_list, null, false);
        RecyclerViewUtils.setVerticalLayout(getContext(), binding.swipeTarget);
        RecyclerViewUtils.addItemDecoration(getContext(),getRecyclerView());
        binding.setModel(model);
        this.swipeToLoadLayout = binding.swipe;
        this.swipeToLoadLayout.setOnLoadMoreListener(this);
        this.swipeToLoadLayout.setOnRefreshListener(this);
        return binding.getRoot();
    }


    public ObservableInt getPlaceHolderState(){
        return model.placeholderState;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        binding.swipeTarget.setAdapter(adapter);
        this.adapter =  adapter;
    }

    public void loadFinish() {
        this.swipeToLoadLayout.setLoadingMore(false);
        this.swipeToLoadLayout.setRefreshing(false);
    }


    public void setLoadRefreshEnable(boolean enable){
        this.swipeToLoadLayout.setLoadMoreEnabled(enable);
        this.swipeToLoadLayout.setRefreshEnabled(enable);
    }

    public void setOnLoadListener(OnLoadListener listener) {
        this.loadListener = listener;
    }


    @Override
    public void onLoadMore() {
        pageMo.loadMore();
        if (loadListener != null) {
            loadListener.onLoadPage(pageMo);
        }
    }

    @Override
    public void onRefresh() {
        pageMo.refresh();
        if (loadListener != null) {
            loadListener.onLoadPage(pageMo);
        }
    }

    public  BaseQuickAdapter getAdapter() {
        return adapter;
    }


    @Override
    public void onReload(View v) {
        super.onReload(v);
        loadListener.onLoadPage(pageMo);
    }


}