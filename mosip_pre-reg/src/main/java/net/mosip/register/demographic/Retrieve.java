package net.mosip.register.demographic;

import java.io.IOException;
import java.io.Console;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.*;
import net.mosip.envManager;

import net.mosip.register.book.RunBooking;
import net.mosip.register.book.CenterDetailsCheck;
import net.mosip.register.upload.DocumentFetch;
import net.mosip.applications.Display;

public class Retrieve {
    public static void main(String[] args) throws IOException {
        retrieve();
    }

    public static void retrieve() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/applications/" + envManager.getEnv("applicationId"))
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataRetrieve result = objectMapper.readValue(responseBody, ResponseDataRetrieve.class);

        System.out.println(responseBody);

        if (result.errors == null){
            System.out.println("Please Check your details: ");
            System.out.println("------------------------------");
            System.out.println("Application ID: " + result.response.preRegistrationId);
            envManager.updateEnv("applicationId", result.response.preRegistrationId);
            System.out.println("Created By: " + result.response.createdBy);
            System.out.println("Created On: " + result.response.createdDateTime);
            System.out.println("Name: " + result.response.demographicDetails.identity.fullName[0].value);
            envManager.updateEnv("name", result.response.demographicDetails.identity.fullName[0].value);
            System.out.println("DOB: " + result.response.demographicDetails.identity.dateOfBirth);
            envManager.updateEnv("dob", result.response.demographicDetails.identity.dateOfBirth);
            System.out.println("Gender: " + result.response.demographicDetails.identity.gender[0].value);
            envManager.updateEnv("gender", result.response.demographicDetails.identity.gender[0].value);
            System.out.println("Address: " + result.response.demographicDetails.identity.addressLine1[0].value);
            envManager.updateEnv("addressLine", result.response.demographicDetails.identity.addressLine1[0].value);
            System.out.println("Region: " + result.response.demographicDetails.identity.region[0].value);
            envManager.updateEnv("region", result.response.demographicDetails.identity.region[0].value);
            System.out.println("Province: " + result.response.demographicDetails.identity.province[0].value);
            envManager.updateEnv("province", result.response.demographicDetails.identity.province[0].value);
            System.out.println("City: " + result.response.demographicDetails.identity.city[0].value);
            envManager.updateEnv("city", result.response.demographicDetails.identity.city[0].value);
            System.out.println("Zone: " + result.response.demographicDetails.identity.zone[0].value);
            envManager.updateEnv("zone", result.response.demographicDetails.identity.zone[0].value);
            System.out.println("Postal Code: " + result.response.demographicDetails.identity.postalCode);
            envManager.updateEnv("pincode", result.response.demographicDetails.identity.postalCode);
            System.out.println("Phone Number: " + result.response.demographicDetails.identity.phone);
            envManager.updateEnv("phoneNumber", result.response.demographicDetails.identity.phone);
            System.out.println("E-mail: " + result.response.demographicDetails.identity.email);
            envManager.updateEnv("email", result.response.demographicDetails.identity.email);
            System.out.println("Status: " + result.response.statusCode);;
            envManager.updateEnv("status", result.response.statusCode);
            System.out.println("------------------------------");

            next(result.response.preRegistrationId);

        } else {
            int l = result.errors.length;
            for(int i = 0; i < l; i++){
                System.err.println("ERROR: " + result.errors[i].errorCode + ": " + result.errors[i].message);
            }
            System.out.println("------------------------------");
        }
    }

    public static void next(String applicationId) throws IOException{
        Console console = System.console();
        while(true) {
            System.out.println("""
                    1. Upload Documents
                    2. Book Appointment
                    3. Exit application process and do it later
                    """);
            int c = Integer.parseInt(console.readLine("How would you like to continue? (enter a number): "));
            System.out.println("------------------------------");
            
            if (c == 3) {
                break;
            }

            switch (c) {
                case 1:
                    DocumentFetch.docFetch(applicationId);
                    break;
                case 2:
                new Display();
                new CenterDetailsCheck();
                    if (envManager.getEnv("status").equals("Booked")) {
                        System.err.println("ERROR: Application ID has already been booked!\nPlease cancel the current booking to book again!");
                        System.out.println("------------------------------");
                        Display.booked_details(applicationId);
                        CenterDetailsCheck.all_details(envManager.getEnv("regCenterId"));
                    } else {
                        RunBooking.runBooking(applicationId);
                    }
                    break;
                default:
                    System.err.println("ERROR: Please enter a valid integer!");
                    System.out.println("------------------------------");
            }
        }
    }
}

class ResponseDataRetrieve {
    public String id;
    public String version;
    public String responsetime;
    public ResponseDetailsRetrieve response;
    public ErrorsRetrieve[] errors;
}

class ResponseDetailsRetrieve {
    public String preRegistrationId;
    public String createdBy;
    public String createdDateTime;
    public String updatedBy;
    public String updatedDateTime;
    public String statusCode;
    public String langCode;
    public DemographicDetailsRetrieve demographicDetails;
}

class DemographicDetailsRetrieve {
    public IdentityRetrieve identity;
}

class IdentityRetrieve {
    public GenderRetrieve[] gender;
    public CityRetrieve[] city;
    public String postalCode;
    public NameRetrieve[] fullName;
    public String dateOfBirth;
    public double IDSchemaVersion;
    public ProvinceRetrieve[] province;
    public ZoneRetrieve[] zone;
    public String phone;
    public AddressRetrieve[] addressLine1;
    public ResidenceRetrieve[] residenceStatus;
    public RegionRetrieve[] region;
    public String email;
}

class GenderRetrieve {
    public String language;
    public String value;
}

class CityRetrieve {
    public String language;
    public String value;
}

class NameRetrieve {
    public String language;
    public String value;
}

class ProvinceRetrieve {
    public String language;
    public String value;
}

class ZoneRetrieve {
    public String language;
    public String value;
}

class AddressRetrieve {
    public String language;
    public String value;
}

class ResidenceRetrieve {
    public String language;
    public String value;
}

class RegionRetrieve {
    public String language;
    public String value;
}

class ErrorsRetrieve {
    public String errorCode;
    public String message;
}