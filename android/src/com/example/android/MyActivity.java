package com.example.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.widget.Toast;
import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.HttpVersion;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.entity.mime.content.ContentBody;
import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import ch.boye.httpclientandroidlib.params.CoreProtocolPNames;
import ch.boye.httpclientandroidlib.util.EntityUtils;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntityBuilder;
import com.dolby.dap.DolbyAudioProcessing;
import com.dolby.dap.OnDolbyAudioProcessingEventListener;
import com.example.android.service.HostManager;
import android.view.View;
import android.widget.Button;
import com.example.android.util.audio.AudioTrack;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URISyntaxException;


public class MyActivity extends Activity implements MediaPlayer.OnCompletionListener,
        OnDolbyAudioProcessingEventListener, SongHandler {
    public static Activity instance;

    private static final int FILE_SELECT_CODE = 0;
    Button btnPlay;
    MediaPlayer mPlayer;
    boolean isRunning;
    private Uri mobMusicUrl;
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
    public Uri getSongUri() {
        return mobMusicUrl;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        currenTrack = new AudioTrack(this);

        instance = this;
        btnPlay = new Button(this);
        btnPlay.setText("magic?");
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
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
        /* end of OnCreate
        HostManager host = new HostManager(getApplicationContext());
        host.connect();

        /* end of OnCreate
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
        */

    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void playSong(Uri uri) {
        currenTrack.prepareSong();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("LOG", "File Uri: " + uri.toString());
                    // Get the path
                    String path = getPath(this, uri);
                    Log.d("LOG", "File Path: " + path);

                    // TODO
                    playSong();
                    return;
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload


                    try {
                        HttpClient httpclient = new DefaultHttpClient();
                        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

                        HttpPost httppost = new HttpPost("http://192.241.208.189:54323/upload");
                        File file = new File(path);

                        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

                        ContentBody cbFile = new FileBody(file, "image/jpeg");
                        builder.addPart("file", cbFile);


                        httppost.setEntity(builder.build());
                        System.out.println("executing request " + httppost.getRequestLine());
                        HttpResponse response = httpclient.execute(httppost);
                        HttpEntity resEntity = response.getEntity();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        try {
                            resEntity.writeTo(baos);
                            mobMusicUrl = baos.toString("UTF-8");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        System.out.println(response.getStatusLine());
                        if (resEntity != null) {
                            System.out.println(EntityUtils.toString(resEntity));
                        }
                        if (resEntity != null) {
                            resEntity.consumeContent();
                        }

                        httpclient.getConnectionManager().shutdown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow("_data");
            if (cursor.moveToFirst()) {
                return cursor.getString(column_index);
            }

        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
}
