package net.mosip.register.demographic;

import java.io.Console;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.mosip.envManager;
import okhttp3.*;

public class Page0 {
    public static void main(String[] args) throws IOException {
        fillPage();
    }

    public static void fillPage() throws IOException{
        new envManager();

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/dynamicfields?pageNumber=0&pageSize=10")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        @SuppressWarnings("unused")
        String responseBody = response.body().string();

        Console console = System.console();

        //INPUT NAME
        String name = console.readLine("Full Name: ");
        envManager.updateEnv("name", name);
        
        System.out.println("------------------------------");
        //INPUT DATE OF BIRTH
        String dob;
        while(true){
            dob = console.readLine("Year of Birth (YYYY/MM/DD): ");
            System.out.println("------------------------------");
            if(!getValidDob(dob)){
                continue;
            } else {
                envManager.updateEnv("dob", dob);
                break;
            }
        }

        //INPUT GENDER
        String gender;
        gender = console.readLine("GENDER\n1. Male\n2. Female\n3. Others\nPlease Enter a number as option: ");
        switch(Integer.parseInt(gender)){
            case 1:
                envManager.updateEnv("gender", "MLE");
                break;
            case 2:
                envManager.updateEnv("gender", "FLE");
                break;
            case 3:
                envManager.updateEnv("gender", "OTH");
                break;
            default:
                System.out.println("------------------------------");
                System.err.println("ERROR: Please Enter a valid choice!");
                System.out.println("------------------------------");
                gender = console.readLine("GENDER\n1. Male\n2. Female\n3.Others\nPlease Enter a number as option: ");
        }
        System.out.println("------------------------------");

        //INPUT RESIDENCE
        String resident;
        resident = console.readLine("RESIDENCE STATUS\n1. Resident\n2. Non-Resident\nPlease Enter a number as option: ");
        switch(Integer.parseInt(resident)){
            case 1:
                envManager.updateEnv("residenceStat", "FR");
                break;
            case 2:
                envManager.updateEnv("residenceStat", "NFR");
                break;
            default:
                System.out.println("------------------------------");
                System.err.println("ERROR: Please Enter a valid choice!");
                System.out.println("------------------------------");
                gender = console.readLine("RESIDENCE STATUS\n1. Resident\n2. Non-Resident\nPlease Enter a number as option: ");
        }
        System.out.println("------------------------------");

        //INPUT ADDRESS
        String address = console.readLine("Please enter your complete address in a single line by using commas:\n");
        envManager.updateEnv("addressLine", address);
        System.out.println("------------------------------");
    }

    public static Map<Integer, Integer> monthToDays(){
        Map<Integer, Integer> monthDaysMap = new HashMap<>();
        monthDaysMap.put(1, 31);  // January
        monthDaysMap.put(2, 28);  // February
        monthDaysMap.put(3, 31);  // March
        monthDaysMap.put(4, 30);  // April
        monthDaysMap.put(5, 31);  // May
        monthDaysMap.put(6, 30);  // June
        monthDaysMap.put(7, 31);  // July
        monthDaysMap.put(8, 31);  // August
        monthDaysMap.put(9, 30);  // September
        monthDaysMap.put(10, 31); // October
        monthDaysMap.put(11, 30); // November
        monthDaysMap.put(12, 31); // December

        return monthDaysMap;
    }

    public static boolean getValidDob(String dob){
        Map<Integer, Integer> monthDays = monthToDays();
            if(dob.length() == 10){
                String[] parts = dob.split("/");
                if (parts.length != 3){
                    System.out.println("------------------------------");
                    System.err.println("ERROR: Please Enter DOB in the given format (YYYY/MM/DD)!");
                    System.out.println("------------------------------");
                } else if (Integer.parseInt(parts[0]) > 2024){
                    System.out.println("------------------------------");
                    System.err.println("ERROR: Please Enter a valid year!");
                    System.out.println("------------------------------");
                } else if (Integer.parseInt(parts[1]) < 0 || Integer.parseInt(parts[1]) > 12){
                    System.out.println("------------------------------");
                    System.err.println("ERROR: Please Enter a valid Month!");
                    System.out.println("------------------------------");
                } else if (Integer.parseInt(parts[2]) < 0 || Integer.parseInt(parts[2]) > monthDays.get(Integer.parseInt(parts[1]))){
                    System.out.println("------------------------------");
                    System.err.println("ERROR: Please Enter a valid date!");
                    System.out.println("------------------------------");
                } else {
                    return true;
                }
            }
            else {
                System.out.println("------------------------------");
                System.err.println("ERROR: Please Enter DOB in the given format (YYYY/MM/DD)!");
                System.out.println("------------------------------");
            }
        return false;
    }
}
