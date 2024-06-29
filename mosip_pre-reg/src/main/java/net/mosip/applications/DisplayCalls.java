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

        if (result.errors == null) {
            return result.response;
        } else {
            throw new Error(result);
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

class BookingMetadata {
    public String registration_center_id;
    public String appointment_date;
    public String time_slot_from;
    public String time_slot_to;
}

class DemographicMetadata {
    public ProofOfAddress proofOfAddress;
    public String postalCode;
    public FullName[] fullName;
}

class ProofOfAddress {
    public String docCatCode;
    public String docTypCode;
    public String docName;
    public String langCode;
    public String documentId;
    public String docRefId;
}

class FullName {
    public String language;
    public String value;
}

class Errors {
    public String errorCode;
    public String message;
}

class Error extends Exception {
    public Error (ResponseData result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}