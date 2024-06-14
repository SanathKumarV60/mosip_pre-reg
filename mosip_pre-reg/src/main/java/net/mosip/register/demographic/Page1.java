package net.mosip.register.demographic;

import java.io.Console;
import java.io.IOException;

import java.util.regex.*;

import net.mosip.envManager;
import okhttp3.*;

public class Page1 {
    public static void main(String[] args) throws IOException {
        fillPage();
    }

    public static void fillPage() throws IOException{
        new envManager();

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/dynamicfields?pageNumber=1&pageSize=10")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        @SuppressWarnings("unused")
        String responseBody = response.body().string();

        Console console = System.console();

        //INPUT PHONE NUMBER
        while(true) {
            String ph = console.readLine("Phone Number: ");
            System.out.println("------------------------------");
            if(ph.matches("\\d+") && ph.length() == 10){
                envManager.updateEnv("phoneNumber", ph);
                break;
            } else {
                System.err.println("ERROR: Please enter a Valid 10-digit number!");
                System.out.println("------------------------------");
            }
        }

        //INPUT EMAIL
        while(true){
            String email = console.readLine("E-mail: ");
            System.out.println("------------------------------");
            if(isValidEmail(email)){
                envManager.updateEnv("email", email);
                break;
            } else {
                System.err.println("ERROR: Please enter a Valid E-mail!");
                System.out.println("------------------------------");
            }
        }

    }

    public static boolean isValidEmail(String email) {
    if (email == null) {
        return false;
    }
    
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}