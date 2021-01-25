package com.ihome.smarthome.module.base.communicate;

import android.content.Intent;

import androidx.annotation.Nullable;

public class CommunicateManager implements ICommunicate {


    @Override
    public void connect(String name) {

    }

    @Override
    public void disConnect(String name) {

    }



    @Override
    public boolean isConnected(String name) {
        return false;
    }

    @Override
    public void sendMessage(String name, String msg) {

    }

    @Override
    public void setOnMessageLisenter(String name, onMessageListener onMessageListener) {

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }
}
