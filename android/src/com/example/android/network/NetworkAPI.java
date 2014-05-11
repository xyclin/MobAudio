package com.example.android.network;

import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import com.example.android.model.Count;
import com.example.android.model.Mob;
import com.example.android.service.HostManager;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkAPI {
    private String TAG = "NetworkAPI";
    private HttpClient client;

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

    public Mob[] getMobs(double radius, double latitude, double longitude) {
        Map form = new HashMap();
        form.put("radius", radius);
        form.put("lat", latitude);
        form.put("lon", longitude);
        HttpEntity requestEntity = new HttpEntity<Map>(form, null);
        ResponseEntity<Mob[]> responseEntity = restTemplate.exchange(LIST_ROUTE, HttpMethod.POST,
                requestEntity, Mob[].class, headers);
        Mob[] body = responseEntity.getBody();
        return body;
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
}
