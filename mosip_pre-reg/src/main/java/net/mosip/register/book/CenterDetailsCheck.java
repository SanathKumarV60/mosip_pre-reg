package net.mosip.register.book;

import java.io.Console;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mosip.envManager;
import net.mosip.models.register.book.centerDetailsCheck.*;

import okhttp3.*;

public class CenterDetailsCheck {
    public static void main(String[] args) throws IOException {
        //checkCenter();
        all_details(envManager.getEnv("regCenterId"));
    }

    public static ResponseDetailsSelect checkCenter_call(String auth, String regCenterId) throws IOException, SelectException {
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/registrationcenters/" + regCenterId + "/eng")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + auth)
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create JSON parse for body
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataSelect result = objectMapper.readValue(responseBody, ResponseDataSelect.class);

        if (result.errors == null) {
            return result.response;
        } else {
            throw new SelectException(result);
        }
    }

    public static void checkCenter() throws IOException {

        try {
            ResponseDetailsSelect resp = checkCenter_call(envManager.getEnv("auth"), envManager.getEnv("regCenterId"));

            Console console = System.console();
            
            System.out.println("Selected Center: " + resp.registrationCenters[0].name);
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
        } catch (SelectException ex) {
            System.err.println(ex.getMessage());
            System.out.println("------------------------------");
        }
    }

    public static ResponseDetailsSelect all_details_call(String auth, String regCenterId) throws IOException, SelectException {
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/registrationcenters/" + regCenterId + "/eng")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + auth)
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create JSON parse for body
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataSelect result = objectMapper.readValue(responseBody, ResponseDataSelect.class);

        if (result.errors == null) {
            return result.response;
        } else {
            throw new SelectException(result);
        }
    }

    public static void all_details(String reg_cen_id) throws IOException {

        try {
            ResponseDetailsSelect resp = all_details_call(envManager.getEnv("auth"), reg_cen_id);

            System.out.println("Name of Center: " + resp.registrationCenters[0].name);
            System.out.println("Contact: " + resp.registrationCenters[0].contactPerson + " (" + resp.registrationCenters[0].contactPhone + ")");
            System.out.println("------------------------------");
        } catch (SelectException ex) {
            System.err.println(ex.getMessage());
            System.out.println("------------------------------");
        }
    }
}