package com.example.android.service;

import android.os.Handler;
import android.util.Log;
import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

public class HostManager {
    private static final String TAG = "Sockets";
    private SocketIO socket;
    public static final String API_URL = "http://192.241.208.189:54321/";

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

    public static HostManager getInstance() {
        if (instance == null) {
            synchronized (HostManager.class) {
                if (instance == null) {
                    instance = new HostManager();
                }
            }
        }
        return instance;
    }

    public boolean subscribeMob(int id) {
        JSONObject json = new JSONObject();
        try {
            json.put("modId", id);
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("subscribe", json);
        return true;
    }

    private void connect() {
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
        public void onMessage(String s, IOAcknowledge ioAcknowledge) {

        }

        @Override
        public void onMessage(JSONObject jsonObject, IOAcknowledge ioAcknowledge) {

        }

        @Override
        public void on(String s, IOAcknowledge ioAcknowledge, Object... objects) {
            Log.i(TAG, s);
            double timestamp = 0;
            try {
                timestamp = ((JSONObject)objects[0]).getDouble("timestamp");
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
            if ("heartbeat".equals(s)) {
                handleHeartbeat(timestamp);
            } else if ("play".equals(s)) {
                handlePlay(timestamp);
            }


        }

        @Override
        public void onError(SocketIOException e) {
            e.printStackTrace();
        }
    }

    double lambda = .3, weight = 0, sum = 0;
    public void handleHeartbeat(double timestamp) {
        double delta = System.currentTimeMillis()-timestamp;
        sum = (1-lambda)*sum+lambda*delta;
        weight *= 1-lambda;
        weight += lambda;
        Log.i(TAG, "Handling Heartbeat");
    }

    Handler h;
    Runnable r;
    public void pleasePostDelayed(Handler h, Runnable r) {
        this.h=h;
        this.r=r;
    }

    public void handlePlay(double when) {
        double delta = 0;
        if (sum !=0)
            delta = weight/sum;
        if (h!=null)
            h.postDelayed(r, ((long)(System.currentTimeMillis()-when-delta)));
    }
}
