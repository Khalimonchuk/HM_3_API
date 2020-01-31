package com.lits.api.test;

import com.lits.api.httpClient;
import okhttp3.Headers;
import okhttp3.Response;

import java.util.HashMap;
import java.util.Map;

import static com.lits.api.httpClient.convert;

public class Login {

    public String getToken ()
    {


        String url = "https://europe-west2-search-app-263e2.cloudfunctions.net/webapp/api/auth/login";
        HashMap<String, String> body = new HashMap<>();
        body.put("email", "i.khalimonchuk@gmail.com");
        body.put("password", "1kh23456");

        httpClient client = new httpClient();
        Response newone = client.POST(url, Headers.of(), body);

        final Map<String, Map<String, String>> loginResponceData = convert (newone, Map.class);
        String token = loginResponceData.get("r").get("access_token");

        return token;


    }

    public static void main(String[] args) {
        Login l1 = new Login();
        System.out.println(l1.getToken());

    }

}
