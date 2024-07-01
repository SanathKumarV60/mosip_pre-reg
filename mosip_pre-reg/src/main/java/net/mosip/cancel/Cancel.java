package net.mosip.cancel;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.io.Console;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.*;
import net.mosip.models.cancel.*;

import net.mosip.envManager;
import net.mosip.applications.Display;

public class Cancel {
    public static void main(String[] args) throws IOException, Error {
        cancel_application();
    }

    public static ResponseDetailsCancel cancel_application_call(String auth, String regCenterId, String date, String fromTime, String toTime, String applicationId) throws IOException, CancelError {
        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        OffsetDateTime gmtTime = now.withOffsetSameInstant(ZoneOffset.UTC);
        String formattedTime = formatter.format(gmtTime);
        envManager.updateEnv("currentTime", formattedTime);

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create( "{\r\n    \"id\": \"mosip.pre-registration.booking.book\",\r\n    \"request\": {\r\n        \"registration_center_id\": \"" + regCenterId +"\",\r\n        \"appointment_date\": \"" + date + "\",\r\n        \"time_slot_from\": \"" + fromTime + "\",\r\n        \"time_slot_to\": \"" + toTime + "\",\r\n        \"pre_registration_id\": \"" + applicationId + "\"\r\n    },\r\n    \"version\": \"1.0\",\r\n    \"requesttime\": \"" + formattedTime + "\"\r\n}", mediaType);
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/appointment/" + applicationId)
            .method("PUT", body)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataCancel result = objectMapper.readValue(responseBody, ResponseDataCancel.class);

        if (result.errors == null) {
            return result.response;
        } else {
            throw new CancelError(result);
        }

    }

    public static String cancel_application() throws IOException {
        int count = Display.display_booked();
        if (count == 0){
            System.out.println("MESSAGE: No Booked Records Found!");
            System.out.println("------------------------------");
            return null;
        }
        
        Console console = System.console();
        String applicationId = console.readLine("Enter Application to cancel from above: ");
        System.out.println("------------------------------");
        Display.updateEnvCancel(applicationId);

        try {
            ResponseDetailsCancel resp = cancel_application_call(envManager.getEnv("auth"), envManager.getEnv("regCenterId"), envManager.getEnv("reqDate"), envManager.getEnv("fromTime"), envManager.getEnv("toTime"), applicationId);
            System.out.println("Cancelled Appointment for application ID: " + applicationId + "!");
            System.out.println("Transaction ID " + resp.transactionId);
            System.out.println("------------------------------");
            return null;
        } catch (CancelError ex) {
            System.err.println(ex.getMessage());
            System.out.println("------------------------------");
            return "";
        }
    }
}