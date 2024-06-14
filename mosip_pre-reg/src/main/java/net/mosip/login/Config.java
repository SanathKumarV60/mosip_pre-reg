package net.mosip.login;

import java.io.IOException;

import okhttp3.*;

public class Config {
    public static void main(String[] args) throws IOException{
        String response = config();
        System.out.println(response);
    }

    public static String config() throws IOException{
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/login/config")
            .method("GET", null)
            .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
