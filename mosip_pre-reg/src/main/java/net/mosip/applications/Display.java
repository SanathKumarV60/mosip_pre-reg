package net.mosip.applications;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import net.mosip.envManager;

public class Display {
    public static void main(String[] args) throws IOException {
        display_applications();
        //booked_details(envManager.getEnv("applicationId"));
    }

    public static String display_applications() throws IOException {
        new envManager();

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/applications")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        // Create JSON parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseData result = objectMapper.readValue(responseBody, ResponseData.class);

        if (result.getResponse() == null && result.getErrors()[0].getErrorCode().equals("PRG_PAM_APP_005")) {
            System.out.println("MESSAGE: " + result.getErrors()[0].getMessage());
            System.out.println("------------------------------");
            return "";
        } else if (result.getErrors() == null) {
            int i = 0;
            int records = Integer.parseInt(result.getResponse().getTotalRecords());
            System.out.println("No. of Records: " + result.getResponse().getTotalRecords());
            System.out.println("------------------------------");
            while (i < records) {
                System.out.println("Application ID: " + result.getResponse().getBasicDetails()[i].getPreRegistrationId());
                System.out.println("Full Name: " + result.getResponse().getBasicDetails()[i].getDemographicMetadata().getFullName()[0].getValue());
                String x = result.getResponse().getBasicDetails()[i].getStatusCode();
                System.out.println("Status: " + x);
                System.out.println("Postal Code: " + result.getResponse().getBasicDetails()[i].getDemographicMetadata().getPostalCode());
                if (x.equals("Pending_Appointment")) {
                    // do nothing
                } else if (x.equals("Booked")) {
                    System.out.println("Pre Registration Center ID: " + result.getResponse().getBasicDetails()[i].getBookingMetadata().getRegistration_center_id());
                    System.out.println("Appointment Date: " + result.getResponse().getBasicDetails()[i].getBookingMetadata().getAppointment_date());
                    System.out.println("Booking From: " + result.getResponse().getBasicDetails()[i].getBookingMetadata().getTime_slot_from());
                    System.out.println("Booking Till: " + result.getResponse().getBasicDetails()[i].getBookingMetadata().getTime_slot_to());
                } else if (x.equals("Expired")) {
                    System.err.println("The booking has expired, please rebook!");
                } else {
                    System.err.println("ERROR: Invalid Status Code for Application ID: " + result.getResponse().getBasicDetails()[i].getPreRegistrationId());
                }

                if (result.getResponse().getBasicDetails()[i].getDemographicMetadata().getProofOfAddress() == null) {
                    System.out.println("MESSAGE: No POA Given!");
                } else {
                    System.out.println("POA Type: " + result.getResponse().getBasicDetails()[i].getDemographicMetadata().getProofOfAddress().getDocTypCode());
                    System.out.println("POA Document Name: " + result.getResponse().getBasicDetails()[i].getDemographicMetadata().getProofOfAddress().getDocName());
                    System.out.println("POA Document Ref ID: " + result.getResponse().getBasicDetails()[i].getDemographicMetadata().getProofOfAddress().getDocRefId());
                }
                System.out.println("------------------------------");
                i++;
            }
        } else {
            int l = result.getErrors().length;
            for (int j = 0; j < l; j++) {
                System.err.println("ERROR: " + result.getErrors()[j].getErrorCode() + ": " + result.getErrors()[j].getMessage());
            }
            System.out.println("------------------------------");
        }

        return responseBody;
    }

