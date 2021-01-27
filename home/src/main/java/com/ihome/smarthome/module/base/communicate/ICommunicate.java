package com.ihome.smarthome.module.base.communicate;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.ihome.smarthome.module.base.eventbusmodel.BaseMessageEvent;

public interface ICommunicate {

    interface  Listener{
       void onMessage(BaseMessageEvent event);

    }
    void connect(String name);
    void disConnect(String name);
    boolean isConnected(String name);

}
