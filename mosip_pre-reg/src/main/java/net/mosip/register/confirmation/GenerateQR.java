package net.mosip.register.confirmation;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mosip.envManager;
import net.mosip.models.register.confirmation.generateQR.*;

import okhttp3.*;

public class GenerateQR {
    public static void main(String[] args) throws IOException {
        generate(envManager.getEnv("applicationId"));
    }

    public static ResponseDetailsQR generate_call(String auth, String applicationId) throws IOException, QRException {
        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        OffsetDateTime gmtTime = now.withOffsetSameInstant(ZoneOffset.UTC);
        String formattedTime = formatter.format(gmtTime);
        
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create("{\r\n    \"id\": \"mosip.pre-registration.qrcode.generate\",\r\n    \"request\": \"" + applicationId + "\",\r\n    \"version\": \"1.0\",\r\n    \"requesttime\": \"" + formattedTime + "\"\r\n}", mediaType);
        Request request = new Request.Builder()
        .url("https://uat2.mosip.net//preregistration/v1/qrCode/generate")
        .method("POST", body)
        .addHeader("Content-Type", "application/json")
        .addHeader("Cookie", "Authorization=" + auth)
        .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create JSON parse for body
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataQR result = objectMapper.readValue(responseBody, ResponseDataQR.class);

        if (result.errors == null) {
            return result.response;
        } else {
            throw new QRException(result);
        }
    }

    public static void generate(String applicationId) throws IOException {
        try {
            ResponseDetailsQR resp = generate_call(envManager.getEnv("auth"), applicationId);

            System.out.println("QR Code String: " + resp.qrcode);
        } catch (QRException ex) {
            System.err.println(ex.getMessage());
            System.out.println("------------------------------");
        }
    }
}