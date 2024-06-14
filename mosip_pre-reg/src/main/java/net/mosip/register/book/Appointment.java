package net.mosip.register.book;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mosip.envManager;
import okhttp3.*;

public class Appointment {
    public static void main(String[] args) throws IOException {
        getAppointment(envManager.getEnv("applicationId"));
    }

    public static void getAppointment(String applicationId) throws IOException {
        new envManager();

        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        OffsetDateTime gmtTime = now.withOffsetSameInstant(ZoneOffset.UTC);
        String formattedTime = formatter.format(gmtTime);
        envManager.updateEnv("currentTime", formattedTime);

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create("{\r\n    \"id\": \"mosip.pre-registration.booking.book\",\r\n    \"request\": {\r\n        \"bookingRequest\": [\r\n            {\r\n                \"preRegistrationId\": \"" + applicationId + "\",\r\n                \"registration_center_id\": \"" + envManager.getEnv("regCenterId") + "\",\r\n                \"appointment_date\": \"" + envManager.getEnv("dateReq") + "\",\r\n                \"time_slot_from\": \"" + envManager.getEnv("fromTime") + "\",\r\n                \"time_slot_to\": \"" + envManager.getEnv("toTime") + "\"\r\n            }\r\n        ]\r\n    },\r\n    \"version\": \"1.0\",\r\n    \"requesttime\": \"" + formattedTime + "\"\r\n}", mediaType);
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/appointment")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create JSON parse for body
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataAppoint result = objectMapper.readValue(responseBody, ResponseDataAppoint.class);

        if (result.errors == null) {
            System.out.println("Appointment for " + applicationId + " booked successfully!");
            System.out.println("Date: " + envManager.getEnv("dateReq"));
            System.out.println("From: " + envManager.getEnv("fromTime"));
            System.out.println("To: " + envManager.getEnv("toTime"));
            System.out.println("------------------------------");
        } else {
            int l = result.errors.length;
            for(int i = 0; i < l; i++){
                System.err.println("ERROR: " + result.errors[i].errorCode + ": " + result.errors[i].message);
            }
            System.out.println("------------------------------");
        }
    }
}

class ResponseDataAppoint {
    public String id;
    public String version;
    public String responsetime;
    public ResponseDetailsAppoint response;
    public ErrorsAppoint[] errors;
}

class ResponseDetailsAppoint {
    public BookingStatusResponse[] bookingStatusResponse;
}

class BookingStatusResponse {
    public String bookingMessage;
}

class ErrorsAppoint {
    public String errorCode;
    public String message;
}