    public static int display_booked() throws IOException {
        new envManager();

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/applications")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        // Create JSON parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseData result = objectMapper.readValue(responseBody, ResponseData.class);

        int count = 0;

        if (result.getResponse() == null && result.getErrors()[0].getErrorCode().equals("PRG_PAM_APP_005")) {
            System.out.println("MESSAGE: " + result.getErrors()[0].getMessage());
            System.out.println("------------------------------");
        } else if (result.getErrors() == null) {
            int i = 0;
            int records = Integer.parseInt(result.getResponse().getTotalRecords());
            while (i < records) {
                String x = result.getResponse().getBasicDetails()[i].getStatusCode();
                if (x.equals("Pending_Appointment")) {
                    // do nothing
                } else if (x.equals("Booked")) {
                    System.out.println("Application ID: " + result.getResponse().getBasicDetails()[i].getPreRegistrationId());
                    System.out.println("Full Name: " + result.getResponse().getBasicDetails()[i].getDemographicMetadata().getFullName()[0].getValue());
                    System.out.println("Status: " + x);
                    System.out.println("Postal Code: " + result.getResponse().getBasicDetails()[i].getDemographicMetadata().getPostalCode());
                    System.out.println("Pre Registration Center ID: " + result.getResponse().getBasicDetails()[i].getBookingMetadata().getRegistration_center_id());
                    System.out.println("Appointment Date: " + result.getResponse().getBasicDetails()[i].getBookingMetadata().getAppointment_date());
                    System.out.println("Booking From: " + result.getResponse().getBasicDetails()[i].getBookingMetadata().getTime_slot_from());
                    System.out.println("Booking Till: " + result.getResponse().getBasicDetails()[i].getBookingMetadata().getTime_slot_to());
                    count++;
                    if (result.getResponse().getBasicDetails()[i].getDemographicMetadata().getProofOfAddress() == null) {
                        System.out.println("MESSAGE: No POA Given!");
                    } else {
                        if (x.equals("Booked")) {
                            System.out.println("POA Type: " + result.getResponse().getBasicDetails()[i].getDemographicMetadata().getProofOfAddress().getDocTypCode());
                            System.out.println("POA Document Name: " + result.getResponse().getBasicDetails()[i].getDemographicMetadata().getProofOfAddress().getDocName());
                            System.out.println("POA Document Ref ID: " + result.getResponse().getBasicDetails()[i].getDemographicMetadata().getProofOfAddress().getDocRefId());
                        }
                    }
                    System.out.println("------------------------------");
                } else {
                    System.err.println("ERROR: Invalid Status Code for Application ID: " + result.getResponse().getBasicDetails()[i].getPreRegistrationId());
                    System.out.println("------------------------------");
                }
                i++;
            }
            System.out.println("No. of Booked Records: " + String.valueOf(count));
            System.out.println("------------------------------");
        } else {
            int l = result.getErrors().length;
            for (int j = 0; j < l; j++) {
                System.err.println("ERROR: " + result.getErrors()[j].getErrorCode() + ": " + result.getErrors()[j].getMessage());
            }
            System.out.println("------------------------------");
        }

        return count;
    }

    public static void booked_details(String applicationId) throws IOException {
        new envManager();

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/applications")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        // Create JSON parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseData result = objectMapper.readValue(responseBody, ResponseData.class);

        // System.out.println(responseBody);

        if (result.getResponse() == null && result.getErrors()[0].getErrorCode().equals("PRG_PAM_APP_005")) {
            System.out.println("MESSAGE: " + result.getErrors()[0].getMessage());
            System.out.println("------------------------------");
        } else if (result.getErrors() == null) {
            int i = 0;
            int records = Integer.parseInt(result.getResponse().getTotalRecords());
            while (i < records) {
                String x = result.getResponse().getBasicDetails()[i].getStatusCode();
                if (result.getResponse().getBasicDetails()[i].getPreRegistrationId().equals(applicationId)) {
                    if (x.equals("Pending_Appointment")) {
                        System.out.println("MESSAGE: The status of the Pre Registration ID is still Pending for Appointment.");
                    } else if (x.equals("Booked")) {
                        System.out.println("Application ID: " + result.getResponse().getBasicDetails()[i].getPreRegistrationId());
                        System.out.println("Full Name: " + result.getResponse().getBasicDetails()[i].getDemographicMetadata().getFullName()[0].getValue());
                        System.out.println("Status: " + x);
                        System.out.println("Postal Code: " + result.getResponse().getBasicDetails()[i].getDemographicMetadata().getPostalCode());
                        System.out.println("Pre Registration Center ID: " + result.getResponse().getBasicDetails()[i].getBookingMetadata().getRegistration_center_id());
                        System.out.println("Appointment Date: " + result.getResponse().getBasicDetails()[i].getBookingMetadata().getAppointment_date());
                        System.out.println("Booking From: " + result.getResponse().getBasicDetails()[i].getBookingMetadata().getTime_slot_from());
                        System.out.println("Booking Till: " + result.getResponse().getBasicDetails()[i].getBookingMetadata().getTime_slot_to());
                        if (result.getResponse().getBasicDetails()[i].getDemographicMetadata().getProofOfAddress() == null) {
                            System.out.println("MESSAGE: No POA Given!");
                        } else {
                            System.out.println("POA Type: " + result.getResponse().getBasicDetails()[i].getDemographicMetadata().getProofOfAddress().getDocTypCode());
                            System.out.println("POA Document Name: " + result.getResponse().getBasicDetails()[i].getDemographicMetadata().getProofOfAddress().getDocName());
                            System.out.println("POA Document Ref ID: " + result.getResponse().getBasicDetails()[i].getDemographicMetadata().getProofOfAddress().getDocRefId());
                        }
                        System.out.println("------------------------------");
                    } else {
                        System.err.println("ERROR: Invalid Status Code for Application ID: " + result.getResponse().getBasicDetails()[i].getPreRegistrationId());
                    }
                    break;
                } else if (result.getResponse().getBasicDetails()[i].getPreRegistrationId().equals(applicationId) && x.equals("Expired")) {
                    System.err.println("The booking has expired, please rebook!");
                } else {
                    i++;
                }
            }
        } else {
            int l = result.getErrors().length;
            for (int j = 0; j < l; j++) {
                System.err.println("ERROR: " + result.getErrors()[j].getErrorCode() + ": " + result.getErrors()[j].getMessage());
            }
            System.out.println("------------------------------");
        }
    }

