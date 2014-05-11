package com.example.android.service;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
<<<<<<< HEAD
=======

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
>>>>>>> cleaned up main, moved file upload to classes
import com.example.android.model.Count;
import com.example.android.model.Mob;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkAPI {
    private String TAG = "NetworkAPI";
    private org.apache.http.client.HttpClient client;

    private static final String API_BASE = "192.241.208.189:54321";
    private static final String LIST_ENDPOINT = "/list";
    private static final String SUBSCRIBE_ENDPOINT = "/subscribe";
    private static final String UNSUBSCRIBE_ENDPOINT = "/unsubscribe";
    private static final String CREATE_ENDPOINT = "/create";
    private static final String DELETE_ENDPOINT = "/delete";
    private static final String COUNT_ENDPOINT = "/count";

    private static final String LIST_ROUTE = API_BASE + LIST_ENDPOINT;
    private static final String SUBSCRIBE_ROUTE = API_BASE + SUBSCRIBE_ENDPOINT;
    private static final String UNSUBSCRIBE_ROUTE = API_BASE + UNSUBSCRIBE_ENDPOINT;
    private static final String CREATE_ROUTE = API_BASE + CREATE_ENDPOINT;
    private static final String DELETE_ROUTE = API_BASE + DELETE_ENDPOINT;
    private static final String COUNT_ROUTE = API_BASE + COUNT_ENDPOINT;

    private RestTemplate restTemplate;
    private Map<String, Object> headers;

    private static NetworkAPI instance;

    private NetworkAPI() {
        this.client = new DefaultHttpClient();
        restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter JSONConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        JSONConverter.setObjectMapper(mapper);
        restTemplate.getMessageConverters().add(JSONConverter);
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        headers = new HashMap<String, Object>();
    }

    public static NetworkAPI getInstance() {
        if (instance == null) {
            synchronized (NetworkAPI.class) {
                if (instance == null) {
                    instance = new NetworkAPI();
                }
            }
        }
        return instance;
    }

    public List<Mob> getMobs(double radius, double latitude, double longitude) {
        Map form = new HashMap();
        form.put("radius", radius);
        form.put("lat", latitude);
        form.put("lon", longitude);
        HttpEntity requestEntity = new HttpEntity<Map>(form, null);
        ResponseEntity<Mob[]> responseEntity = restTemplate.exchange(LIST_ROUTE, HttpMethod.POST,
                requestEntity, Mob[].class, headers);
        Mob[] body = responseEntity.getBody();
        return body == null ? null : Arrays.asList(body);
    }

    public boolean subscribeMob(int id) {
        /*Map form = new HashMap();
        form.put("mobId", id);
        HttpEntity requestEntity = new HttpEntity<Map>(form, null);
        ResponseEntity<Mob> responseEntity = restTemplate.exchange(SUBSCRIBE_ROUTE, HttpMethod.POST,
                requestEntity, Mob.class, headers);
        Mob body = responseEntity.getBody();
        return body != null && body.getMobId() != -1;*/
        return HostManager.getInstance().subscribeMob(id);
    }

    public boolean unsubscribeMob(int id) {
        Map form = new HashMap();
        form.put("mobId", id);
        HttpEntity requestEntity = new HttpEntity<Map>(form, null);
        ResponseEntity<Mob> responseEntity = restTemplate.exchange(UNSUBSCRIBE_ROUTE, HttpMethod.POST,
                requestEntity, Mob.class, headers);
        Mob body = responseEntity.getBody();
        return body != null && body.getMobId() != -1;
    }

    public Mob createMob(Mob mob) {
        Map form = new HashMap();
        form.put("url", mob.getUrl());
        form.put("name", mob.getName());
        form.put("time", mob.getTime().toString());
        form.put("lat", mob.getLatitude());
        form.put("lon", mob.getLongitude());
        HttpEntity requestEntity = new HttpEntity<Map>(form, null);
        ResponseEntity<Mob> responseEntity = restTemplate.exchange(CREATE_ROUTE, HttpMethod.POST,
                requestEntity, Mob.class, headers);
        Mob body = responseEntity.getBody();
        return body == null ? null : body;
    }

    public boolean deleteMob(int id) {
        Map form = new HashMap();
        form.put("mobId", id);
        HttpEntity requestEntity = new HttpEntity<Map>(form, null);
        ResponseEntity<Mob> responseEntity = restTemplate.exchange(DELETE_ROUTE, HttpMethod.POST,
                requestEntity, Mob.class, headers);
        Mob body = responseEntity.getBody();
        return body != null && body.getMobId() != -1;
    }

    public int getCount(int id) {
        Map form = new HashMap();
        form.put("mobId", id);
        HttpEntity requestEntity = new HttpEntity<Map>(form, null);
        ResponseEntity<Count> responseEntity = restTemplate.exchange(DELETE_ROUTE, HttpMethod.POST,
                requestEntity, Count.class, headers);
        Count body = responseEntity.getBody();
        return body == null ? -1 : body.getCount();
    }

    public void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public String uploadFile(int requestCode, int resultCode, Intent data, Context main) {
        String resultString;
        if (resultCode == RESULT_OK) {
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
