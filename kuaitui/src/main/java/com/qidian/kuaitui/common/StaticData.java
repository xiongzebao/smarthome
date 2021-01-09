package com.qidian.kuaitui.common;

import com.qidian.base.views.SelectRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xiongbin
 * @description:
 * @date : 2020/12/30 17:40
 */

public class StaticData {
     public static List getChannelData(){
        ArrayList list = new ArrayList();
        list.add(new SelectRecyclerView.BaseModel("自媒体"));
        list.add(new SelectRecyclerView.BaseModel("融媒体"));
        list.add(new SelectRecyclerView.BaseModel("代理"));
        list.add(new SelectRecyclerView.BaseModel("推手"));
        list.add(new SelectRecyclerView.BaseModel("农村电商"));
        list.add(new SelectRecyclerView.BaseModel("校园"));

        return  list;
    }

    public static List<String> getReceiptTitle(){
        return Arrays.asList("渠道名称","接待总数","已到场数","未到场数","到场率");
    }

    public static List<String> getInterViewTitle(){
        return Arrays.asList("渠道名称","面试总数","已通过数","未通过数","面试率");
    }

    public static List<String> getEntryTitle(){
        return Arrays.asList("渠道名称","应入职数","已入职数","未入职数","入职率");
    }

    public static List<String> getLeaveTitle(){
        return Arrays.asList("渠道名称","总入职数","今日离职","未离职数","离职率");
    }




}
