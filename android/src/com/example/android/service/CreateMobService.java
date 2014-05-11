package com.example.android.service;

import android.app.IntentService;
import android.content.Intent;
import com.example.android.model.Mob;
import com.example.android.network.NetworkAPI;

/**
 * Created by gabriel on 5/11/14.
 */
public class CreateMobService extends IntentService {
    public static final String MOB_KEY = "mob_key";

    public CreateMobService() {
        super("CreateMobService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Mob mob = (Mob) intent.getSerializableExtra(MOB_KEY);
        Mob newMob = NetworkAPI.getInstance().createMob(mob);
        Intent outIntent = new Intent();
        outIntent.putExtra(MOB_KEY, newMob);
        sendBroadcast(outIntent);
    }
}
