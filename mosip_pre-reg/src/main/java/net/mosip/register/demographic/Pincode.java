package net.mosip.register.demographic;

import java.io.IOException;
import java.io.Console;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.*;
import net.mosip.models.register.demographic.pincode.*;

import net.mosip.envManager;

public class Pincode {
    public static void main(String[] args) throws IOException{
        getPin();
    }

    public static ResponseDetailsPin getPin_call(String auth, String zone) throws IOException, PincodeException {
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/immediatechildren/" + zone + "/eng")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + auth)
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataPin result = objectMapper.readValue(responseBody, ResponseDataPin.class);

        if (result.errors == null) {
            return result.response;
        } else {
            throw new PincodeException(result);
        }
    }

    public static void getPin() throws IOException{
        try {
            ResponseDetailsPin resp = getPin_call(envManager.getEnv("auth"), envManager.getEnv("zone"));
            Console console = System.console();
            
            int i = 1;
            int l = resp.locations.length;
            System.out.println("Available pins in your selected zone: ");
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
                    envManager.updateEnv("pincode", resp.locations[cInt - 1].code);
                    System.out.println("------------------------------");
                    break;
                }
            }
        } catch (PincodeException ex) {
            System.err.println(ex.getMessage());
            System.out.println("------------------------------");
        }
    }
}
