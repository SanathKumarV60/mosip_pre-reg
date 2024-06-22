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

    public static ResponseDetailsCenters getCenters_call(String auth, String pincode) throws IOException, ErrorCenters {
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/registrationcenters/eng/5/names?name=" + pincode)
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + auth)
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create JSON parse for body
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataCenters result = objectMapper.readValue(responseBody, ResponseDataCenters.class);

        if (result.errors == null) {
            return result.response;
        } else {
            throw new ErrorCenters(result);
        }
    }

    public static ResponseDetailsWorking workingDays_call(String auth, String regCenterId) throws IOException, ErrorWorking {
        //This request is to retrieve the working days of the center
        OkHttpClient client2 = new OkHttpClient().newBuilder()
            .build();
        Request request2 = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/workingdays/" + regCenterId + "/eng")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + auth)
            .build();
        Response response2 = client2.newCall(request2).execute();
        String responseBody2 = response2.body().string();

        ObjectMapper objectMapper2 = new ObjectMapper();
        ResponseWorking result2 = objectMapper2.readValue(responseBody2, ResponseWorking.class);

        if (result2.errors == null) {
            return result2.response;
        } else{
            throw new ErrorWorking(result2);
        }
    }

    public static void getCenters() throws IOException {
        try {
            ResponseDetailsCenters resp = getCenters_call(envManager.getEnv("auth"), envManager.getEnv("pincode"));

            Console console = System.console();

            int i = 1;
            ArrayList<String> codesList = new ArrayList<>();
            for (int j = 0; j < resp.registrationCenters.length; j++){
                if (resp.registrationCenters[j].isActive) {
                    System.out.println(String.valueOf(i++) + ". " + resp.registrationCenters[j].name + " (" + resp.registrationCenters[j].id + "),");
                    System.out.println(resp.registrationCenters[j].addressLine1 + ",\n" + resp.registrationCenters[j].addressLine2 + ",\n" + resp.registrationCenters[j].addressLine3);
                    System.out.println("___________");
                    System.out.println("Contact Person: " + resp.registrationCenters[j].contactPerson);
                    System.out.println("Phone Number: " + resp.registrationCenters[j].contactPhone);
                    System.out.println("Time Zone: " + resp.registrationCenters[j].timeZone);
                    System.out.println("Working hours: " + resp.registrationCenters[j].centerStartTime + " - " + resp.registrationCenters[j].centerEndTime);
                    System.out.println("Lunch Time: " + resp.registrationCenters[j].lunchStartTime + " - " + resp.registrationCenters[j].lunchEndTime);
                    System.out.println("Number of kiosks: " + resp.registrationCenters[j].numberOfKiosks);
                    System.out.println("Per Kiosk Process Time: " + resp.registrationCenters[j].perKioskProcessTime);

                    try {
                        ResponseDetailsWorking resp2 = workingDays_call(envManager.getEnv("auth"), resp.registrationCenters[j].id);

                        System.out.print("Working Days: ");
                        for(int k = 0; k < resp2.workingdays.length - 1; k++){
                            System.out.print(resp2.workingdays[k].name + ", ");
                        }
                        System.out.print(resp2.workingdays[resp2.workingdays.length - 1].name);
                        System.out.println();
                    } catch (ErrorWorking ex) {
                        System.err.println(ex.getMessage());
                        System.out.println("------------------------------");
                    }

                    codesList.add(resp.registrationCenters[j].id);
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
        } catch (ErrorCenters ex) {
            System.out.println(ex.getMessage());
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

class ErrorCenters extends Exception {
    public ErrorCenters (ResponseDataCenters result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}

class ErrorWorking extends Exception {
    public ErrorWorking (ResponseWorking result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}