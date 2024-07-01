package net.mosip.applications;

import java.io.IOException;

import net.mosip.envManager;
import net.mosip.models.applications.*;

public class Display {
    public static void main(String[] args) throws IOException {
        //display_applications();
        //booked_details(envManager.getEnv("applicationId"));
        display_no_doc();
    }

    public static String display_applications() throws IOException {
        try {
            ResponseDetails resp = DisplayCall.display_applications(envManager.getEnv("auth"));

            int i = 0;
            int records = Integer.parseInt(resp.totalRecords);
            System.out.println("No. of Records: " + resp.totalRecords);
            System.out.println("------------------------------");
            while (i < records) {
                System.out.println("Application ID: " + resp.basicDetails[i].preRegistrationId);
                System.out.println("Full Name: " + resp.basicDetails[i].demographicMetadata.fullName[0].value);
                String x = resp.basicDetails[i].statusCode;
                System.out.println("Status: " + x);
                System.out.println("Postal Code: " + resp.basicDetails[i].demographicMetadata.postalCode);
                if (x.equals("Pending_Appointment")) {
                    // do nothing
                } else if (x.equals("Booked")) {
                    System.out.println("Pre Registration Center ID: " + resp.basicDetails[i].bookingMetadata.registration_center_id);
                    System.out.println("Appointment Date: " + resp.basicDetails[i].bookingMetadata.appointment_date);
                    System.out.println("Booking From: " + resp.basicDetails[i].bookingMetadata.time_slot_from);
                    System.out.println("Booking Till: " + resp.basicDetails[i].bookingMetadata.time_slot_to);
                } else if (x.equals("Expired")) {
                    System.err.println("The booking has expired, please rebook!");
                } else {
                    System.err.println("ERROR: Invalid Status Code for Application ID: " + resp.basicDetails[i].preRegistrationId);
                }

                if (resp.basicDetails[i].demographicMetadata.proofOfAddress == null) {
                    System.out.println("MESSAGE: No POA Given!");
                } else {
                    System.out.println("POA Type: " + resp.basicDetails[i].demographicMetadata.proofOfAddress.docTypCode);
                    System.out.println("POA Document Name: " + resp.basicDetails[i].demographicMetadata.proofOfAddress.docName);
                    System.out.println("POA Document Ref ID: " + resp.basicDetails[i].demographicMetadata.proofOfAddress.docRefId);
                }
                System.out.println("------------------------------");
                i++;
            }

            return null;
        } catch (ApplicationException ex){
            System.out.println(ex.getMessage());
            System.out.println("------------------------------");
            return "";
        }
    }

    public static int display_booked() throws IOException {
        try {
            ResponseDetails resp = DisplayCall.display_applications(envManager.getEnv("auth"));
            
            int count = 0;
            int i = 0;
            int records = Integer.parseInt(resp.totalRecords);
            while (i < records) {
                String x = resp.basicDetails[i].statusCode;
                if (x.equals("Pending_Appointment") || x.equals("Expired")) {
                    // do nothing
                } else if (x.equals("Booked")) {
                    System.out.println("Application ID: " + resp.basicDetails[i].preRegistrationId);
                    System.out.println("Full Name: " + resp.basicDetails[i].demographicMetadata.fullName[0].value);
                    System.out.println("Status: " + x);
                    System.out.println("Postal Code: " + resp.basicDetails[i].demographicMetadata.postalCode);
                    System.out.println("Pre Registration Center ID: " + resp.basicDetails[i].bookingMetadata.registration_center_id);
                    System.out.println("Appointment Date: " + resp.basicDetails[i].bookingMetadata.appointment_date);
                    System.out.println("Booking From: " + resp.basicDetails[i].bookingMetadata.time_slot_from);
                    System.out.println("Booking Till: " + resp.basicDetails[i].bookingMetadata.time_slot_to);
                    count++;
                    if (resp.basicDetails[i].demographicMetadata.proofOfAddress == null) {
                        System.out.println("MESSAGE: No POA Given!");
                    } else {
                        if (x.equals("Booked")) {
                            System.out.println("POA Type: " + resp.basicDetails[i].demographicMetadata.proofOfAddress.docTypCode);
                            System.out.println("POA Document Name: " + resp.basicDetails[i].demographicMetadata.proofOfAddress.docName);
                            System.out.println("POA Document Ref ID: " + resp.basicDetails[i].demographicMetadata.proofOfAddress.docRefId);
                        }
                    }
                    System.out.println("------------------------------");
                } else {
                    System.err.println("ERROR: Invalid Status Code for Application ID: " + resp.basicDetails[i].preRegistrationId);
                    System.out.println("------------------------------");
                }
                i++;
            }
            System.out.println("No. of Booked Records: " + String.valueOf(count));
            System.out.println("------------------------------");
            return count;
        } catch (ApplicationException ex) {
            System.out.println(ex.getMessage());
            System.out.println("------------------------------");
            return 0;
        }
    }

