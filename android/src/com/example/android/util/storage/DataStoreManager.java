package com.example.android.util.storage;

import android.content.Context;
import android.net.Uri;

import java.io.*;
import java.util.HashMap;


public class DataStoreManager {
    private static HashMap<String, Uri> map;
    private Context context;

    public DataStoreManager(Context context) {
        this.context = context;
        map = new HashMap<String, Uri>();
    }

    public static Uri getURI(String key) {
        return map.get(key);
    }


    public void save(File song, String key) {
        FileOutputStream stream;
        try {
            stream = context.openFileOutput(key, Context.MODE_PRIVATE);
            stream.write(readBytes(song));
            map.put(key, Uri.fromFile(song));
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] readBytes(File file) throws IOException {
        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ( (read = ios.read(buffer)) != -1 ) {
                ous.write(buffer, 0, read);
            }
        } finally {
            try {
                if ( ous != null )
                    ous.close();
            } catch ( IOException e) {
            }

            try {
                if ( ios != null )
                    ios.close();
            } catch ( IOException e) {
            }
        }
        return ous.toByteArray();
    }
}
