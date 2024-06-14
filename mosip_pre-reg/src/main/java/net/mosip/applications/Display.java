package net.mosip.applications;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.*;
import net.mosip.envManager;

public class Display {
    public static void main(String[] args) throws IOException{
        //@SuppressWarnings("unused")
        //String response = display_applications();
        booked_details(envManager.getEnv("applicationId"));;
    }

    public static String display_applications() throws IOException{
        new envManager();

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/applications")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //System.out.println(responseBody);

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseData result = objectMapper.readValue(responseBody, ResponseData.class);
        
        if (result.response == null && result.errors[0].errorCode.equals("PRG_PAM_APP_005")){
            System.out.println("MESSAGE: " + result.errors[0].message);
            System.out.println("------------------------------");
            return "";
        } else if (result.errors == null) {
            int i = 0;
            int records = Integer.parseInt(result.response.totalRecords);
            System.out.println("No. of Records: " + result.response.totalRecords);
            System.out.println("------------------------------");
            while(i < records){
                System.out.println("Application ID: " + result.response.basicDetails[i].preRegistrationId);
                System.out.println("Full Name: " + result.response.basicDetails[i].demographicMetadata.fullName[0].value);
                String x = result.response.basicDetails[i].statusCode;
                System.out.println("Status: " + x);
                System.out.println("Postal Code: " + result.response.basicDetails[i].demographicMetadata.postalCode);
                if(x.equals("Pending_Appointment")){
                    //do nothing
                } else if (x.equals("Booked")){
                    System.out.println("Pre Registration Center ID: " + result.response.basicDetails[i].bookingMetadata.registration_center_id);
                    System.out.println("Appointment Date: " + result.response.basicDetails[i].bookingMetadata.appointment_date);
                    System.out.println("Booking From: " + result.response.basicDetails[i].bookingMetadata.time_slot_from);
                    System.out.println("Booking Till: " + result.response.basicDetails[i].bookingMetadata.time_slot_to);
                } else if (x.equals("Expired")){
                        System.err.println("The booking has expired, please rebook!");
                } else {
                    System.err.println("ERROR: Invalid Status Code for Application ID: "+ result.response.basicDetails[i].preRegistrationId);
                }

                if(result.response.basicDetails[i].demographicMetadata.proofOfAddress == null){
                    System.out.println("MESSAGE: No POA Given!");
                } else {
                    System.out.println("POA Type: " + result.response.basicDetails[i].demographicMetadata.proofOfAddress.docTypCode);
                    System.out.println("POA Document Name: " + result.response.basicDetails[i].demographicMetadata.proofOfAddress.docName);
                    System.out.println("POA Document Ref ID: " + result.response.basicDetails[i].demographicMetadata.proofOfAddress.docRefId);
                }
                System.out.println("------------------------------");
                i++;
            }
        } else {
            int l = result.errors.length;
            for(int j = 0; j < l; j++){
                System.err.println("ERROR: " + result.errors[j].errorCode + ": " + result.errors[j].message);
            }
            System.out.println("------------------------------");
        }

        return responseBody;
    }

    public static int display_booked() throws IOException{
        new envManager();

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/applications")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseData result = objectMapper.readValue(responseBody, ResponseData.class);
        
        int count = 0;

        if (result.response == null && result.errors[0].errorCode.equals("PRG_PAM_APP_005")){
            System.out.println("MESSAGE: " + result.errors[0].message);
            System.out.println("------------------------------");
        } else if (result.errors == null) {
            int i = 0;
            int records = Integer.parseInt(result.response.totalRecords);
            while(i < records){
                String x = result.response.basicDetails[i].statusCode;
                if(x.equals("Pending_Appointment")){
                    //do nothing
                } else if (x.equals("Booked")){
                    System.out.println("Application ID: " + result.response.basicDetails[i].preRegistrationId);
                    System.out.println("Full Name: " + result.response.basicDetails[i].demographicMetadata.fullName[0].value);
                    System.out.println("Status: " + x);
                    System.out.println("Postal Code: " + result.response.basicDetails[i].demographicMetadata.postalCode);
                    System.out.println("Pre Registration Center ID: " + result.response.basicDetails[i].bookingMetadata.registration_center_id);
                    System.out.println("Appointment Date: " + result.response.basicDetails[i].bookingMetadata.appointment_date);
                    System.out.println("Booking From: " + result.response.basicDetails[i].bookingMetadata.time_slot_from);
                    System.out.println("Booking Till: " + result.response.basicDetails[i].bookingMetadata.time_slot_to);
                    count++;
                    if(result.response.basicDetails[i].demographicMetadata.proofOfAddress == null){
                        System.out.println("MESSAGE: No POA Given!");
                    } else {
                        if(x.equals("Booked")){
                            System.out.println("POA Type: " + result.response.basicDetails[i].demographicMetadata.proofOfAddress.docTypCode);
                            System.out.println("POA Document Name: " + result.response.basicDetails[i].demographicMetadata.proofOfAddress.docName);
                            System.out.println("POA Document Ref ID: " + result.response.basicDetails[i].demographicMetadata.proofOfAddress.docRefId);
                        }
                    }
                    System.out.println("------------------------------");
                } else {
                    System.err.println("ERROR: Invalid Status Code for Application ID: "+ result.response.basicDetails[i].preRegistrationId);
                    System.out.println("------------------------------");
                }
                i++;
            }
            System.out.println("No. of Booked Records: " + String.valueOf(count));
            System.out.println("------------------------------");
        } else {
            int l = result.errors.length;
            for(int j = 0; j < l; j++){
                System.err.println("ERROR: " + result.errors[j].errorCode + ": " + result.errors[j].message);
            }
            System.out.println("------------------------------");
        }

        return count;
    }

