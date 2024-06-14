package net.mosip.register.confirmation;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mosip.envManager;
import okhttp3.*;

public class GenerateQR {
    public static void main(String[] args) throws IOException {
        generate(envManager.getEnv("applicationId"));
    }

    public static void generate(String applicationId) throws IOException {
        new envManager();

        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        OffsetDateTime gmtTime = now.withOffsetSameInstant(ZoneOffset.UTC);
        String formattedTime = formatter.format(gmtTime);
        envManager.updateEnv("currentTime", formattedTime);
        
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create("{\r\n    \"id\": \"mosip.pre-registration.qrcode.generate\",\r\n    \"request\": \"" + applicationId + "\",\r\n    \"version\": \"1.0\",\r\n    \"requesttime\": \"" + formattedTime + "\"\r\n}", mediaType);
        Request request = new Request.Builder()
        .url("https://uat2.mosip.net//preregistration/v1/qrCode/generate")
        .method("POST", body)
        .addHeader("Content-Type", "application/json")
        .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
        .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create JSON parse for body
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataQR result = objectMapper.readValue(responseBody, ResponseDataQR.class);

        if (result.errors == null) {
            System.out.println("QR Code String: " + result.response.qrcode);
        } else {
            int l = result.errors.length;
            for(int i = 0; i < l; i++){
                System.err.println("ERROR: " + result.errors[i].errorCode + ": " + result.errors[i].message);
            }
            System.out.println("------------------------------");
        }
    }
}

class ResponseDataQR {
    public String id;
    public String version;
    public String responsetime;
    public String metadata;
    public ResponseDetailsQR response;
    public ErrorsQR[] errors;
}

class ResponseDetailsQR {
    public String qrcode;
}

class ErrorsQR {
    public String errorCode;
    public String message;
}