    public static void booked_details(String applicationId) throws IOException {
        try {
            ResponseDetails resp = DisplayCall.display_applications(envManager.getEnv("auth"));

            int i = 0;
            int records = Integer.parseInt(resp.totalRecords);
            while (i < records) {
                String x = resp.basicDetails[i].statusCode;
                if (resp.basicDetails[i].preRegistrationId.equals(applicationId)) {
                    if (x.equals("Pending_Appointment")) {
                        System.out.println("MESSAGE: The status of the Pre Registration ID is still Pending for Appointment.");
                    } else if (x.equals("Expired")) {
                        System.out.println("MESSAGE: The status of the Pre Registration ID is Expired. Please re-book.");
                    } else if (x.equals("Booked")) {
                        System.out.println("Application ID: " + resp.basicDetails[i].preRegistrationId);
                        System.out.println("Full Name: " + resp.basicDetails[i].demographicMetadata.fullName[0].value);
                        System.out.println("Status: " + x);
                        System.out.println("Postal Code: " + resp.basicDetails[i].demographicMetadata.postalCode);
                        System.out.println("Pre Registration Center ID: " + resp.basicDetails[i].bookingMetadata.registration_center_id);
                        envManager.updateEnv("regCenterId", resp.basicDetails[i].bookingMetadata.registration_center_id);
                        System.out.println("Appointment Date: " + resp.basicDetails[i].bookingMetadata.appointment_date);
                        System.out.println("Booking From: " + resp.basicDetails[i].bookingMetadata.time_slot_from);
                        System.out.println("Booking Till: " + resp.basicDetails[i].bookingMetadata.time_slot_to);
                        if (resp.basicDetails[i].demographicMetadata.proofOfAddress == null) {
                            System.out.println("MESSAGE: No POA Given!");
                        } else {
                            System.out.println("POA Type: " + resp.basicDetails[i].demographicMetadata.proofOfAddress.docTypCode);
                            System.out.println("POA Document Name: " + resp.basicDetails[i].demographicMetadata.proofOfAddress.docName);
                            System.out.println("POA Document Ref ID: " + resp.basicDetails[i].demographicMetadata.proofOfAddress.docRefId);
                        }
                        System.out.println("------------------------------");
                    } else {
                        System.err.println("ERROR: Invalid Status Code for Application ID: " + resp.basicDetails[i].preRegistrationId);
                    }
                    break;
                } else if (resp.basicDetails[i].preRegistrationId.equals(applicationId) && x.equals("Expired")) {
                    System.err.println("The booking has expired, please rebook!");
                } else {
                    i++;
                }
            }
        } catch (ApplicationException ex) {
            System.out.println(ex.getMessage());
            System.out.println("------------------------------");
        }
    }

    public static void updateEnvCancel(String applicationId) throws IOException {
        try{
            ResponseDetails resp = DisplayCall.display_applications(envManager.getEnv("auth"));

            int i = 0;
            int records = Integer.parseInt(resp.totalRecords);
            while (i < records) {
                if (resp.basicDetails[i].preRegistrationId.equals(applicationId)) {
                    envManager.updateEnv("applicationId", applicationId);
                    envManager.updateEnv("regCenterId", resp.basicDetails[i].bookingMetadata.registration_center_id);
                    envManager.updateEnv("dateReq", resp.basicDetails[i].bookingMetadata.appointment_date);
                    envManager.updateEnv("fromTime", resp.basicDetails[i].bookingMetadata.time_slot_from + ":00");
                    envManager.updateEnv("toTime", resp.basicDetails[i].bookingMetadata.time_slot_to + ":00");
                }
                i++;
            }
        } catch (ApplicationException ex) {
            System.err.println(ex.getMessage());
            System.out.println("------------------------------");
        }
    }    

    public static void display_no_doc() throws IOException {
        try {
            ResponseDetails resp = DisplayCall.display_applications(envManager.getEnv("auth"));

            int count = 0;
            int i = 0;
            int records = Integer.parseInt(resp.totalRecords);
            while (i < records) {
                if (resp.basicDetails[i].demographicMetadata.proofOfAddress == null) {
                    System.out.println("Application ID: " + resp.basicDetails[i].preRegistrationId);
                    System.out.println("Full Name: " + resp.basicDetails[i].demographicMetadata.fullName[0].value);
                    System.out.println("Postal Code: " + resp.basicDetails[i].demographicMetadata.postalCode);
                    System.out.println("MESSAGE: No POA Given!");
                    System.out.println("------------------------------");
                    count++;
                }
                i++;
            }
            System.out.println("No. of records with no documents: " + String.valueOf(count));
            System.out.println("------------------------------");
        } catch (ApplicationException ex) {
            System.out.println(ex.getMessage());
            System.out.println("------------------------------");
        }
    }
    
    public static void display_not_booked() throws IOException {
        try {
            ResponseDetails resp = DisplayCall.display_applications(envManager.getEnv("auth"));

            int count = 0;
            int i = 0;
            int records = Integer.parseInt(resp.totalRecords);
            while (i < records) {
                String x = resp.basicDetails[i].statusCode;
                if (!x.equals("Booked")) {
                    System.out.println("Application ID: " + resp.basicDetails[i].preRegistrationId);
                    System.out.println("Full Name: " + resp.basicDetails[i].demographicMetadata.fullName[0].value);
                    System.out.println("Status: " + x);
                    System.out.println("Postal Code: " + resp.basicDetails[i].demographicMetadata.postalCode);
                    System.out.println("------------------------------");
                    count++;
                }
                i++;
            }
            System.out.println("No. of not booked records: " + String.valueOf(count));
            System.out.println("------------------------------");
        } catch (ApplicationException ex) {
            System.out.println(ex.getMessage());
            System.out.println("------------------------------");
        }
    }    
}