    public static void booked_details(String applicationId) throws IOException {
        new envManager();

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/applications")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseData result = objectMapper.readValue(responseBody, ResponseData.class);

        //System.out.println(responseBody);

        if (result.response == null && result.errors[0].errorCode.equals("PRG_PAM_APP_005")){
            System.out.println("MESSAGE: " + result.errors[0].message);
            System.out.println("------------------------------");
        } else if (result.errors == null) {
            int i = 0;
            int records = Integer.parseInt(result.response.totalRecords);
            while(i < records){
                String x = result.response.basicDetails[i].statusCode;
                if(x.equals("Pending_Appointment")){
                    //do nothing
                } else if (x.equals("Booked") && result.response.basicDetails[i].preRegistrationId.equals(applicationId)){
                    System.out.println("Application ID: " + result.response.basicDetails[i].preRegistrationId);
                    System.out.println("Full Name: " + result.response.basicDetails[i].demographicMetadata.fullName[0].value);
                    System.out.println("Status: " + x);
                    System.out.println("Postal Code: " + result.response.basicDetails[i].demographicMetadata.postalCode);
                    System.out.println("Pre Registration Center ID: " + result.response.basicDetails[i].bookingMetadata.registration_center_id);
                    envManager.updateEnv("regCenterId", result.response.basicDetails[i].bookingMetadata.registration_center_id);
                    System.out.println("Appointment Date: " + result.response.basicDetails[i].bookingMetadata.appointment_date);
                    System.out.println("Booking From: " + result.response.basicDetails[i].bookingMetadata.time_slot_from);
                    System.out.println("Booking Till: " + result.response.basicDetails[i].bookingMetadata.time_slot_to);
                    System.out.println("------------------------------");
                } else {
                    System.err.println("ERROR: Invalid Status Code for Application ID: "+ result.response.basicDetails[i].preRegistrationId);
                    System.out.println("------------------------------");
                }
                i++;
            }
        } else {
            int l = result.errors.length;
            for(int j = 0; j < l; j++){
                System.err.println("ERROR: " + result.errors[j].errorCode + ": " + result.errors[j].message);
            }
            System.out.println("------------------------------");
        }
    }

    public static void updateEnvCancel(String applicationId) throws IOException{
        new envManager();

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/applications")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseData result = objectMapper.readValue(responseBody, ResponseData.class);
        
        if (result.response == null && result.errors[0].errorCode.equals("PRG_PAM_APP_005")){
            System.out.println("MESSAGE: " + result.errors[0].message);
            System.out.println("------------------------------");
        } else if (result.errors == null) {
            int i = 0;
            boolean found = false;
            int records = Integer.parseInt(result.response.totalRecords);
            while(i < records){
                if (result.response.basicDetails[i].preRegistrationId.equals(applicationId)){
                    found = true;
                    envManager.updateEnv("applicationId", applicationId);
                    envManager.updateEnv("regCenterId", result.response.basicDetails[i].bookingMetadata.registration_center_id);
                    envManager.updateEnv("dateReq", result.response.basicDetails[i].bookingMetadata.appointment_date);
                    envManager.updateEnv("fromTime", result.response.basicDetails[i].bookingMetadata.time_slot_from + ":00");
                    envManager.updateEnv("toTime", result.response.basicDetails[i].bookingMetadata.time_slot_to + ":00");
                }
                i++;                
            }

            if(!found){
                System.err.println("ERROR: No Such Booked Application ID Exists!");
                System.out.println("------------------------------");
            }
        } else {
            int l = result.errors.length;
            for(int j = 0; j < l; j++){
                System.err.println("ERROR: " + result.errors[j].errorCode + ": " + result.errors[j].message);
            }
            System.out.println("------------------------------");
        } 
    }

