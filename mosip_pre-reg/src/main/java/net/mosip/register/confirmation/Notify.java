package net.mosip.register.confirmation;

import java.io.File;
import java.io.IOException;
import java.io.Console;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mosip.envManager;
import net.mosip.models.register.confirmation.notify.*;

import okhttp3.*;

public class Notify {
    public static void main(String[] args) throws IOException {
        getNotif(envManager.getEnv("applicationId"));
    }

    public static ResponseDetailsNotif getNotif_call(String auth, String name, String applicationId, String date, String appointmentTime, String phone, String email, String filePath) throws IOException, NotifyException {
        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        OffsetDateTime gmtTime = now.withOffsetSameInstant(ZoneOffset.UTC);
        String formattedTime = formatter.format(gmtTime);

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("NotificationRequestDTO","{\"id\":\"mosip.pre-registration.notification.notify\",\"request\":{\"name\":\"" + name + "\",\"preRegistrationId\":\"" + applicationId + "\",\"appointmentDate\":\"" + date + "\",\"appointmentTime\":\"" + appointmentTime + "\",\"mobNum\":\"" + phone + "\",\"emailID\":\"" + email + "\",\"additionalRecipient\":false,\"isBatch\":false},\"version\":\"1.0\",\"requesttime\":\"" + formattedTime + "\"}")
            .addFormDataPart("langCode","eng")
            .addFormDataPart("attachment", "ack.pdf",
                RequestBody.create(new File(filePath), 
                MediaType.parse("application/octet-stream")))
        .build();
        Request request = new Request.Builder()
        .url("https://uat2.mosip.net//preregistration/v1/notification/notify")
        .method("POST", body)
        .addHeader("Cookie", "Authorization=" + auth)
        .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create JSON parse for body
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataNotif result = objectMapper.readValue(responseBody, ResponseDataNotif.class);        

        if (result.errors == null) {
            return result.response;
        } else {
            throw new NotifyException(result);
        }
    }

    public static void getNotif(String applicationId) throws IOException {
        Console console = System.console();

        String appointmentTime = envManager.getEnv("fromTime");
        int part = Integer.parseInt(appointmentTime.substring(0, 2));
        if ( part > 12) {
            String check = String.valueOf(part - 12);
            if (check.length() == 1) {
                check = "0" + check;
            }
            appointmentTime = check + appointmentTime.substring(2, 5)+ " PM";
        } else {
            appointmentTime = appointmentTime.substring(0, 5) + " AM";
        }

        String phone = console.readLine("Phone number: ");
        System.out.println("------------------------------");
        String email = console.readLine("E-mail: ");
        System.out.println("------------------------------");

        //  THIS NEEDS TO BE REPLACED WITH THE DOCUMENT THAT IS CREATED
        //  BY THE SYSTEM WITH THE QR CODE AND CONFIRMATION
        String filePath = "../demo_ack.pdf";

        try {
            ResponseDetailsNotif resp = getNotif_call(envManager.getEnv("auth"), envManager.getEnv("name"), applicationId, envManager.getEnv("dateReq"), appointmentTime, phone, email, filePath);

            System.out.println(resp.message);
            System.out.println("------------------------------");
        } catch (NotifyException ex) {
            System.err.println(ex.getMessage());
            System.out.println("------------------------------");
        }
    }
}