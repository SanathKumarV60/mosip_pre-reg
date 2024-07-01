package net.mosip.register.confirmation;

import java.io.IOException;
import java.io.Console;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mosip.envManager;
import net.mosip.models.register.confirmation.onScreenAck.*;

import okhttp3.*;

public class OnScreenAck {
    public static void main(String[] args) throws IOException {
        getAck();
    }

    public static ResponseDetailsAck getAck_call(String auth) throws IOException, AckException {
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/templates/eng/Onscreen-Acknowledgement")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + auth)
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataAck result = objectMapper.readValue(responseBody, ResponseDataAck.class);

        if (result.errors == null) {
            return result.response;
        } else {
            throw new AckException(result);
        }
    }

    public static void getAck() throws IOException {
        try {
            ResponseDetailsAck resp = getAck_call(envManager.getEnv("auth"));

            Console console = System.console();

            System.out.println(resp.templates[0].fileText);
            System.out.println("------------------------------");

            while (true) {
                String notify = console.readLine("Would you like a notification? (Y/N): ");
                System.out.println("------------------------------");
                if (notify.equals("Y") || notify.equals("y")) {
                    Notify.getNotif(envManager.getEnv("applicationId"));
                    break;
                } else if (notify.equals("N") || notify.equals("n")) {
                    break;
                } else {
                    System.err.println("ERROR: Please enter either (Y/y/N/n)!");
                    System.out.println("------------------------------");
                }
            }
        } catch (AckException ex) {
            System.err.println(ex.getMessage());
            System.out.println("------------------------------");
        }
    }
}