    public static void display_no_doc() throws IOException{
        new envManager();

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/applications")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseData result = objectMapper.readValue(responseBody, ResponseData.class);

        if (result.response == null && result.errors[0].errorCode.equals("PRG_PAM_APP_005")){
            System.out.println("MESSAGE: " + result.errors[0].message);
            System.out.println("------------------------------");
        } else if (result.errors == null) {
            int i = 1;
            int count = 0;
            int l = result.response.basicDetails.length;
            while(i <= l){
                int j = i - 1;
                if(result.response.basicDetails[j].demographicMetadata.proofOfAddress == null){
                    System.out.println("Application ID: " + result.response.basicDetails[j].preRegistrationId);
                    System.out.println("Full Name: " + result.response.basicDetails[j].demographicMetadata.fullName[0].value);
                    System.out.println("Status: " + result.response.basicDetails[j].statusCode);
                    System.out.println("Postal Code: " + result.response.basicDetails[j].demographicMetadata.postalCode);
                    System.out.println("------------------------------");
                    count++;
                }
                i++;
            }
            System.out.println("No. of records found for no documents: " + String.valueOf(count));
            System.out.println("------------------------------");
        } else {
            int l = result.errors.length;
            for(int j = 0; j < l; j++){
                System.err.println("ERROR: " + result.errors[j].errorCode + ": " + result.errors[j].message);
            }
            System.out.println("------------------------------");
        }
    }

    public static void display_not_booked() throws IOException{
        new envManager();

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/applications")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseData result = objectMapper.readValue(responseBody, ResponseData.class);

        if (result.response == null && result.errors[0].errorCode.equals("PRG_PAM_APP_005")){
            System.out.println("MESSAGE: " + result.errors[0].message);
            System.out.println("------------------------------");
        } else if (result.errors == null) {
            int j = 0;
            int count = 0;
            int l = result.response.basicDetails.length;
            while(j < l){
                if(result.response.basicDetails[j].statusCode.equals("Pending_Appointment") || result.response.basicDetails[j].statusCode.equals("Expired")){
                    System.out.println("Application ID: " + result.response.basicDetails[j].preRegistrationId);
                    System.out.println("Full Name: " + result.response.basicDetails[j].demographicMetadata.fullName[0].value);
                    System.out.println("Status: " + result.response.basicDetails[j].statusCode);
                    System.out.println("Postal Code: " + result.response.basicDetails[j].demographicMetadata.postalCode);
                    System.out.println("------------------------------");
                    count++;
                }
                j++;
            }
            System.out.println("No. of not booked applications: " + String.valueOf(count));
            System.out.println("------------------------------");
        } else {
            int l = result.errors.length;
            for(int j = 0; j < l; j++){
                System.err.println("ERROR: " + result.errors[j].errorCode + ": " + result.errors[j].message);
            }
            System.out.println("------------------------------");
        }
    }
}

class ResponseData {
    public String id;
    public String version;
    public String responsetime;
    public ResponseDetails response;
    public Errors[] errors;
}

class ResponseDetails {
    public BasicDetails[] basicDetails;
    public String totalRecords;
    public String noOfRecords;
    public String pageIndex;
}

class BasicDetails {
    public String preRegistrationId;
    public String statusCode;
    public BookingMetadata bookingMetadata;
    public DemographicMetadata demographicMetadata;
}

class BookingMetadata{
    public String registration_center_id;
    public String appointment_date;
    public String time_slot_from;
    public String time_slot_to;
}

class DemographicMetadata{
    public ProofOfAddress proofOfAddress;
    public String postalCode;
    public FullName[] fullName;
}

class ProofOfAddress{
    public String docCatCode;
    public String docTypCode;
    public String docName;
    public String langCode;
    public String documentId;
    public String docRefId;      
}

class FullName{
    public String language;
    public String value;
}

class Errors {
    public String errorCode;
    public String message;
}