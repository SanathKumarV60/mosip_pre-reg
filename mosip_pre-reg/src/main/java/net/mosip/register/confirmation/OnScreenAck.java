package net.mosip.register.confirmation;

import java.io.IOException;
import java.io.Console;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mosip.envManager;
import okhttp3.*;

public class OnScreenAck {
    public static void main(String[] args) throws IOException {
        getAck();
    }

    public static void getAck() throws IOException {
        new envManager();
        Console console = System.console();

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/templates/eng/Onscreen-Acknowledgement")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataAck result = objectMapper.readValue(responseBody, ResponseDataAck.class);

        if (result.errors == null) {
            System.out.println(result.response.templates[0].fileText);
            System.out.println("------------------------------");

            while (true) {
                String notify = console.readLine("Would you like a notification? (Y/N): ");
                System.out.println("------------------------------");
                if (notify.equals("Y") || notify.equals("y")) {
                    Notify.getNotif(envManager.getEnv("applicationId"));
                    break;
                } else if (notify.equals("N") || notify.equals("n")) {
                    break;
                } else {
                    System.err.println("ERROR: Please enter either (Y/y/N/n)!");
                    System.out.println("------------------------------");
                }
            }
        } else {
            int l = result.errors.length;
            for(int i = 0; i < l; i++){
                System.err.println("ERROR: " + result.errors[i].errorCode + ": " + result.errors[i].message);
            }
            System.out.println("------------------------------");
        }
    }
}

class ResponseDataAck {
    public String id;
    public String version;
    public String responsetime;
    public String[] metadata;
    public ResponseDetailsAck response;
    public ErrorsAck[] errors;
}

class ResponseDetailsAck {
    public Templates[] templates;
}

class Templates {
    public String id;
    public String name;
    public String description;
    public String fileFormatCode;
    public String model;
    public String fileText;
    public String moduleId;
    public String moduleName;
    public String templateTypeCode;
    public String langCode;
    public boolean isActive;
}

class ErrorsAck {
    public String errorCode;
    public String message;
}