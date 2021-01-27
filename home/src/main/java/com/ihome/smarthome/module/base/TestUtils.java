package com.ihome.smarthome.module.base;

import com.erongdu.wireless.tools.log.MyLog;
import com.ihome.smarthome.module.base.eventbusmodel.BTMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author xiongbin
 * @description:
 * @date : 2021/1/26 13:39
 */

public class TestUtils {
    public static TestUtils testUtils=null;

    public static void newInstance(){
        testUtils = new TestUtils();
    }

    public void register(){
        EventBus.getDefault().register(this);
    }


    public TestUtils() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BTMessageEvent event) {

        MyLog.e("TestUtils :"+event.getName()+":"+event.getMessage());
    }

    public void unregister(){
        EventBus.getDefault().unregister(this);
    }

}
