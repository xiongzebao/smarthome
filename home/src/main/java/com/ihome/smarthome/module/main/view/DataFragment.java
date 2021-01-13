package com.ihome.smarthome.module.main.view;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ihome.base.base.BaseFragment;
import com.ihome.base.common.ui.ListFragment;
import com.ihome.smarthome.R;

import java.util.List;

/**
 * @author xiongbin
 * @description: 公共数据展示
 * @date : 2020/12/28 13:28
 */

public class DataFragment extends BaseFragment {

    LinearLayout layout_title;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.frag_data_list,null);
         layout_title = rootView.findViewById(R.id.layout_title);
        return  rootView;
    }



    public void setTitles(List<String> list){
        getLayoutTitleView(getContext(),layout_title,list);
    }

    public static void getLayoutTitleView(Context context, LinearLayout layout, List<String> titles){
        for (int i=0;i<titles.size();i++){
            TextView tv = new TextView(context );
            tv.setText(titles.get(i));
            tv.setTextColor(context.getResources().getColor(R.color.color33));
            tv.setTextSize(13);
            tv.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.gravity= Gravity.CENTER;
            lp.weight=1;
            tv.setLayoutParams(lp);
            layout.addView(tv);
        }
    }


    public static void getLayoutItemView(Context context, LinearLayout layout, List<String> titles){
        for (int i=0;i<titles.size();i++){
            TextView tv = new TextView(context );
            tv.setText(titles.get(i));
            tv.setTextColor(context.getResources().getColor(R.color.color33));
            tv.setTextSize(11);
            tv.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.gravity= Gravity.CENTER;
            lp.weight=1;
            tv.setLayoutParams(lp);
            layout.addView(tv);
        }
    }


    public void setListDatas(List<List<String>> list){
        ListFragment listFragment = (ListFragment) getChildFragmentManager().findFragmentById(R.id.list_fragment);

        listFragment.setLoadRefreshEnable(false);
        listFragment.setAdapter(new DataFragmentAdapter(R.layout.layout_data_list_item,getContext()));
        listFragment.getAdapter().setNewData(list);

    }

    public static class DataFragmentAdapter extends BaseQuickAdapter<List<String>, BaseViewHolder>{

        Context context;
        public DataFragmentAdapter(int layoutResId,Context context) {
            super(layoutResId);
            this.context = context;
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, List<String> item) {
            getLayoutItemView(context, (LinearLayout) helper.itemView,item);
        }
    }

}
