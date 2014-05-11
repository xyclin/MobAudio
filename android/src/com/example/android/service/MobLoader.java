package com.example.android.service;

import android.os.AsyncTask;

import android.util.Log;
import com.example.android.model.Mob;
import com.example.android.network.NetworkAPI;

/**
 * Created by gabriel on 5/11/14.
 */
public class MobLoader extends AsyncTask<Double,Void,Mob[]> {
    @Override
    protected Mob[] doInBackground(Double... args) {
        double radius = args[0];
        double lat = args[1];
        double lon = args[2];
        Log.d("MobLoader", "AsyncTask Mob loader");
        return NetworkAPI.getInstance().getMobs(radius, lat, lon);
    }
}
