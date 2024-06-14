package net.mosip.register.demographic;

import java.io.IOException;
import java.io.Console;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.*;
import net.mosip.envManager;

public class Zone {
    public static void main(String[] args) throws IOException{
        getZone();
    }

    public static void getZone() throws IOException{
        new envManager();
        Console console = System.console();

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/immediatechildren/" + envManager.getEnv("city") + "/eng")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //System.out.println(responseBody);

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataZone result = objectMapper.readValue(responseBody, ResponseDataZone.class);

        if (result.errors == null) {
            int i = 1;
            int l = result.response.locations.length;
            System.out.println("Available Zones in your selected city: ");
            while(i <= l && result.response.locations[i - 1].isActive){
                System.out.println(String.valueOf(i) + ". " + result.response.locations[i - 1].name);
                i++;
            }
            while(true){
                String c =  console.readLine("Enter a choice: ");
                int cInt = Integer.parseInt(c);
                if(cInt < 0 || cInt > l){
                    System.out.println("------------------------------");
                    System.err.println("ERROR: Please enter a valid number!");
                    System.out.println("------------------------------");
                } else {
                    envManager.updateEnv("zone", result.response.locations[cInt - 1].code);
                    System.out.println("------------------------------");
                    break;
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

class ResponseDataZone {
    public String id;
    public String version;
    public String responsetime;
    public MetadataZone metadata;
    public ResponseDetailsZone response;
    public ErrorsZone[] errors;
}

class ResponseDetailsZone {
    public LocationsZone[] locations;
}

class MetadataZone {
    //Empty
}

class ErrorsZone {
    public String errorCode;
    public String message;
}

class LocationsZone {
    public String code;
    public String name;
    public int hierarchyLevel;
    public String hierarchyName;
    public String parentLocCode;
    public String langCode;
    public boolean isActive;
}