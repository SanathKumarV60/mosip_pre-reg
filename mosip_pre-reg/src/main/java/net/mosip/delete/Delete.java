package net.mosip.delete;

import java.io.IOException;
import java.io.Console;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.*;
import net.mosip.envManager;
import net.mosip.applications.Display;

public class Delete {
    public static void main(String[] args) throws IOException{
        String response = delete_application();
        System.out.println(response);
    }

    public static String delete_application() throws IOException{
        new envManager();
        
        String resp = Display.display_applications();

        if (resp == "") {
            System.err.println("MESSAGE: No records found for given user id");
            System.out.println("------------------------------");
            return "";
        }
        
        Console console = System.console();
        String applicationId = console.readLine("Enter Application to delete from above: ");
        System.out.println("------------------------------");

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
        ResponseData result = objectMapper.readValue(responseBody, ResponseData.class);

        if (result.errors == null) {
            System.out.println("Deleted application ID: " + result.response.preRegistrationId + "!");
            System.out.println("Deleted By: " + result.response.deletedBy);
            System.out.println("Deleted On: " + result.response.deletedDateTime);
            System.out.println("------------------------------");
        } else if (result.errors[0].errorCode.equals("PRG_PAM_APP_005")) {
            System.err.println("ERROR (PRG_PAM_APP_005): No Such application ID exists!");
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
    public String preRegistrationId;
    public String deletedBy;
    public String deletedDateTime;
}

class Errors {
    public String errorCode;
    public String message;
}