package net.mosip.delete;

import java.io.IOException;
import java.io.Console;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.*;
import net.mosip.models.delete.*;

import net.mosip.envManager;
import net.mosip.applications.Display;

public class Delete {
    public static void main(String[] args) throws IOException, Error {
        String response = delete_application();
        System.out.println(response);
    }

    public static ResponseDetailsDelete delete_application_call(String applicationId) throws IOException, DeleteException {
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create( "", mediaType);
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/applications/" + applicationId)
            .method("DELETE", body)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataDelete result = objectMapper.readValue(responseBody, ResponseDataDelete.class);

        if (result.errors == null) {
            return result.response;
        } else {
            throw new DeleteException(result);
        }
    }

    public static String delete_application() throws IOException {
        try {
        String chk = Display.display_applications();

        if (chk == "") {
            System.err.println("MESSAGE: No records found for given user id");
            System.out.println("------------------------------");
            return "";
        }
        
        Console console = System.console();
        String applicationId = console.readLine("Enter Application to delete from above: ");
        System.out.println("------------------------------");

        ResponseDetailsDelete resp = delete_application_call(applicationId);

        System.out.println("Deleted application ID: " + resp.preRegistrationId + "!");
        System.out.println("Deleted By: " + resp.deletedBy);
        System.out.println("Deleted On: " + resp.deletedDateTime);
        System.out.println("------------------------------");

        return null;
        } catch (DeleteException ex) {
            System.err.println(ex.getMessage());
            System.out.println("------------------------------");
            return "";
        }
    }
}