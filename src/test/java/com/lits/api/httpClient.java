package com.lits.api;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;

--comment 
public class httpClient {

    public static <T> T convert (Response response, Class<T> c) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(response.body().string(), c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private OkHttpClient okHttpClient = new OkHttpClient();
    private ObjectMapper mapper ;


    public httpClient(){
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }




    public Response GET(String url) {
        return GET(url, Headers.of());
    }




    public Response GET (String url, Headers headers) {
        final Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .build();

        try {
            return okHttpClient.newCall(request).execute();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    public Response GET (String url, Headers headers, String at) {
        final Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .addHeader("Authorization", at)
                .build();

        try {
            return okHttpClient.newCall(request).execute();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }



    public Response POST (String url, Headers headers, Object o){

        String bodyString = "";

        try {
            bodyString = mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // String bodyString = mapper.writeValueAsString(o);

        String head = "\"Content-type\", \"application/json\"";



        RequestBody body = RequestBody.create(bodyString, MediaType.parse("application/json"));

        final Request request = new Request.Builder()
                .method("POST", body)
                .url(url)
                .headers(headers)
                .build();

        try {
            return okHttpClient.newCall(request).execute();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    public Response POST (String url, Headers headers, Object o, String at){

        String bodyString = "";

        try {
            bodyString = mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //String bodyString = mapper.writeValueAsString(o);


        RequestBody body = RequestBody.create(bodyString, MediaType.parse("application/json"));

        final Request request = new Request.Builder()
                .method("POST", body)
                .url(url)
                .headers(headers)
                .addHeader("Authorization", at)
                .build();

        try {
            return okHttpClient.newCall(request).execute();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}
