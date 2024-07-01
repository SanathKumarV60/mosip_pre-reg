package net.mosip.applications;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import net.mosip.envManager;
import net.mosip.models.applications.*;

public class DisplayCall {
    public static void main(String[] args) throws IOException, ApplicationException {
        display_applications(envManager.getEnv("auth"));
        //booked_details(envManager.getEnv("applicationId"));
    }

    public static ResponseDetails display_applications(String auth) throws IOException, ApplicationException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/applications")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + auth)
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        // Create JSON parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseData result = objectMapper.readValue(responseBody, ResponseData.class);

        if (result.errors == null) {
            return result.response;
        } else {
            throw new ApplicationException(result);
        }
    }
}