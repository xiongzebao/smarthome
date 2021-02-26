package com.ihome.smarthome.module.base.communicate;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;

import com.blankj.utilcode.util.DeviceUtils;
import com.ihome.smarthome.base.MyApplication;
import com.ihome.smarthome.module.base.Constants;
import com.ihome.smarthome.module.base.eventbusmodel.BaseMessageEvent;
import com.ihome.smarthome.module.base.eventbusmodel.LogEvent;
import com.ihome.smarthome.utils.EventBusUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;
import io.socket.engineio.client.transports.Polling;
import io.socket.engineio.client.transports.WebSocket;

public class MySocketManager implements ICommunicate {

    public static String TAG = "MySocketManager";
    private boolean isConnected = false;
    private Socket mSocket;
    private HashMap<String, List<String>> header = new HashMap<>();
    private HashMap<String, String> auth = new HashMap<>();
    private static MySocketManager instance = null;
    private ArrayList<Pair<String, Listener>> listeners = new ArrayList<>();
    private HashSet<String> eventLogs = new HashSet<>();//记录指定事件日志
    private int counter = 0;
    private long interval = 60000;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            counter++;
           // EventBusUtils.sendLog(TAG,"handler ping",LogEvent.LOG_DEBUG,true);
            EventBusUtils.saveToDatabase(TAG,"handler",LogEvent.LOG_DEBUG);
            if (counter%5==0) {
                EventBusUtils.sendLog(TAG, "心跳连接超时，重新连接！", LogEvent.LOG_IMPORTANT, true);
                reConnect();
            }
            sendPing();
            handler.postDelayed(this, interval);
        }
    };


    private void startTimer() {
        counter=0;
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, interval);
    }

    public void addListenLog(String event) {
        eventLogs.add(event);
    }

    public void removeListenLog(String event) {
        eventLogs.remove(event);
    }

    private Emitter.Listener onConnect = args -> {
        EventBusUtils.sendLog(TAG, "websocket connected:" + paramsToString(args), LogEvent.LOG_IMPORTANT, true);
        startTimer();


    };
    private Emitter.Listener onDisconnect = args -> {
        EventBusUtils.sendLog(TAG, "websocket Disconnect:" + paramsToString(args), LogEvent.LOG_IMPORTANT, true);

    };

    private Emitter.Listener onConnectError = args -> {
        EventBusUtils.sendLog(TAG, "websocket connect error:" + paramsToString(args), LogEvent.LOG_IMPORTANT, true);

    };


    private Emitter.Listener onPing = args -> {
        EventBusUtils.sendLog(TAG, "onPing", LogEvent.LOG_DEBUG, true);
        counter = 0;
    };

    private Emitter.Listener server_msg = args -> {
        EventBusUtils.sendLog(TAG, paramsToString(args), LogEvent.LOG_DEBUG, true);
        counter = 0;
    };

    public static MySocketManager getInstance() {
        if (instance == null) {
            instance = new MySocketManager(MyApplication.application);
        }
        return instance;
    }

    public void sendMessage(String msg) {
        mSocket.emit(Constants.CLIENT_MSG, msg);
    }

    public void sendPing() {
        if(mSocket.connected()){
            mSocket.emit(Constants.PING, "p");
        }
    }

    private String paramsToString(Object... args) {
        String str = "";
        for (int i = 0; i < args.length; i++) {
            str += args[i];
        }
        return str;
    }

    public MySocketManager(Context context) {
        List<String> list = new ArrayList<>();
        list.add("android");
        list.add(DeviceUtils.getAndroidID());
        list.add(DeviceUtils.getModel());
        header.put("device", list);
        auth.put("xiongbin", "123456");
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
                // .setExtraHeaders(singletonMap("authorization", singletonList("bearer 1234")))
                .setExtraHeaders(header)
                // Manager options
                .setReconnection(true)
                .setReconnectionAttempts(3)
                .setReconnectionDelay(3_000)
                .setReconnectionDelayMax(5_000)
                .setRandomizationFactor(0.5)
                .setTimeout(10_000)
                .setSecure(true)
                // Socket options
                .setAuth(auth)
                .build();
        mSocket = IO.socket(URI.create(Constants.CHAT_SERVER_URL), options);
        on(io.socket.client.Socket.EVENT_CONNECT, onConnect);
        on(io.socket.client.Socket.EVENT_DISCONNECT, onDisconnect);
        on(io.socket.client.Socket.EVENT_CONNECT_ERROR, onConnectError);
        on(Constants.PING, onPing);



        mSocket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Transport transport = (Transport) args[0];
                transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                       // Log.v(TAG, "Caught EVENT_REQUEST_HEADERS after EVENT_TRANSPORT, adding headers");
                        //Map<String, List<String>> mHeaders = (Map<String, List<String>>) args[0];
                      //  mHeaders.put("Authorization", Arrays.asList("Basic bXl1c2VyOm15cGFzczEyMw=="));
                    }
                });
            }
        });


    }


    public void reConnect() {
        if(mSocket.isActive()){
            mSocket.close();
        }
        mSocket.connect();
    }

    public void connect(){
        mSocket.connect();
    }
    private void on(String event, Emitter.Listener listener) {
        mSocket.on(event, listener);
    }

    private boolean containsInListener(String event) {
        for (int i = 0; i < listeners.size(); i++) {
            if (event.equals(listeners.get(i).first)) {
                return true;
            }
        }
        return false;
    }

    public void on(String event, Listener listener) {

        if (containsInListener(event)) {
            listeners.add(new Pair<>(event, listener));
            return;
        }
        Emitter.Listener elistener = new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                for (int i = 0; i < listeners.size(); i++) {
                    if (listeners.get(i).first.equals(event)) {
                        BaseMessageEvent messageEvent = new BaseMessageEvent();
                        messageEvent.event = event;
                        messageEvent.message = (String) args[0];
                        //toDo
                        listeners.get(i).second.onMessage(messageEvent);
                    }
                }
                if (eventLogs.contains(event)) {
                    EventBusUtils.saveToDatabase(TAG, (String) args[0], LogEvent.LOG_EVENT, event);
                }
            }
        };
        listeners.add(new Pair<>(event, listener));
        mSocket.on(event, elistener);
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

    public void destroy() {
        mSocket.disconnect();
        instance = null;
    }

}


