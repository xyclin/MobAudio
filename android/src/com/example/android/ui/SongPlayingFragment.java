package com.example.android.ui;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dolby.dap.DolbyAudioProcessing;
import com.dolby.dap.OnDolbyAudioProcessingEventListener;
import com.example.android.MyActivity;
import com.example.android.R;

public class SongPlayingFragment extends Fragment /*implements MediaPlayer.OnCompletionListener,
        OnDolbyAudioProcessingEventListener*/ {
    private MediaPlayer mPlayer;
    private DolbyAudioProcessing mDolbyAudioProcessing;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //mPlayer = ClientManager.getInstance().getSong();
        return inflater.inflate(R.layout.song_play_view, container, false);
    }

    public  void play(String address) {
        mPlayer.start();
    }
}
