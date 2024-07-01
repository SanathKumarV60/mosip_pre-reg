package net.mosip.register.demographic;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mosip.envManager;
import net.mosip.models.register.demographic.makeApplication.*;

import okhttp3.*;

public class MakeApplication {
    public static void main(String[] args) throws IOException {
        makeApplication();
    }

    public static ResponseDetailsApp makeApplication_call(String auth, String name, String dob, String gender, String residenceStat, String addressLine, String region, String province, String city, String zone, String pincode, String phone, String email) throws IOException, ApplicationException {
        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        OffsetDateTime gmtTime = now.withOffsetSameInstant(ZoneOffset.UTC);
        String formattedTime = formatter.format(gmtTime);

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create("{\r\n    \"id\": \"mosip.pre-registration.demographic.create\",\r\n    \"request\": {\r\n        \"langCode\": \"eng\",\r\n        \"demographicDetails\": {\r\n            \"identity\": {\r\n                \"IDSchemaVersion\": 0.1,\r\n                \"fullName\": [\r\n                    {\r\n                        \"language\": \"eng\",\r\n                        \"value\": \"" + name + "\"\r\n                    }\r\n                ],\r\n                \"dateOfBirth\": \"" + dob + "\",\r\n                \"gender\": [\r\n                    {\r\n                        \"language\": \"eng\",\r\n                        \"value\": \"" + gender + "\"\r\n                    }\r\n                ],\r\n                \"residenceStatus\": [\r\n                    {\r\n                        \"language\": \"eng\",\r\n                        \"value\": \"" + residenceStat + "\"\r\n                    }\r\n                ],\r\n                \"addressLine1\": [\r\n                    {\r\n                        \"language\": \"eng\",\r\n                        \"value\": \"" + addressLine + "\"\r\n                    }\r\n                ],\r\n                \"region\": [\r\n                    {\r\n                        \"language\": \"eng\",\r\n                        \"value\": \"" + region + "\"\r\n                    }\r\n                ],\r\n                \"province\": [\r\n                    {\r\n                        \"language\": \"eng\",\r\n                        \"value\": \"" + province + "\"\r\n                    }\r\n                ],\r\n                \"city\": [\r\n                    {\r\n                        \"language\": \"eng\",\r\n                        \"value\": \"" + city + "\"\r\n                    }\r\n                ],\r\n                \"zone\": [\r\n                    {\r\n                        \"language\": \"eng\",\r\n                        \"value\": \"" + zone + "\"\r\n                    }\r\n                ],\r\n                \"postalCode\": \"" + pincode + "\",\r\n                \"phone\": \"" + phone + "\",\r\n                \"email\": \"" + email + "\"\r\n            }\r\n        }\r\n    },\r\n    \"version\": \"1.0\",\r\n    \"requesttime\": \"" + formattedTime +"\"\r\n}", mediaType);
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/applications")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Cookie", "Authorization=" + auth)
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataApp result = objectMapper.readValue(responseBody, ResponseDataApp.class);

        if (result.errors == null) {
            return result.response;
        } else {
            throw new ApplicationException(result);
        }
    }

    public static void makeApplication() throws IOException {
        try {
            ResponseDetailsApp resp = makeApplication_call(envManager.getEnv("auth"), envManager.getEnv("name"), envManager.getEnv("dob"), envManager.getEnv("gender"), envManager.getEnv("residenceStat"), envManager.getEnv("addressLine"), envManager.getEnv("region"), envManager.getEnv("province"), envManager.getEnv("city"), envManager.getEnv("zone"), envManager.getEnv("pincode"), envManager.getEnv("phoneNumber"), envManager.getEnv("email"));

            System.out.println("Booking Started with Application ID: " + resp.preRegistrationId + "!");
            envManager.updateEnv("applicationId", resp.preRegistrationId);
            System.out.println("Creation Time: " + resp.createdDateTime);
            System.out.println("------------------------------");
        } catch (ApplicationException ex) {
            System.err.println(ex.getMessage());
            System.out.println("------------------------------");
        }
    }
}
