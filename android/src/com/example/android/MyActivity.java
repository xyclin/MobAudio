package com.example.android;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import com.example.android.service.HostManager;
import android.view.View;
import android.widget.Button;


public class MyActivity extends Activity {
    Button btnPlay;
    MediaPlayer mPlayer;
    boolean isRunning;
    int arity = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HostManager host = HostManager.getInstance();

        btnPlay = new Button(this);
        btnPlay.setText("Preppin'");
        try {
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource("http://charleslin.ca/res/my_nigga.mp3");
            mPlayer.prepareAsync();
            btnPlay.setText("Ready!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    if(arity++ % 2 == 0) {
                        try {
                            Thread.currentThread().sleep(100);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    };
                    isRunning = true;
                    mPlayer.start();
                } else {
                    isRunning = false;
                    mPlayer.pause();
                }
            }
        });
        setContentView(btnPlay);
    }
}
