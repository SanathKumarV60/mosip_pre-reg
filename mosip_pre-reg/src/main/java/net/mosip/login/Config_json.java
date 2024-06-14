package net.mosip.login;

import java.io.IOException;

import okhttp3.*;

public class Config_json {
    public static void main(String[] args) throws IOException{
        String response = config_json();
        System.out.println(response);
    }

    public static String config_json() throws IOException{
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net/pre-registration-ui/assets/config.json")
            .method("GET", null)
            .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
