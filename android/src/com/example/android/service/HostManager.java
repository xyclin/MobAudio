package com.example.android.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;
import org.json.JSONObject;

import java.net.MalformedURLException;

public class HostManager implements ConnectionHandler {
    private String TAG = "Sockets";
    private boolean isConnected;
    private Context context;
    public HostManager(Context context) {
        this.isConnected = false;
        this.context = context;
    }

    public void connect() {
        SocketIO socket;
        Log.i(TAG, "Before connecting");
        try {
            socket = new SocketIO("http://linux024.student.cs.uwaterloo.ca:54321/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }

        socket.connect(new IOCallback() {

            @Override
            public void onDisconnect() {

            }

            @Override
            public void onConnect() {
                Log.i(TAG, "Connected to socket!");
                isConnected = true;
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
                if (s == "heartbeat") {
                    handleHeartbeat();
                }


            }

            @Override
            public void onError(SocketIOException e) {
                e.printStackTrace();
            }
        });
    }

    public void handleHeartbeat() {
        Log.i(TAG, "Handling Heartbeat");
    }

    public boolean isConnected() {
        return isConnected;
    }
}
