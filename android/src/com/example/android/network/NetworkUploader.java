package com.example.android.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import ch.boye.httpclientandroidlib.HttpVersion;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntityBuilder;
import ch.boye.httpclientandroidlib.entity.mime.content.ContentBody;
import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;
import ch.boye.httpclientandroidlib.params.CoreProtocolPNames;
import ch.boye.httpclientandroidlib.util.EntityUtils;
import com.example.android.MyActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class NetworkUploader {
    private static final int FILE_SELECT_CODE = 0;
    private static NetworkUploader instance = null;
    private NetworkUploader() {}

    public static NetworkUploader getInstance() {
        if (instance == null) {
            instance = new NetworkUploader();
        }
        return instance;
    }

    public void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            MyActivity.instance.startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public String uploadFile(int requestCode, int resultCode, Intent data, Context main) {
        String resultString = null;
        if (resultCode == Activity.RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
            Log.d("LOG", "File Uri: " + uri.toString());
            // Get the path
            String path = getPath(main, uri);
            Log.d("LOG", "File Path: " + path);
            // Get the file instance
            // File file = new File(path);
            // Initiate the upload


            try {
                ch.boye.httpclientandroidlib.client.HttpClient httpclient = new ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient();
                httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

                HttpPost httppost = new HttpPost("http://192.241.208.189:54323/upload");
                File file = new File(path);

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();

                ContentBody cbFile = new FileBody(file, "image/jpeg");
                builder.addPart("file", cbFile);


                httppost.setEntity(builder.build());
                System.out.println("executing request " + httppost.getRequestLine());
                ch.boye.httpclientandroidlib.HttpResponse response = httpclient.execute(httppost);
                ch.boye.httpclientandroidlib.HttpEntity resEntity = response.getEntity();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    resEntity.writeTo(baos);
                    resultString = baos.toString("UTF-8");
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
        return resultString;
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
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

}
