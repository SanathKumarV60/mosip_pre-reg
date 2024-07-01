package net.mosip.register.demographic;

import java.io.IOException;
import java.io.Console;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.*;
import net.mosip.models.register.demographic.region.*;

import net.mosip.envManager;

public class Region {
    public static void main(String[] args) throws IOException {
        getRegion();
    }

    public static ResponseDetailsRegion getRegion_call(String auth) throws IOException, RegionException {
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/immediatechildren/MOR/eng")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + auth)
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataRegion result = objectMapper.readValue(responseBody, ResponseDataRegion.class);

        if (result.errors == null) {
            return result.response;
        } else {
            throw new RegionException(result);
        }
    }

    public static void getRegion() throws IOException {
        try {
            ResponseDetailsRegion resp = getRegion_call(envManager.getEnv("auth"));
            Console console = System.console();

            int i = 1;
            int l = resp.locations.length;
            System.out.println("Available Regions: ");
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
                    envManager.updateEnv("region", resp.locations[cInt - 1].code);
                    System.out.println("------------------------------");
                    break;
                }
            }
        } catch (RegionException ex) {
            System.err.println(ex.getMessage());
            System.out.println("------------------------------");
        }
    }
}
