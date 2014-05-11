package com.example.android.util.audio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class AudioTrack implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private String TAG = "AudioTrack";

    private MediaPlayer mediaPlayer;
    private boolean isPrepared;

    public AudioTrack() {
        this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.isPrepared = false;
    }

    public void prepareSong(String path) {
        if (!mediaPlayer.isPlaying()) {
            try {
                Log.d(TAG, path);
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                isPrepared = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void play() {
        if (isPrepared) {
            this.mediaPlayer.start();
        } else {
            // Let user know it is still being prepared
            Log.d(TAG, "Still being prepared, please wait");
        }
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void seekTo(int sec) {
        if (mediaPlayer.isPlaying() || isPrepared) {
            mediaPlayer.seekTo(sec);
        }
    }

    /**
     * Called when MediaPlayer is ready
     */
    public void onPrepared(MediaPlayer player) {
        isPrepared = true; // player.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // ... react appropriately ...
        // The MediaPlayer has moved to the Error state, must be reset
        Log.d(TAG, "Error, wrong state");
        return true;
    }
}