package net.mosip.register.book;

import java.io.Console;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mosip.envManager;
import okhttp3.*;

public class CenterDetailsCheck {
    public static void main(String[] args) throws IOException {
        checkCenter();
    }

    public static void checkCenter() throws IOException {
        new envManager();
        Console console = System.console();

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/registrationcenters/" + envManager.getEnv("regCenterId") + "/eng")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create JSON parse for body
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataSelect result = objectMapper.readValue(responseBody, ResponseDataSelect.class);

        if (result.errors == null){
            System.out.println("Selected Center: " + result.response.registrationCenters[0].name);
            while (true) {
                String cont = console.readLine("Would you like to proceed with this Center? (Y/N): ");
                System.out.println("------------------------------");
                if (cont.equals("Y") || cont.equals("y")) {
                    break;
                } else if (cont.equals("N") || cont.equals("n")) {
                    Centers.getCenters();
                    break;
                } else {
                    System.err.println("ERROR: Pleas enter either (Y/y/N/n)!");
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

    public static void all_details(String reg_cen_id) throws IOException {
        new envManager();

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/registrationcenters/" + reg_cen_id + "/eng")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create JSON parse for body
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataSelect result = objectMapper.readValue(responseBody, ResponseDataSelect.class);

        if (result.errors == null){
            System.out.println("Name of Center: " + result.response.registrationCenters[0].name);
            System.out.println("Contact: " + result.response.registrationCenters[0].contactPerson + " (" + result.response.registrationCenters[0].contactPhone + ")");
            System.out.println("------------------------------");
        } else {
            int l = result.errors.length;
            for(int i = 0; i < l; i++){
                System.err.println("ERROR: " + result.errors[i].errorCode + ": " + result.errors[i].message);
            }
            System.out.println("------------------------------");
        }
    }
}

class ResponseDataSelect {
    public String id;
    public String version;
    public String responsetime;
    public ResponseDetailsSelect response;
    public String[] metadata;
    public ErrorsSelect[] errors;
}

class ResponseDetailsSelect {
    public RegistrationCentersSelect[] registrationCenters;
}

class RegistrationCentersSelect {
    public String id;
    public String name;
    public String centerTypeCode;
    public String addressLine1;
    public String addressLine2;
    public String addressLine3;
    public String latitude;
    public String longitude;
    public String locationCode;
    public String holidayLocationCode;
    public String contactPhone;
    public String workingHours;
    public String langCode;
    public int numberOfKiosks;
    public String perKioskProcessTime;
    public String centerStartTime;
    public String centerEndTime;
    public String timeZone;
    public String contactPerson;
    public String lunchStartTime;
    public String lunchEndTime;
    public boolean isActive;
    public String zoneCode;
}

class ErrorsSelect {
    public String errorCode;
    public String message;
}