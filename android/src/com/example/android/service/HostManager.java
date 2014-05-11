package com.example.android.service;

public class HostManager implements ConnectionHandler {
    private boolean isConnected;
    public HostManager() {
        this.isConnected = false;
    }

    public void connect(String address) {

    }

    public boolean isConnected() {
        return isConnected;
    }
}
