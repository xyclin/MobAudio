package com.example.android.service;

import android.content.Context;
import android.widget.Toast;
import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;
import org.json.JSONObject;

import java.net.MalformedURLException;

public class HostManager implements ConnectionHandler {
    private boolean isConnected;
    private Context context;
    public HostManager(Context context) {
        this.isConnected = false;
        this.context = context;
    }

    public void connect() {
        SocketIO socket;
        try {
            socket = new SocketIO("192.241.208.189:54321");
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
                if (s == "heartbeat") {
                    handleHeartbeat(objects);
                }
            }

            @Override
            public void onError(SocketIOException e) {

            }
        });
    }

    public void handleHeartbeat(Object objects) {
        Toast.makeText(context, "Sup",
                Toast.LENGTH_SHORT).show();
    }

    public boolean isConnected() {
        return isConnected;
    }
}
