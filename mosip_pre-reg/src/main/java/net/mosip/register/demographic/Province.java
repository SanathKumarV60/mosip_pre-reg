package net.mosip.register.demographic;

import java.io.IOException;
import java.io.Console;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.*;
import net.mosip.envManager;

public class Province {
    public static void main(String[] args) throws IOException {
        getProvince();
    }

    public static ResponseDetailsProvince getProvince_call(String auth, String region) throws IOException, ErrorProvince {
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/immediatechildren/" + region + "/eng")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataProvince result = objectMapper.readValue(responseBody, ResponseDataProvince.class);


        if (result.errors == null) {
            return result.response;
        } else {
            throw new ErrorProvince(result);
        }
    }

    public static void getProvince() throws IOException {
        try {
            ResponseDetailsProvince resp = getProvince_call(envManager.getEnv("auth"), envManager.getEnv("region"));
            Console console = System.console();
            int i = 1;
            int l = resp.locations.length;
            System.out.println("Available Provinces in your selected region: ");
            while(i <= l && resp.locations[i - 1].isActive){
                System.out.println(String.valueOf(i) + ". " + resp.locations[i - 1].name);
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
                    envManager.updateEnv("province", resp.locations[cInt - 1].code);
                    System.out.println("------------------------------");
                    break;
                }
            }

        } catch (ErrorProvince ex) {
            System.err.println(ex.getMessage());
            System.out.println("------------------------------");
        }
    }
}

class ResponseDataProvince {
    public String id;
    public String version;
    public String responsetime;
    public MetadataProvince metadata;
    public ResponseDetailsProvince response;
    public ErrorsProvince[] errors;
}

class ResponseDetailsProvince {
    public LocationsProvince[] locations;
}

class MetadataProvince {
    //Empty
}

class ErrorsProvince {
    public String errorCode;
    public String message;
}

class LocationsProvince {
    public String code;
    public String name;
    public int hierarchyLevel;
    public String hierarchyName;
    public String parentLocCode;
    public String langCode;
    public boolean isActive;
}

class ErrorProvince extends Exception {
    public ErrorProvince (ResponseDataProvince result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}