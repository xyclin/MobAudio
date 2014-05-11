package com.example.android;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.dolby.dap.DolbyAudioProcessing;
import com.dolby.dap.OnDolbyAudioProcessingEventListener;

import com.example.android.network.NetworkUploader;
import com.example.android.service.ClientManager;
import com.example.android.service.HostManager;
import com.example.android.ui.DiscoverMobFragment;
import com.example.android.util.audio.AudioTrack;

import com.example.android.network.NetworkAPI;


public class MyActivity extends Activity implements MediaPlayer.OnCompletionListener,
        OnDolbyAudioProcessingEventListener{
    public static Activity instance;

    private static final int FILE_SELECT_CODE = 0;
    private String TAG = "My Activity";
    private NetworkAPI mNetworkAPI;

    private DolbyAudioProcessing mDolbyAudioProcessing;

    private AudioTrack currenTrack;
    private ClientManager mClientManager;

    @Override
    public void onCompletion(MediaPlayer mp) {}
    @Override
    public void onDolbyAudioProcessingClientConnected() {}
    @Override
    public void onDolbyAudioProcessingClientDisconnected() {}
    @Override
    public void onDolbyAudioProcessingEnabled(boolean b) {}
    @Override
    public void onDolbyAudioProcessingProfileSelected(DolbyAudioProcessing.PROFILE profile) {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNetworkAPI = NetworkAPI.getInstance();
        mClientManager = ClientManager.getInstance();


        //setContentView(R.layout.main);
        currenTrack = new AudioTrack();

        instance = this;
        /*btnPlay = new Button(this);
        btnPlay.setText("magic?");
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkUploader.getInstance().showFileChooser();
            }
        });*/
        setContentView(R.id.fragment_container);

        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new DiscoverMobFragment())
                .addToBackStack(null)
                .commit();

        HostManager.getInstance().pleasePostDelayed(findViewById(R.id.fragment_container).getHandler(), new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyActivity.this, "play!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupDolby() {
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
        currenTrack.prepareSong("PATH");
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
