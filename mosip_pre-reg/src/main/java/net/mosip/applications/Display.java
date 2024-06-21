package net.mosip.applications;

import java.io.IOException;

import net.mosip.envManager;

public class Display {
    public static void main(String[] args) throws IOException {
        //display_applications();
        //booked_details(envManager.getEnv("applicationId"));
        display_no_doc();
    }

    public static String display_applications() throws IOException {
        try {
            ResponseDetails resp = DisplayCalls.display_applications(envManager.getEnv("auth"));

            int i = 0;
            int records = Integer.parseInt(resp.getTotalRecords());
            System.out.println("No. of Records: " + resp.getTotalRecords());
            System.out.println("------------------------------");
            while (i < records) {
                System.out.println("Application ID: " + resp.getBasicDetails()[i].getPreRegistrationId());
                System.out.println("Full Name: " + resp.getBasicDetails()[i].getDemographicMetadata().getFullName()[0].getValue());
                String x = resp.getBasicDetails()[i].getStatusCode();
                System.out.println("Status: " + x);
                System.out.println("Postal Code: " + resp.getBasicDetails()[i].getDemographicMetadata().getPostalCode());
                if (x.equals("Pending_Appointment")) {
                    // do nothing
                } else if (x.equals("Booked")) {
                    System.out.println("Pre Registration Center ID: " + resp.getBasicDetails()[i].getBookingMetadata().getRegistration_center_id());
                    System.out.println("Appointment Date: " + resp.getBasicDetails()[i].getBookingMetadata().getAppointment_date());
                    System.out.println("Booking From: " + resp.getBasicDetails()[i].getBookingMetadata().getTime_slot_from());
                    System.out.println("Booking Till: " + resp.getBasicDetails()[i].getBookingMetadata().getTime_slot_to());
                } else if (x.equals("Expired")) {
                    System.err.println("The booking has expired, please rebook!");
                } else {
                    System.err.println("ERROR: Invalid Status Code for Application ID: " + resp.getBasicDetails()[i].getPreRegistrationId());
                }

                if (resp.getBasicDetails()[i].getDemographicMetadata().getProofOfAddress() == null) {
                    System.out.println("MESSAGE: No POA Given!");
                } else {
                    System.out.println("POA Type: " + resp.getBasicDetails()[i].getDemographicMetadata().getProofOfAddress().getDocTypCode());
                    System.out.println("POA Document Name: " + resp.getBasicDetails()[i].getDemographicMetadata().getProofOfAddress().getDocName());
                    System.out.println("POA Document Ref ID: " + resp.getBasicDetails()[i].getDemographicMetadata().getProofOfAddress().getDocRefId());
                }
                System.out.println("------------------------------");
                i++;
            }

            return null;
        } catch (Error ex){
            System.out.println(ex.getMessage());
            System.out.println("------------------------------");
            return "";
        }
    }

    public static int display_booked() throws IOException {
        try {
            ResponseDetails resp = DisplayCalls.display_applications(envManager.getEnv("auth"));
            
            int count = 0;
            int i = 0;
            int records = Integer.parseInt(resp.getTotalRecords());
            while (i < records) {
                String x = resp.getBasicDetails()[i].getStatusCode();
                if (x.equals("Pending_Appointment") || x.equals("Expired")) {
                    // do nothing
                } else if (x.equals("Booked")) {
                    System.out.println("Application ID: " + resp.getBasicDetails()[i].getPreRegistrationId());
                    System.out.println("Full Name: " + resp.getBasicDetails()[i].getDemographicMetadata().getFullName()[0].getValue());
                    System.out.println("Status: " + x);
                    System.out.println("Postal Code: " + resp.getBasicDetails()[i].getDemographicMetadata().getPostalCode());
                    System.out.println("Pre Registration Center ID: " + resp.getBasicDetails()[i].getBookingMetadata().getRegistration_center_id());
                    System.out.println("Appointment Date: " + resp.getBasicDetails()[i].getBookingMetadata().getAppointment_date());
                    System.out.println("Booking From: " + resp.getBasicDetails()[i].getBookingMetadata().getTime_slot_from());
                    System.out.println("Booking Till: " + resp.getBasicDetails()[i].getBookingMetadata().getTime_slot_to());
                    count++;
                    if (resp.getBasicDetails()[i].getDemographicMetadata().getProofOfAddress() == null) {
                        System.out.println("MESSAGE: No POA Given!");
                    } else {
                        if (x.equals("Booked")) {
                            System.out.println("POA Type: " + resp.getBasicDetails()[i].getDemographicMetadata().getProofOfAddress().getDocTypCode());
                            System.out.println("POA Document Name: " + resp.getBasicDetails()[i].getDemographicMetadata().getProofOfAddress().getDocName());
                            System.out.println("POA Document Ref ID: " + resp.getBasicDetails()[i].getDemographicMetadata().getProofOfAddress().getDocRefId());
                        }
                    }
                    System.out.println("------------------------------");
                } else {
                    System.err.println("ERROR: Invalid Status Code for Application ID: " + resp.getBasicDetails()[i].getPreRegistrationId());
                    System.out.println("------------------------------");
                }
                i++;
            }
            System.out.println("No. of Booked Records: " + String.valueOf(count));
            System.out.println("------------------------------");
            return count;
        } catch (Error ex) {
            System.out.println(ex.getMessage());
            System.out.println("------------------------------");
            return 0;
        }
    }