    public static void updateEnvCancel(String applicationId) throws IOException {
        new envManager();
    
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/applications")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
    
        // Create JSON parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseData result = objectMapper.readValue(responseBody, ResponseData.class);
    
        if (result.getResponse() == null && result.getErrors()[0].getErrorCode().equals("PRG_PAM_APP_005")) {
            System.out.println("MESSAGE: " + result.getErrors()[0].getMessage());
            System.out.println("------------------------------");
        } else if (result.getErrors() == null) {
            int i = 0;
            boolean found = false;
            int records = Integer.parseInt(result.getResponse().getTotalRecords());
            while (i < records) {
                if (result.getResponse().getBasicDetails()[i].getPreRegistrationId().equals(applicationId)) {
                    found = true;
                    envManager.updateEnv("applicationId", applicationId);
                    envManager.updateEnv("regCenterId", result.getResponse().getBasicDetails()[i].getBookingMetadata().getRegistration_center_id());
                    envManager.updateEnv("dateReq", result.getResponse().getBasicDetails()[i].getBookingMetadata().getAppointment_date());
                    envManager.updateEnv("fromTime", result.getResponse().getBasicDetails()[i].getBookingMetadata().getTime_slot_from() + ":00");
                    envManager.updateEnv("toTime", result.getResponse().getBasicDetails()[i].getBookingMetadata().getTime_slot_to() + ":00");
                }
                i++;
            }
    
            if (!found) {
                System.err.println("ERROR: No Such Booked Application ID Exists!");
                System.out.println("------------------------------");
            }
        } else {
            int l = result.getErrors().length;
            for (int j = 0; j < l; j++) {
                System.err.println("ERROR: " + result.getErrors()[j].getErrorCode() + ": " + result.getErrors()[j].getMessage());
            }
            System.out.println("------------------------------");
        }
    }    

    public static void display_no_doc() throws IOException {
        new envManager();
    
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/applications")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
    
        // Create JSON parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseData result = objectMapper.readValue(responseBody, ResponseData.class);
    
        if (result.getResponse() == null && result.getErrors()[0].getErrorCode().equals("PRG_PAM_APP_005")) {
            System.out.println("MESSAGE: " + result.getErrors()[0].getMessage());
            System.out.println("------------------------------");
        } else if (result.getErrors() == null) {
            int i = 0;
            int records = Integer.parseInt(result.getResponse().getTotalRecords());
            while (i < records) {
                if (result.getResponse().getBasicDetails()[i].getDemographicMetadata().getProofOfAddress() == null) {
                    System.out.println("Application ID: " + result.getResponse().getBasicDetails()[i].getPreRegistrationId());
                    System.out.println("Full Name: " + result.getResponse().getBasicDetails()[i].getDemographicMetadata().getFullName()[0].getValue());
                    System.out.println("Postal Code: " + result.getResponse().getBasicDetails()[i].getDemographicMetadata().getPostalCode());
                    System.out.println("MESSAGE: No POA Given!");
                    System.out.println("------------------------------");
                }
                i++;
            }
        } else {
            int l = result.getErrors().length;
            for (int j = 0; j < l; j++) {
                System.err.println("ERROR: " + result.getErrors()[j].getErrorCode() + ": " + result.getErrors()[j].getMessage());
            }
            System.out.println("------------------------------");
        }
    }
    
