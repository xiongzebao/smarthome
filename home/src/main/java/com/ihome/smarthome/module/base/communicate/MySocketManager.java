package com.ihome.smarthome.module.base.communicate;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.ihome.smarthome.module.base.Constants;


import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class MySocketManager implements ICommunicate{
    String TAG = "MySocketManager";

    private boolean isConnected = false;
    private Socket mSocket;
    private Map<String, Emitter.Listener> listers = new HashMap<>();
    private onMessageListener listener =null;
    private Context context;



    public MySocketManager(Context context) {
        this.context = context;
        {
            try {
                mSocket = IO.socket(Constants.CHAT_SERVER_URL);
            } catch (URISyntaxException e) {
                Log.e("xiong", e.getMessage());
                throw new RuntimeException(e);
            }
        }
        on(io.socket.client.Socket.EVENT_CONNECT, onConnect);
        on(io.socket.client.Socket.EVENT_DISCONNECT, onDisconnect);
        on(io.socket.client.Socket.EVENT_CONNECT_ERROR, onConnectError);
        on(Socket.EVENT_RECONNECT_ERROR, onConnectError);
        on(Socket.EVENT_RECONNECT_FAILED, onConnectError);
        on(io.socket.client.Socket.EVENT_CONNECT_TIMEOUT, onConnectError);

        on(Constants.EVENT_MSG,onNewMessage);
    }


    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

          //  listener.onMessage("new message to ...");
        }
    };


    public void connect() {
        mSocket.connect();
    }

    public void on(String event, Emitter.Listener listener) {
        if (listers.containsKey(event)) {
            return;
        }
        mSocket.on(event, listener);
        listers.put(event, listener);
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Log.e("xiong", "onConnect");
            if (!isConnected) {
        /*        mSocket.emit(Constants.EVENT_CONNECTED, new MessageEvent("" +
                        "控制端连接成功:" + Constants.getUserName(), Constants.EVENT_CONNECTED).toJson());*/

                isConnected = true;
                listener.onConnect("服务器连接成功", onMessageListener.SERVER_CONNECTED);
            }
        }
    };


    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Log.i(TAG, "diconnected");
            isConnected = false;
            listener.onDisConnect("","服务器断开连接，请检查网络");
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.e(TAG, "Error connecting:" + args[0]);
            listener.onError("","网络连接失败");
        }
    };


    @Override
    public void connect(String name) {

    }

    @Override
    public void disConnect(String name) {
        if (isConnected) {
            for (Map.Entry<String, Emitter.Listener> entry : listers.entrySet()) {
                mSocket.off(entry.getKey(), entry.getValue());
            }
            mSocket.disconnect();
            isConnected = false;
        }
    }

/*
    @Override
    public void destroy() {
        disConnect( "");
        listener =null;
        mSocket.close();
    }
*/


    @Override
    public boolean isConnected(String name) {
        return isConnected;
    }

    @Override
    public void sendMessage(String name, String msg) {

    }

    @Override
    public void setOnMessageLisenter(String name, onMessageListener onMessageListener) {

    }


    public void sendMessage(String message) {
        if (!mSocket.connected()) {
            ToastUtils.showLong("Socket unconnected!");
            return;
        }
        mSocket.emit(Constants.EVENT_MSG, message);
    }

/*    @Override
    public void setOnMessageLisenter(onMessageListener onMessageListener) {
        this.listener = onMessageListener;
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }

}
