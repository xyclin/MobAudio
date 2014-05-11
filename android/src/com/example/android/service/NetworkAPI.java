package com.example.android.service;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.net.URI;

public class NetworkAPI {
    private String TAG = "NetworkAPI";
    private ConnectionHandler handler;
    private HttpClient client;

    public NetworkAPI(ConnectionHandler handler) {
        this.handler = handler;
        this.client = new DefaultHttpClient();
    }
    /* shut up i need this to compile
    public File get(String url) {
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

        HttpGet getRequest = new HttpGet(uri);
        HttpResponse response = execute(getRequest);
    }

    public String post(File audioTrack) {

    }

    public HttpResponse execute(HttpUriRequest request) {
        HttpResponse response = null;
        try{
            response = client.execute(request);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return response;
    }
    */
}
