package com.qidian.kuaitui.common;

import com.qidian.base.views.SelectRecyclerView;
import com.qidian.kuaitui.module.home.model.ProjectItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiongbin
 * @description:
 * @date : 2020/12/30 17:44
 */

public class GlobalData {
    public static List<ProjectItem> recruits;

    public static List getRecruitData(){
        ArrayList list = new ArrayList();
        for (int i =0;i<GlobalData.recruits.size();i++){
            list.add(new SelectRecyclerView.DefaultModel(GlobalData.recruits.get(i).getProjectName()));
        }
        return  list;
    }
}
