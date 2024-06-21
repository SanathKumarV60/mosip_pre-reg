package net.mosip.register.demographic;

import java.io.IOException;
import java.io.Console;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.*;
import net.mosip.envManager;

public class City {
    public static void main(String[] args) throws IOException {
        getCity();
    }

    public static ResponseDetailsCity getCity_call(String auth, String province) throws IOException, ErrorCity {
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/immediatechildren/" + province + "/eng")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + auth)
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataCity result = objectMapper.readValue(responseBody, ResponseDataCity.class);

        if (result.errors == null) {
            return result.response;
        } else {
            throw new ErrorCity(result);
        }
    }

    public static void getCity() throws IOException {

        try {
            ResponseDetailsCity resp = getCity_call(envManager.getEnv("auth"), envManager.getEnv("province"));

            Console console = System.console();

            int i = 1;
            int l = resp.locations.length;
            System.out.println("Available Cities in your selected province: ");
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
                    envManager.updateEnv("city", resp.locations[cInt - 1].code);
                    System.out.println("------------------------------");
                    break;
                }
            }
        } catch (ErrorCity ex) {
            System.err.println(ex.getMessage());
            System.out.println("------------------------------");
        }
    }
}

class ResponseDataCity {
    public String id;
    public String version;
    public String responsetime;
    public MetadataCity metadata;
    public ResponseDetailsCity response;
    public ErrorsCity[] errors;
}

class ResponseDetailsCity {
    public LocationsCity[] locations;
}

class MetadataCity {
    //Empty
}

class ErrorsCity {
    public String errorCode;
    public String message;
}

class LocationsCity {
    public String code;
    public String name;
    public int hierarchyLevel;
    public String hierarchyName;
    public String parentLocCode;
    public String langCode;
    public boolean isActive;
}

class ErrorCity extends Exception {
    public ErrorCity (ResponseDataCity result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}