    public static void booked_details(String applicationId) throws IOException {
        try {
            ResponseDetails resp = DisplayCalls.display_applications(envManager.getEnv("auth"));

            int i = 0;
            int records = Integer.parseInt(resp.getTotalRecords());
            while (i < records) {
                String x = resp.getBasicDetails()[i].getStatusCode();
                if (resp.getBasicDetails()[i].getPreRegistrationId().equals(applicationId)) {
                    if (x.equals("Pending_Appointment")) {
                        System.out.println("MESSAGE: The status of the Pre Registration ID is still Pending for Appointment.");
                    } else if (x.equals("Expired")) {
                        System.out.println("MESSAGE: The status of the Pre Registration ID is Expired. Please re-book.");
                    } else if (x.equals("Booked")) {
                        System.out.println("Application ID: " + resp.getBasicDetails()[i].getPreRegistrationId());
                        System.out.println("Full Name: " + resp.getBasicDetails()[i].getDemographicMetadata().getFullName()[0].getValue());
                        System.out.println("Status: " + x);
                        System.out.println("Postal Code: " + resp.getBasicDetails()[i].getDemographicMetadata().getPostalCode());
                        System.out.println("Pre Registration Center ID: " + resp.getBasicDetails()[i].getBookingMetadata().getRegistration_center_id());
                        System.out.println("Appointment Date: " + resp.getBasicDetails()[i].getBookingMetadata().getAppointment_date());
                        System.out.println("Booking From: " + resp.getBasicDetails()[i].getBookingMetadata().getTime_slot_from());
                        System.out.println("Booking Till: " + resp.getBasicDetails()[i].getBookingMetadata().getTime_slot_to());
                        if (resp.getBasicDetails()[i].getDemographicMetadata().getProofOfAddress() == null) {
                            System.out.println("MESSAGE: No POA Given!");
                        } else {
                            System.out.println("POA Type: " + resp.getBasicDetails()[i].getDemographicMetadata().getProofOfAddress().getDocTypCode());
                            System.out.println("POA Document Name: " + resp.getBasicDetails()[i].getDemographicMetadata().getProofOfAddress().getDocName());
                            System.out.println("POA Document Ref ID: " + resp.getBasicDetails()[i].getDemographicMetadata().getProofOfAddress().getDocRefId());
                        }
                        System.out.println("------------------------------");
                    } else {
                        System.err.println("ERROR: Invalid Status Code for Application ID: " + resp.getBasicDetails()[i].getPreRegistrationId());
                    }
                    break;
                } else if (resp.getBasicDetails()[i].getPreRegistrationId().equals(applicationId) && x.equals("Expired")) {
                    System.err.println("The booking has expired, please rebook!");
                } else {
                    i++;
                }
            }
        } catch (Error ex) {
            System.out.println(ex.getMessage());
            System.out.println("------------------------------");
        }
    }

    public static void updateEnvCancel(String applicationId) throws IOException {
        try{
            ResponseDetails resp = DisplayCalls.display_applications(envManager.getEnv("auth"));

            int i = 0;
            int records = Integer.parseInt(resp.getTotalRecords());
            while (i < records) {
                if (resp.getBasicDetails()[i].getPreRegistrationId().equals(applicationId)) {
                    envManager.updateEnv("applicationId", applicationId);
                    envManager.updateEnv("regCenterId", resp.getBasicDetails()[i].getBookingMetadata().getRegistration_center_id());
                    envManager.updateEnv("dateReq", resp.getBasicDetails()[i].getBookingMetadata().getAppointment_date());
                    envManager.updateEnv("fromTime", resp.getBasicDetails()[i].getBookingMetadata().getTime_slot_from() + ":00");
                    envManager.updateEnv("toTime", resp.getBasicDetails()[i].getBookingMetadata().getTime_slot_to() + ":00");
                }
                i++;
            }
        } catch (Error ex) {
            System.out.println(ex.getMessage());
            System.out.println("------------------------------");
        }
    }    

    public static void display_no_doc() throws IOException {
        try {
            ResponseDetails resp = DisplayCalls.display_applications(envManager.getEnv("auth"));

            int count = 0;
            int i = 0;
            int records = Integer.parseInt(resp.getTotalRecords());
            while (i < records) {
                if (resp.getBasicDetails()[i].getDemographicMetadata().getProofOfAddress() == null) {
                    System.out.println("Application ID: " + resp.getBasicDetails()[i].getPreRegistrationId());
                    System.out.println("Full Name: " + resp.getBasicDetails()[i].getDemographicMetadata().getFullName()[0].getValue());
                    System.out.println("Postal Code: " + resp.getBasicDetails()[i].getDemographicMetadata().getPostalCode());
                    System.out.println("MESSAGE: No POA Given!");
                    System.out.println("------------------------------");
                    count++;
                }
                i++;
            }
            System.out.println("No. of records with no documents: " + String.valueOf(count));
            System.out.println("------------------------------");
        } catch (Error ex) {
            System.out.println(ex.getMessage());
            System.out.println("------------------------------");
        }
    }
    
    public static void display_not_booked() throws IOException {
        try {
            ResponseDetails resp = DisplayCalls.display_applications(envManager.getEnv("auth"));

            int count = 0;
            int i = 0;
            int records = Integer.parseInt(resp.getTotalRecords());
            while (i < records) {
                String x = resp.getBasicDetails()[i].getStatusCode();
                if (!x.equals("Booked")) {
                    System.out.println("Application ID: " + resp.getBasicDetails()[i].getPreRegistrationId());
                    System.out.println("Full Name: " + resp.getBasicDetails()[i].getDemographicMetadata().getFullName()[0].getValue());
                    System.out.println("Status: " + x);
                    System.out.println("Postal Code: " + resp.getBasicDetails()[i].getDemographicMetadata().getPostalCode());
                    System.out.println("------------------------------");
                    count++;
                }
                i++;
            }
            System.out.println("No. of not booked records: " + String.valueOf(count));
            System.out.println("------------------------------");
        } catch (Error ex) {
            System.out.println(ex.getMessage());
            System.out.println("------------------------------");
        }
    }    
}