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

    public static ResponseDetailsSlots getSlots_call(String auth, String regCenterId) throws IOException, ErrorSlots {
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/appointment/availability/" + regCenterId)
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + auth)
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create JSON parse for body
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataSlots result = objectMapper.readValue(responseBody, ResponseDataSlots.class);

        if (result.errors == null) {
            return result.response;
        } else {
            throw new ErrorSlots(result);
        }
    }

    public static void getSlots() throws IOException {
        try {
            ResponseDetailsSlots resp = getSlots_call(envManager.getEnv("auth"), envManager.getEnv("regCenterId"));

            Console console = System.console();

            ArrayList<String> datesList = new ArrayList<>();
            int n = 1;
            System.out.println("Available Dates:");
            for (int i = 0; i < resp.centerDetails.length; i++){
                if (!resp.centerDetails[i].holiday) {
                    System.out.println(String.valueOf(n++) + ". " + resp.centerDetails[i].date);
                    datesList.add(resp.centerDetails[i].date);
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
            for (int i = 0; i < resp.centerDetails.length; i++){
                if (resp.centerDetails[i].date.equals(date)) {
                    for (int j = 0; j < resp.centerDetails[i].timeSlots.length; j++) {
                        if (resp.centerDetails[i].timeSlots[j].availability == 0) {
                            continue;
                        } else {
                            System.out.println(String.valueOf(m++) + ". " + resp.centerDetails[i].timeSlots[j].fromTime + " - " + resp.centerDetails[i].timeSlots[j].toTime);

                            String[] val = {resp.centerDetails[i].timeSlots[j].fromTime, resp.centerDetails[i].timeSlots[j].toTime};
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

        } catch (ErrorSlots ex) {
            System.err.println(ex.getMessage());
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

class ErrorSlots extends Exception {
    public ErrorSlots (ResponseDataSlots result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}