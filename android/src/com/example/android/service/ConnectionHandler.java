package com.example.android.service;

import io.socket.SocketIO;

import java.net.MalformedURLException;

public interface ConnectionHandler {
    public SocketIO connect() throws MalformedURLException;

    public boolean isConnected();
    // TODO: Methods for interacting with web server
}
