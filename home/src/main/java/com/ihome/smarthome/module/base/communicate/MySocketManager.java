
package com.ihome.smarthome.module.base.communicate;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.erongdu.wireless.tools.log.MyLog;
import com.ihome.smarthome.base.MyApplication;
import com.ihome.smarthome.module.base.Constants;
import com.ihome.smarthome.utils.EventBusUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.Polling;
import io.socket.engineio.client.transports.WebSocket;


public class MySocketManager implements ICommunicate {
    String TAG = "xiong";
    private boolean isConnected = false;
    private Socket mSocket;
    HashMap<String, List<String>> header = new HashMap<>();
    HashMap<String,String> auth = new HashMap<>();
    private static MySocketManager instance = null;

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            sendParamLog(args);
            EventBusUtils.sendSucessLog("websocket connected!","1");
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            sendParamLog(args);
            EventBusUtils.sendFailLog("websocket disconnected!","1");
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            sendParamLog(args);
            EventBusUtils.sendFailLog("websocket connect error!","1");
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            sendParamLog(args);

        }
    };

    public static MySocketManager getInstance() {
        if (instance == null) {
            instance = new MySocketManager(MyApplication.application);
        }
        return instance;
    }


    public void sendMessage(String msg){
        mSocket.emit(Constants.CLIENT_MSG,msg);

    }

    private void sendParamLog(Object...args){
        for (int i = 0; i < args.length; i++) {
            EventBusUtils.sendDeBugLog(args[i] + "");
        }
    }



    public MySocketManager(Context context) {
         List<String>  list=new ArrayList<>();
         list.add("android");
        header.put("device_type",list);

        auth.put("xiongbin","123456");
        IO.Options options = IO.Options.builder()
                // IO factory options
                .setForceNew(false)
                .setMultiplex(true)
                // low-level engine options
                .setTransports(new String[]{Polling.NAME, WebSocket.NAME})
                .setUpgrade(true)
                .setRememberUpgrade(false)
                .setPath("/socket.io/")
                .setQuery("device_type=phone")
                .setExtraHeaders(header)

                // Manager options
                .setReconnection(true)
                .setReconnectionAttempts(3)
                .setReconnectionDelay(5_000)
                .setReconnectionDelayMax(50_000)
                .setRandomizationFactor(0.5)
                .setTimeout(20_000)
                .setSecure(true)
                // Socket options
                .setAuth(auth)
                .build();
        mSocket = IO.socket(URI.create(Constants.CHAT_SERVER_URL), options);
        on(io.socket.client.Socket.EVENT_CONNECT, onConnect);
        on(io.socket.client.Socket.EVENT_DISCONNECT, onDisconnect);
        on(io.socket.client.Socket.EVENT_CONNECT_ERROR, onConnectError);
        on(Constants.SERVER_MSG, onNewMessage);

    }




    public void connect() {
        mSocket.connect();
    }

    public void on(String event, Emitter.Listener listener) {

        mSocket.on(event, listener);

    }


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
}


