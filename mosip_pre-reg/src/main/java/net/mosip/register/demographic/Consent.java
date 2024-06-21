package net.mosip.register.demographic;

import java.io.Console;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.*;
import net.mosip.envManager;

public class Consent {
    public static void main(String[] args) throws IOException{
        boolean response = giveConsent();
        System.out.println(response);
    }

    public static ResponseDetailsConsent giveConsent_call(String auth) throws IOException, ErrorConsent {
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/templates/eng/consent")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + auth)
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataConsent result = objectMapper.readValue(responseBody, ResponseDataConsent.class);

        if (result.errors == null) {
            return result.response;
        } else {
            throw new ErrorConsent(result);
        }
    }

    public static boolean giveConsent() throws IOException{
        boolean consented = false;

        try {
            ResponseDetailsConsent resp = giveConsent_call(envManager.getEnv("auth"));
            System.out.println(resp.templates[0].fileText);
            Console console = System.console();
        
            String accept = console.readLine("Do you give Consent? (Y/N): ");
            System.out.println("------------------------------");

            boolean valid = false;
            while(!valid){
                if (accept.equals("y") || accept.equals("Y")){
                    valid = consented = true;
                    System.out.println("MESSAGE: Please begin filling the form!");
                    System.out.println("------------------------------");
                } else if (accept.equals("n") || accept.equals("N")) {
                    valid = true;
                    consented = false;
                    System.out.println("MESSAGE: Consent not given! Please Try Again!");
                    System.out.println("------------------------------");
                } else {
                    accept = getValidInput();
                }
            }
        } catch (ErrorConsent ex) {
            System.err.println(ex.getMessage());
            System.out.println("------------------------------");
        }

        return consented;
    }

    private static String getValidInput() {
        Console console = System.console();
        String accept;
    
        do {
            System.err.println("ERROR: Please enter a valid choice (y/Y or n/N)!");
            System.out.println("------------------------------");
            accept = console.readLine("Do you give Consent? (Y/N): ");
            System.out.println("------------------------------");
        } while (!accept.equals("y") && !accept.equals("Y") && !accept.equals("n") && !accept.equals("N"));
    
        return accept;
    }
}

class ResponseDataConsent {
    public String id;
    public String version;
    public String responsetime;
    public MetadataConsent[] metadata;
    public ResponseDetailsConsent response;
    public ErrorsConsent[] errors;
}

class ResponseDetailsConsent {
    public TemplatesConsent[] templates;
}

class MetadataConsent {
    //empty
}

class TemplatesConsent {
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

class ErrorsConsent {
    public String errorCode;
    public String message;
}

class ErrorConsent extends Exception {
    public ErrorConsent (ResponseDataConsent result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}