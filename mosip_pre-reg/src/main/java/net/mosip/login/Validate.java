package net.mosip.login;

import java.io.IOException;
import java.io.Console;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mosip.envManager;
import okhttp3.*;

public class Validate {
    public static void main(String[] args) throws IOException{
        String otp = null;
        Console console = System.console();
        if (console != null) {
            otp = console.readLine("Enter OTP: ");
            System.out.println("------------------------------");
        } else{
            System.err.println("ERROR: Could not take in OTP!");
        }

        String response = validateOtp(otp);
        if (response.substring(0, 5).equals("ERROR")) {
            System.err.println(response);
        } else {
            envManager.updateEnv("auth", response);
            System.out.println("LOGGED IN SUCCESSFULLY!");
        }
        System.out.println("------------------------------");
    }

    public static String validateOtp(String otp) throws IOException{
        new envManager();

        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        OffsetDateTime gmtTime = now.withOffsetSameInstant(ZoneOffset.UTC);
        String formattedTime = formatter.format(gmtTime);
        envManager.updateEnv("currentTime", formattedTime);

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create("{\r\n    \"id\": \"mosip.pre-registration.login.useridotp\",\r\n    \"request\": {\r\n        \"otp\": \"" + otp + "\",\r\n        \"userId\": \"" + envManager.getEnv("userId") +"\"\r\n    },\r\n    \"version\": \"1.0\",\r\n    \"requesttime\": \"" + formattedTime + "\"\r\n}", mediaType);
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/login/validateOtp")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        ObjectMapper objectMapper = new ObjectMapper();
        ResponseData result = objectMapper.readValue(responseBody, ResponseData.class);

        if (result.errors == null) {        
            if(response.header("Set-Cookie") != null){
                String auth = ((response.header("Set-Cookie")).split(";")[0]).split("=")[1];
                return auth;
            }
            else{
                return "ERROR: NO HEADERS! LOGIN UNSUCCESSFUL!";
            }
        } else {
            return "ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message;
        }
    }
}
