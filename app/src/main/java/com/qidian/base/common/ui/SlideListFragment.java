package com.qidian.base.common.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.erongdu.wireless.network.entity.PageMo;
import com.qidian.base.R;
import com.qidian.base.base.BaseFragment;
import com.qidian.base.databinding.FragListBinding;
import com.qidian.base.databinding.FragListSlideBinding;
import com.qidian.base.utils.RecyclerViewUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author xiongbin
 * @description:这是一个类似QQ侧滑滑出顶置和删除的自定义Fragment
 * @date : 2020/12/24 15:21
 */

public class SlideListFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener {


    protected FragListSlideBinding binding;
    protected RecyclerView.Adapter adapter;
    protected  SwipeToLoadLayout swipeToLoadLayout;
    protected OnLoadListener loadListener;
    protected PageMo pageMo = new PageMo();
    protected ListFragmentModel model = new ListFragmentModel(this);





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_list_slide, null, false);
        RecyclerViewUtils.setVerticalLayout(getContext(), binding.swipeTarget);

        binding.setModel(model );
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

    public void setAdapter(RecyclerView.Adapter adapter) {
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

    public void setLoadEnable(boolean enable){
        this.swipeToLoadLayout.setLoadMoreEnabled(enable);
    }
    public void setRefreshEnable(boolean enable){
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

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }


    @Override
    public void onReload(View v) {
        super.onReload(v);
        loadListener.onLoadPage(pageMo);
    }


}
