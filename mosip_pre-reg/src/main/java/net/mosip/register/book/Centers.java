package net.mosip.register.book;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mosip.envManager;
import okhttp3.*;

public class Centers {
    public static void main(String[] args) throws IOException {
        getCenters();
    }

    public static void getCenters() throws IOException {
        new envManager();
        Console console = System.console();

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/registrationcenters/eng/5/names?name=" + envManager.getEnv("pincode"))
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create JSON parse for body
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataCenters result = objectMapper.readValue(responseBody, ResponseDataCenters.class);

        if (result.errors == null){
            int i = 1;
            ArrayList<String> codesList = new ArrayList<>();
            for (int j = 0; j < result.response.registrationCenters.length; j++){
                if (result.response.registrationCenters[j].isActive) {
                    System.out.println(String.valueOf(i++) + ". " + result.response.registrationCenters[j].name + " (" + result.response.registrationCenters[j].id + "),");
                    System.out.println(result.response.registrationCenters[j].addressLine1 + ",\n" + result.response.registrationCenters[j].addressLine2 + ",\n" + result.response.registrationCenters[j].addressLine3);
                    System.out.println("___________");
                    System.out.println("Contact Person: " + result.response.registrationCenters[j].contactPerson);
                    System.out.println("Phone Number: " + result.response.registrationCenters[j].contactPhone);
                    System.out.println("Time Zone: " + result.response.registrationCenters[j].timeZone);
                    System.out.println("Working hours: " + result.response.registrationCenters[j].centerStartTime + " - " + result.response.registrationCenters[j].centerEndTime);
                    System.out.println("Lunch Time: " + result.response.registrationCenters[j].lunchStartTime + " - " + result.response.registrationCenters[j].lunchEndTime);
                    System.out.println("Number of kiosks: " + result.response.registrationCenters[j].numberOfKiosks);
                    System.out.println("Per Kiosk Process Time: " + result.response.registrationCenters[j].perKioskProcessTime);

                    //This request is to retrieve the working days of the center
                    OkHttpClient client2 = new OkHttpClient().newBuilder()
                        .build();
                    Request request2 = new Request.Builder()
                        .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/workingdays/" + result.response.registrationCenters[j].id + "/eng")
                        .method("GET", null)
                        .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
                        .build();
                    Response response2 = client2.newCall(request2).execute();
                    String responseBody2 = response2.body().string();

                    ObjectMapper objectMapper2 = new ObjectMapper();
                    ResponseWorking result2 = objectMapper2.readValue(responseBody2, ResponseWorking.class);

                    if(result2.errors == null) {
                        System.out.print("Working Days: ");
                        for(int k = 0; k < result2.response.workingdays.length - 1; k++){
                            System.out.print(result2.response.workingdays[k].name + ", ");
                        }
                        System.out.print(result2.response.workingdays[result2.response.workingdays.length - 1].name);
                        System.out.println();
                    } else {
                        int l = result2.errors.length;
                        for(int k = 0; k < l; k++){
                            System.err.println("ERROR: " + result2.errors[k].errorCode + ": " + result2.errors[k].message);
                        }
                        System.out.println("------------------------------");
                    }

                    codesList.add(result.response.registrationCenters[j].id);
                    System.out.println("------------------------------");
                }
                String[] codes = codesList.stream().toArray(String[]::new);

                while(true) {
                    int option = Integer.parseInt(console.readLine("Enter preferred center (enter an integer): "));
                    System.out.println("------------------------------");

                    if(option < 0 || option > codes.length) {
                        System.err.println("ERROR: Please enter a valid integer!");
                        System.out.println("------------------------------");
                    } else {
                        envManager.updateEnv("regCenterId", codes[option - 1]);
                        break;
                    }
                }

                CenterDetailsCheck.checkCenter();
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

class ResponseDataCenters {
    public String id;
    public String version;
    public String responsetime;
    public String metadata;
    public ResponseDetailsCenters response;
    public ErrorsCenters[] errors;
}

class ResponseDetailsCenters {
    public RegistrationCenters[] registrationCenters;
}

class RegistrationCenters {
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

class ErrorsCenters {
    public String errorCode;
    public String message;
}

class ResponseWorking {
    public String id;
    public String version;
    public String responsetime;
    public String metadata;
    public ResponseDetailsWorking response;
    public ErrorsWorking[] errors;
}

class ResponseDetailsWorking {
    public WorkingDays[] workingdays;
}

class WorkingDays {
    public String name;
    public int order;
    public String languageCode;
}

class ErrorsWorking {
    public String errorCode;
    public String message;
}