package com.ihome.base.utils;

import android.content.Context;

import com.erongdu.wireless.tools.utils.ActivityManager;
import com.ihome.base.views.CutscenesProgress;

import java.util.HashMap;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/13 9:12
 */

public class ScenceUtils {
    private  static HashMap<Context,CutscenesProgress>  scenesMap = new HashMap<>();
    static public void  showCutscence(Context context,String msg ){
        if(scenesMap.containsKey(context)){
            CutscenesProgress cutscenesProgress= scenesMap.get(context);
            cutscenesProgress.setMessage(msg);
            cutscenesProgress.show();
        }else{
            CutscenesProgress cutscenesProgress=  CutscenesProgress.createDialog(context);
            scenesMap.put(context,cutscenesProgress);
            cutscenesProgress.setMessage(msg);
            cutscenesProgress.show();
        }
    }

   static public void closeCutScence(Context context){

        if(scenesMap.containsKey(context)){
            CutscenesProgress cutscenesProgress= scenesMap.get(context);
            cutscenesProgress.dismiss();
            scenesMap.remove(context);
        }
    }
}
