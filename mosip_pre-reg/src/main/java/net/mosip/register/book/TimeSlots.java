package net.mosip.register.book;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mosip.envManager;
import okhttp3.*;

public class TimeSlots {
    public static void main(String[] args) throws IOException {
        getSlots();
    }

    public static void getSlots() throws IOException {
        new envManager();
        Console console = System.console();

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/appointment/availability/" + envManager.getEnv("regCenterId"))
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create JSON parse for body
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataSlots result = objectMapper.readValue(responseBody, ResponseDataSlots.class);

        if (result.errors == null) {
            ArrayList<String> datesList = new ArrayList<>();
            int n = 1;
            System.out.println("Available Dates:");
            for (int i = 0; i < result.response.centerDetails.length; i++){
                if (!result.response.centerDetails[i].holiday) {
                    System.out.println(String.valueOf(n++) + ". " + result.response.centerDetails[i].date);
                    datesList.add(result.response.centerDetails[i].date);
                } else {
                    continue;
                }
            }
            System.out.println("------------------------------");
            String[] dates = datesList.stream().toArray(String[]::new);

            while (true){
                int date = Integer.parseInt(console.readLine("Which date do you want to book on?(Enter a number): "));
                System.out.println("------------------------------");
                if (date < 1 || date > n) {
                    System.err.println("ERROR: Please enter a valid integer!");
                    System.out.println("------------------------------");
                } else {
                    envManager.updateEnv("dateReq", dates[date - 1]);
                    break;
                }
            }
            
            ArrayList<String[]> slotsList = new ArrayList<>();
            String date = envManager.getEnv("dateReq");
            int m = 1;
            for (int i = 0; i < result.response.centerDetails.length; i++){
                if (result.response.centerDetails[i].date.equals(date)) {
                    for (int j = 0; j < result.response.centerDetails[i].timeSlots.length; j++) {
                        if (result.response.centerDetails[i].timeSlots[j].availability == 0) {
                            continue;
                        } else {
                            System.out.println(String.valueOf(m++) + ". " + result.response.centerDetails[i].timeSlots[j].fromTime + " - " + result.response.centerDetails[i].timeSlots[j].toTime);

                            String[] val = {result.response.centerDetails[i].timeSlots[j].fromTime, result.response.centerDetails[i].timeSlots[j].toTime};
                            slotsList.add(val);
                        }
                    }
                } else {
                    continue;
                }
            }
            System.out.println("------------------------------");
            String[][] slots = slotsList.stream().toArray(String[][]::new);
            
            while (true){
                int slot = Integer.parseInt(console.readLine("Which time slot is preferred? (Enter a number): "));
                System.out.println("------------------------------");
                if (slot < 1 || slot > m) {
                    System.err.println("ERROR: Please enter a valid integer!");
                    System.out.println("------------------------------");
                } else {
                    envManager.updateEnv("fromTime", slots[slot - 1][0]);
                    envManager.updateEnv("toTime", slots[slot - 1][1]);
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

class ResponseDataSlots {
    public String id;
    public String version;
    public String responsetime;
    public String metadata;
    public ResponseDetailsSlots response;
    public ErrorsSlots[] errors;
}

class ResponseDetailsSlots {
    public String regCenterId;
    public CenterDetails[] centerDetails;
}

class CenterDetails {
    public String date;
    public Slots[] timeSlots;
    public boolean holiday;
}

class Slots {
    public String fromTime;
    public String toTime;
    public int availability;
}

class ErrorsSlots {
    public String errorCode;
    public String message;
}