    public static void display_not_booked() throws IOException {
        new envManager();
    
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/applications")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
    
        // Create JSON parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseData result = objectMapper.readValue(responseBody, ResponseData.class);
    
        if (result.getResponse() == null && result.getErrors()[0].getErrorCode().equals("PRG_PAM_APP_005")) {
            System.out.println("MESSAGE: " + result.getErrors()[0].getMessage());
            System.out.println("------------------------------");
        } else if (result.getErrors() == null) {
            int i = 0;
            int records = Integer.parseInt(result.getResponse().getTotalRecords());
            while (i < records) {
                String x = result.getResponse().getBasicDetails()[i].getStatusCode();
                if (!x.equals("Booked")) {
                    System.out.println("Application ID: " + result.getResponse().getBasicDetails()[i].getPreRegistrationId());
                    System.out.println("Full Name: " + result.getResponse().getBasicDetails()[i].getDemographicMetadata().getFullName()[0].getValue());
                    System.out.println("Status: " + x);
                    System.out.println("Postal Code: " + result.getResponse().getBasicDetails()[i].getDemographicMetadata().getPostalCode());
                    System.out.println("------------------------------");
                }
                i++;
            }
        } else {
            int l = result.getErrors().length;
            for (int j = 0; j < l; j++) {
                System.err.println("ERROR: " + result.getErrors()[j].getErrorCode() + ": " + result.getErrors()[j].getMessage());
            }
            System.out.println("------------------------------");
        }
    }    
}


class ResponseData {
    private String id;
    private String version;
    private String responsetime;
    private ResponseDetails response;
    private Errors[] errors;

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public String getResponsetime() {
        return responsetime;
    }

    public ResponseDetails getResponse() {
        return response;
    }

    public Errors[] getErrors() {
        return errors;
    }
}

class ResponseDetails {
    private BasicDetails[] basicDetails;
    private String totalRecords;
    private String noOfRecords;
    private String pageIndex;

    public BasicDetails[] getBasicDetails() {
        return basicDetails;
    }

    public String getTotalRecords() {
        return totalRecords;
    }

    public String getNoOfRecords() {
        return noOfRecords;
    }

    public String getPageIndex() {
        return pageIndex;
    }
}

class BasicDetails {
    private String preRegistrationId;
    private String statusCode;
    private BookingMetadata bookingMetadata;
    private DemographicMetadata demographicMetadata;

    public String getPreRegistrationId() {
        return preRegistrationId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public BookingMetadata getBookingMetadata() {
        return bookingMetadata;
    }

    public DemographicMetadata getDemographicMetadata() {
        return demographicMetadata;
    }
}

class BookingMetadata {
    private String registration_center_id;
    private String appointment_date;
    private String time_slot_from;
    private String time_slot_to;

    public String getRegistration_center_id() {
        return registration_center_id;
    }

    public String getAppointment_date() {
        return appointment_date;
    }

    public String getTime_slot_from() {
        return time_slot_from;
    }

    public String getTime_slot_to() {
        return time_slot_to;
    }
}

class DemographicMetadata {
    private ProofOfAddress proofOfAddress;
    private String postalCode;
    private FullName[] fullName;

    public ProofOfAddress getProofOfAddress() {
        return proofOfAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public FullName[] getFullName() {
        return fullName;
    }
}

class ProofOfAddress {
    private String docCatCode;
    private String docTypCode;
    private String docName;
    private String langCode;
    private String documentId;
    private String docRefId;

    public String getDocCatCode() {
        return docCatCode;
    }

    public String getDocTypCode() {
        return docTypCode;
    }

    public String getDocName() {
        return docName;
    }

    public String getLangCode() {
        return langCode;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getDocRefId() {
        return docRefId;
    }
}

class FullName {
    private String language;
    private String value;

    public String getLanguage() {
        return language;
    }

    public String getValue() {
        return value;
    }
}

class Errors {
    private String errorCode;
    private String message;

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
