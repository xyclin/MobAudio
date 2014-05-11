package com.example.android;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.dolby.dap.DolbyAudioProcessing;
import com.dolby.dap.OnDolbyAudioProcessingEventListener;

import com.example.android.network.NetworkUploader;
import com.example.android.util.audio.AudioTrack;

import com.example.android.network.NetworkAPI;


public class MyActivity extends Activity implements MediaPlayer.OnCompletionListener,
        OnDolbyAudioProcessingEventListener, SongHandler {
    public static Activity instance;

    private static final int FILE_SELECT_CODE = 0;
    private String TAG = "My Activity";
    private NetworkAPI mNetworkAPI;

    Button btnPlay;
    MediaPlayer mPlayer;
    boolean isRunning;
    private Uri mobMusicUrl;
    private String mobMusicString;
    private DolbyAudioProcessing mDolbyAudioProcessing;

    private AudioTrack currenTrack;

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onDolbyAudioProcessingClientConnected() {

    }

    @Override
    public void onDolbyAudioProcessingClientDisconnected() {

    }

    @Override
    public void onDolbyAudioProcessingEnabled(boolean b) {

    }

    @Override
    public void onDolbyAudioProcessingProfileSelected(DolbyAudioProcessing.PROFILE profile) {

    }

    @Override
    public String getSongPath() {
        return mobMusicString;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mNetworkAPI = NetworkAPI.getInstance();

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        currenTrack = new AudioTrack(this);

        instance = this;
        btnPlay = new Button(this);
        btnPlay.setText("magic?");
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkUploader.getInstance().showFileChooser();
            }
        });
        setContentView(btnPlay);

        mDolbyAudioProcessing =
                DolbyAudioProcessing.getDolbyAudioProcessing(this, DolbyAudioProcessing.PROFILE.VOICE, this);

        if (mDolbyAudioProcessing == null) {
            Toast.makeText(this,
                    "Dolby Audio Processing not available on this device.",
                    Toast.LENGTH_SHORT).show();
            //finish();
            //return;
        } else {
            Toast.makeText(this,
                    "Hello there :)",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void playSong() {
        currenTrack.prepareSong();
        Log.i(TAG, "Prepared song");
        currenTrack.play();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                // Catches activityResult from NetworkAPI.showFileChooser and returns RESULT URL STRING.
                NetworkUploader.getInstance().uploadFile(requestCode, resultCode, data, this);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
