package com.example.android.service;

public class ClientManager implements ConnectionHandler {
    private boolean isConnected;

    public ClientManager() {
        this.isConnected = false;
    }

    public void connect() {

    }

    public boolean isConnected() {
        return isConnected;
    }
}
