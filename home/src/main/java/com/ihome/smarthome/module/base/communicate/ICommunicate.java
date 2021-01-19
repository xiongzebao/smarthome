package com.ihome.smarthome.module.base.communicate;

import android.content.Intent;

import androidx.annotation.Nullable;

public interface ICommunicate {

    interface onMessageLisenter{

        String BT_ACCEPTED = "BT_ACCEPTED";
        String BT_DISCONNECT = "BT_DISCONNECT";
        String BT_CONNECTED = "BT_CONNECTED";
        String SERVER_CONNECTED = "SERVER_CONNECTED";

        void onMessage(String name, String msg);
        void onConnect(String name, String type);
        void onDisConnect(String name,String o);
        void onError(String name,String err_msg);
        void onConnectFailed(String name,String err_msg);
    }
    void connect(String name);
    void disConnect(String name);
    void destroy( );
    boolean isConnected(String name);
    void sendMessage(String name,String msg);
    void setOnMessageLisenter(onMessageLisenter onMessageLisenter);
    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);


}
