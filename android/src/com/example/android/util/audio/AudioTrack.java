package com.example.android.util.audio;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.SeekBar;
import android.widget.Toast;
import com.example.android.util.storage.DataStoreManager;

import java.io.IOException;

public class AudioTrack implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private MediaPlayer mediaPlayer;
    private boolean isPrepared;
    private Context context;
    private SeekBar seekbar;
    private Handler handler;

    public AudioTrack(Context context) {
        this.context = context;
        this.mediaPlayer= new MediaPlayer();
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.isPrepared = false;
    }

    public void prepareSong(String key, SeekBar seekBar) {
        if (!mediaPlayer.isPlaying()) {
            try{
                mediaPlayer.setDataSource(context, DataStoreManager.getURI(key));
                mediaPlayer.prepareAsync();
                isPrepared = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            int startTime = mediaPlayer.getCurrentPosition();
            seekbar.setProgress(startTime);
            handler.postDelayed(this, 100);
        }
    };

    public void play() {
        if (isPrepared) {
            this.mediaPlayer.start();
            this.handler.postDelayed(UpdateSongTime, 100);
        } else {
            // Let user know it is still being prepared
            Toast.makeText(context, "Still being prepared, please wait",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    /** Called when MediaPlayer is ready */
    public void onPrepared(MediaPlayer player) {
        isPrepared = true; // player.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // ... react appropriately ...
        // The MediaPlayer has moved to the Error state, must be reset
        Toast.makeText(context, "Moved to the wrong state",
                Toast.LENGTH_SHORT).show();
        return true;
    }
}