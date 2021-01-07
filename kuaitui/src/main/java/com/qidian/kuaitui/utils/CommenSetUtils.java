package com.qidian.kuaitui.utils;

import com.qidian.base.utils.SharedInfo;
import com.qidian.kuaitui.module.home.model.ProjectItem;

/**
 * @author xiongbin
 * @description:
 * @date : 2020/12/30 13:13
 */

public class CommenSetUtils {
    public static String getProjectTitle(){
        ProjectItem item = SharedInfo.getInstance().getEntity(ProjectItem.class);
        if(item!=null){

            return item.getFunctionName()+"\n"+item.getProjectName();
        }

        return "";
    }

    public static String getRecruitID(){
        ProjectItem item = SharedInfo.getInstance().getEntity(ProjectItem.class);
        if(item!=null){
            return item.getRecruitID();
        }
        return "";
    }




}
