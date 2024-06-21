package net.mosip.applications;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import net.mosip.envManager;

public class DisplayCalls {
    public static void main(String[] args) throws IOException, Error {
        display_applications(envManager.getEnv("auth"));
        //booked_details(envManager.getEnv("applicationId"));
    }

    public static ResponseDetails display_applications(String auth) throws IOException, Error {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/applications")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + auth)
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        // Create JSON parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseData result = objectMapper.readValue(responseBody, ResponseData.class);

        if (result.getErrors() == null) {
            return result.getResponse();
        } else {
            throw new Error(result);
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

class Error extends Exception {
    public Error (ResponseData result) {
        super ("ERROR: " + result.getErrors()[0].getErrorCode() + ": " + result.getErrors()[0].getMessage());
    }
}