package com.example.android.service;

import android.util.Log;
import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.Socket;

public class HostManager{
    private static final String TAG = "Sockets";
    private SocketIO socket;
    public static final String API_URL = "http://linux024.student.cs.uwaterloo.ca:54321/";

    public static final String HEARTBEAT_EVENT = "heartbeat";
    public static final String LIST_EVENT = "list";
    public static final String SUBSCRIBED_EVENT = "subscribe";
    public static final String UNSUBSCRIBE_EVENT = "unsubscribe";
    public static final String CREATE_EVENT = "create";
    public static final String DESTROY_EVENT = "destroy";
    public static final String COUNT_EVENT = "count";
    public static final String PLAY_EVENT = "play";

    private static HostManager instance = null;

    private HostManager() {
        connect();
    }

    public static HostManager getInstance(){
        if (instance == null){
            synchronized (HostManager.class) {
                if (instance == null) {
                    instance = new HostManager();
                }
            }
        }
        return instance;
    }

    private void connect(){
        if (!isConnected()) {
            Log.i(TAG, "Before connecting");
            try {
                socket = new SocketIO(API_URL);
            } catch (MalformedURLException e) {
                socket = null;
                return;
            }
            socket.connect(new Callback());
        }
    }

    public boolean isConnected() {
        return socket != null;
    }

    private class Callback implements IOCallback {

        @Override
        public void onDisconnect() {
            socket = null;
        }

        @Override
        public void onConnect() {
            Log.i(TAG, "Connected to socket!");
        }

            @Override
            public void on(String s, IOAcknowledge ioAcknowledge, Object... objects) {
                Log.i(TAG, s);
                if (s == "heartbeat") {
                    handleHeartbeat();
                }


            }

            @Override
            public void onError(SocketIOException e) {
                e.printStackTrace();
            }
        }

    public void handleHeartbeat() {
        Log.i(TAG, "Handling Heartbeat");
    }
}
