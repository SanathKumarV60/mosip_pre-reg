package net.mosip.register.demographic;

import java.io.IOException;
import java.io.Console;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.*;
import net.mosip.models.register.demographic.zone.*;

import net.mosip.envManager;

public class Zone {
    public static void main(String[] args) throws IOException{
        getZone();
    }

    public static ResponseDetailsZone getZone_call(String auth, String city) throws IOException, ZoneException {
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/immediatechildren/" + city + "/eng")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + auth)
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //System.out.println(responseBody);

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataZone result = objectMapper.readValue(responseBody, ResponseDataZone.class);

        if (result.errors == null) {
            return result.response;
        } else {
            throw new ZoneException(result);
        }
    }

    public static void getZone() throws IOException {
        try {
            ResponseDetailsZone resp = getZone_call(envManager.getEnv("auth"), envManager.getEnv("city"));
            Console console = System.console();

            int i = 1;
            int l = resp.locations.length;
            System.out.println("Available Zones in your selected city: ");
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
                    envManager.updateEnv("zone", resp.locations[cInt - 1].code);
                    System.out.println("------------------------------");
                    break;
                }
            }
        } catch (ZoneException ex) {
            System.out.println(ex.getMessage());
            System.out.println("------------------------------");
        }
    }
}
