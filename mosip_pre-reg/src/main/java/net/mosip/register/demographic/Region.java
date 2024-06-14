package net.mosip.register.demographic;

import java.io.IOException;
import java.io.Console;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.*;
import net.mosip.envManager;

public class Region {
    public static void main(String[] args) throws IOException{
        getRegion();
    }

    public static void getRegion() throws IOException{
        new envManager();
        Console console = System.console();

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/immediatechildren/MOR/eng")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataRegion result = objectMapper.readValue(responseBody, ResponseDataRegion.class);

        if (result.errors == null) {
            int i = 1;
            int l = result.response.locations.length;
            System.out.println("Available Regions: ");
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
                    envManager.updateEnv("region", result.response.locations[cInt - 1].code);
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

class ResponseDataRegion {
    public String id;
    public String version;
    public String responsetime;
    public MetadataRegion metadata;
    public ResponseDetailsRegion response;
    public ErrorsRegion[] errors;
}

class ResponseDetailsRegion {
    public LocationsRegion[] locations;
}

class MetadataRegion {
    //Empty
}

class ErrorsRegion {
    public String errorCode;
    public String message;
}

class LocationsRegion {
    public String code;
    public String name;
    public int hierarchyLevel;
    public String hierarchyName;
    public String parentLocCode;
    public String langCode;
    public boolean isActive;
}