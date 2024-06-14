package net.mosip.cancel;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.io.Console;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.*;
import net.mosip.envManager;
import net.mosip.applications.Display;

public class Cancel {
    public static void main(String[] args) throws IOException{
        String response = cancel_application();
        System.out.println(response);
    }

    public static String cancel_application() throws IOException{
        new envManager();

        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        OffsetDateTime gmtTime = now.withOffsetSameInstant(ZoneOffset.UTC);
        String formattedTime = formatter.format(gmtTime);
        envManager.updateEnv("currentTime", formattedTime);

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

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create( "{\r\n    \"id\": \"mosip.pre-registration.booking.book\",\r\n    \"request\": {\r\n        \"registration_center_id\": \"" + envManager.getEnv("regCenterId") +"\",\r\n        \"appointment_date\": \"" + envManager.getEnv("reqDate") + "\",\r\n        \"time_slot_from\": \"" + envManager.getEnv("fromTime") + "\",\r\n        \"time_slot_to\": \"" + envManager.getEnv("toTime") + "\",\r\n        \"pre_registration_id\": \"" + applicationId + "\"\r\n    },\r\n    \"version\": \"1.0\",\r\n    \"requesttime\": \"" + formattedTime + "\"\r\n}", mediaType);
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/appointment/" + applicationId)
            .method("PUT", body)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseData result = objectMapper.readValue(responseBody, ResponseData.class);

        if (result.errors == null) {
            System.out.println("Cancelled Appointment for application ID: " + applicationId + "!");
            System.out.println("Transaction ID " + result.response.transactionId);
            System.out.println("------------------------------");
        } else if (result.errors[0].errorCode.equals("PRG_BOOK_RCI_013")) {
            System.err.println("ERROR (PRG_BOOK_RCI_013): The given application ID has not yet been booked!");
            System.out.println("------------------------------");
        } else {
            int l = result.errors.length;
            for(int i = 0; i < l; i++){
                System.err.println("ERROR: " + result.errors[i].errorCode + ": " + result.errors[i].message);
            }
            System.out.println("------------------------------");
        }

        return responseBody;
    }
}

class ResponseData {
    public String id;
    public String version;
    public String responsetime;
    public ResponseDetails response;
    public Errors[] errors;
}

class ResponseDetails {
    public String transactionId;
    public String message;
}

class Errors {
    public String errorCode;
    public String message;
}