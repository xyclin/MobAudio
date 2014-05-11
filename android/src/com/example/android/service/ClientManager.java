package com.example.android.service;

import io.socket.SocketIO;

public class ClientManager implements ConnectionHandler {
    private boolean isConnected;

    public ClientManager() {
        this.isConnected = false;
    }

    public SocketIO connect() {
        return null;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
