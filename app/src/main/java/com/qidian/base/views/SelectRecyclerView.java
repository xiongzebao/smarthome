package com.qidian.base.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qidian.base.R;
import com.qidian.base.utils.RecyclerViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiongbin
 * @description:
 * @date : 2020/12/25 15:48
 */

public class SelectRecyclerView extends RecyclerView {


    BaseQuickAdapter adapter;
    ArrayList<BaseModel> list = new ArrayList<>();

    public SelectRecyclerView(@NonNull Context context) {
        super(context);
        initView();
    }

    public SelectRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SelectRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }




    public  List getSelectData(){
        ArrayList selList = new ArrayList();
        for (int i=0;i<list.size();i++){
            if(list.get(i).isSelect()){
                selList.add(list.get(i));
            }
        }
        return  selList;
    }

    private void initView(){
        RecyclerViewUtils.setVerticalLayout(getContext(),this);
        RecyclerViewUtils.addItemDecoration(getContext(),this);
        this.adapter = getMyAdapter();
        setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                select(position);
            }
        });

    }

    private void select(int position){
        for (int i=0;i<list.size();i++){
            list.get(i).setSelect(false);
        }

        list.get(position).setSelect(true);
        adapter.notifyDataSetChanged();
    }


    protected BaseQuickAdapter getMyAdapter(){
        return new DefaultAdpater(R.layout.layout_select_item,list);
    }


    public void setData(List list){
        this.list.addAll(list);
        getAdapter().notifyDataSetChanged();
    }

    static public   class BaseModel{
        private  boolean select =false;
        private  String label;


        public BaseModel(){

        }
        public BaseModel(String label){
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public void toggleSelect(){
            setSelect(!select);
        }

        //子类重写此方法改变显示标签
        protected   String getText(){
            return label;
        }
    }



    public static class DefaultAdpater extends BaseQuickAdapter<BaseModel , BaseViewHolder>{

        public DefaultAdpater(int layoutResId, List<BaseModel> data) {
            super(layoutResId, data);

        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, BaseModel baseModel) {
              //  baseViewHolder.addOnClickListener(R.)
                baseViewHolder.setText(R.id.tv_label,baseModel.getText());
                if(baseModel.isSelect()){
                    baseViewHolder.setImageResource(R.id.iv_select,R.mipmap.ic_select);
                }else{
                    baseViewHolder.setImageResource(R.id.iv_select,R.mipmap.ic_unselect);
                }
        }
    }



}
