package com.qidian.kuaitui.common;

import com.qidian.base.views.SelectRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiongbin
 * @description:
 * @date : 2020/12/30 17:40
 */

public class StaticData {
     public static List getChannelData(){
        ArrayList list = new ArrayList();
        list.add(new SelectRecyclerView.DefaultModel("自媒体"));
        list.add(new SelectRecyclerView.DefaultModel("融媒体"));
        list.add(new SelectRecyclerView.DefaultModel("代理"));
        list.add(new SelectRecyclerView.DefaultModel("推手"));
        list.add(new SelectRecyclerView.DefaultModel("农村电商"));
        list.add(new SelectRecyclerView.DefaultModel("校园"));

        return  list;
    }




}
