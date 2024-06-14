package net.mosip.register.demographic;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mosip.envManager;
import okhttp3.*;

public class MakeApplication {
    public static void main(String[] args) throws IOException {
        makeApplication();
    }

    public static void makeApplication() throws IOException {
        new envManager();

        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        OffsetDateTime gmtTime = now.withOffsetSameInstant(ZoneOffset.UTC);
        String formattedTime = formatter.format(gmtTime);
        envManager.updateEnv("currentTime", formattedTime);

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create("{\r\n    \"id\": \"mosip.pre-registration.demographic.create\",\r\n    \"request\": {\r\n        \"langCode\": \"eng\",\r\n        \"demographicDetails\": {\r\n            \"identity\": {\r\n                \"IDSchemaVersion\": 0.1,\r\n                \"fullName\": [\r\n                    {\r\n                        \"language\": \"eng\",\r\n                        \"value\": \"" + envManager.getEnv("name") + "\"\r\n                    }\r\n                ],\r\n                \"dateOfBirth\": \"" + envManager.getEnv("dob") + "\",\r\n                \"gender\": [\r\n                    {\r\n                        \"language\": \"eng\",\r\n                        \"value\": \"" + envManager.getEnv("gender") + "\"\r\n                    }\r\n                ],\r\n                \"residenceStatus\": [\r\n                    {\r\n                        \"language\": \"eng\",\r\n                        \"value\": \"" + envManager.getEnv("residenceStat") + "\"\r\n                    }\r\n                ],\r\n                \"addressLine1\": [\r\n                    {\r\n                        \"language\": \"eng\",\r\n                        \"value\": \"" + envManager.getEnv("addressLine") + "\"\r\n                    }\r\n                ],\r\n                \"region\": [\r\n                    {\r\n                        \"language\": \"eng\",\r\n                        \"value\": \"" + envManager.getEnv("region") + "\"\r\n                    }\r\n                ],\r\n                \"province\": [\r\n                    {\r\n                        \"language\": \"eng\",\r\n                        \"value\": \"" + envManager.getEnv("province") + "\"\r\n                    }\r\n                ],\r\n                \"city\": [\r\n                    {\r\n                        \"language\": \"eng\",\r\n                        \"value\": \"" + envManager.getEnv("city") + "\"\r\n                    }\r\n                ],\r\n                \"zone\": [\r\n                    {\r\n                        \"language\": \"eng\",\r\n                        \"value\": \"" + envManager.getEnv("zone") + "\"\r\n                    }\r\n                ],\r\n                \"postalCode\": \"" + envManager.getEnv("pincode") + "\",\r\n                \"phone\": \"" + envManager.getEnv("phoneNumber") + "\",\r\n                \"email\": \"" + envManager.getEnv("email") + "\"\r\n            }\r\n        }\r\n    },\r\n    \"version\": \"1.0\",\r\n    \"requesttime\": \"" + formattedTime +"\"\r\n}", mediaType);
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/applications")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataApp result = objectMapper.readValue(responseBody, ResponseDataApp.class);

        if(result.errors == null){
            System.out.println("Booking Started with Application ID: " + result.response.preRegistrationId + "!");
            envManager.updateEnv("applicationId", result.response.preRegistrationId);
            System.out.println("Creation Time: " + result.response.createdDateTime);
            System.out.println("------------------------------");
        } else {
            int l = result.errors.length;
            for(int i = 0; i < l; i++){
                System.err.println("ERROR" + result.errors[i].errorCode + ": " + result.errors[i].message);
            }
            System.out.println("------------------------------");
        }
    }
}

class ResponseDataApp {
    public String id;
    public String version;
    public String responsetime;
    public ResponseDetailsApp response;
    public ErrorsApp[] errors;
}

class ResponseDetailsApp {
    public String preRegistrationId;
    public String createdDateTime;
    public String statusCode;
    public String langCode;
    public DemographicDetailsApp demographicDetails;
}

class DemographicDetailsApp {
    public IdentityApp identity;
}

class IdentityApp {
    public double IDSchemaVersion;
    public FullNameApp[] fullName;
    public String dateOfBirth;
    public GenderApp[] gender;
    public ResidenceApp[] residenceStatus;
    public AddressVal[] addressLine1;
    public RegionVal[] region;
    public ProvinceVal[] province;
    public CityVal[] city;
    public ZoneVal[] zone;
    public String postalCode;
    public String phone;
    public String email;
}

class FullNameApp {
    public String language;
    public String value;
}

class GenderApp {
    public String language;
    public String value;
}

class ResidenceApp {
    public String language;
    public String value;
}

class AddressVal {
    public String language;
    public String value;
}

class RegionVal {
    public String language;
    public String value;
}

class ProvinceVal {
    public String language;
    public String value;
}

class CityVal {
    public String language;
    public String value;
}

class ZoneVal {
    public String language;
    public String value;
}

class ErrorsApp {
    public String errorCode;
    public String message;
}