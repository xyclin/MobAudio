package com.example.android.service;

import java.net.MalformedURLException;

import io.socket.SocketIO;

public interface ConnectionHandler {
    public SocketIO connect() throws MalformedURLException;
    public boolean isConnected();
    // TODO: Methods for interacting with web server
}
