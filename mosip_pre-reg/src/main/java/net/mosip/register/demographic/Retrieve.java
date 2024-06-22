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
        //retrieve();
        updateAllEnv(envManager.getEnv("applicationId"));
    }

    public static ResponseDetailsRetrieve retrieve_call(String auth, String applicationId) throws IOException, ErrorRetrieve {
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/applications/" + applicationId)
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + auth)
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataRetrieve result = objectMapper.readValue(responseBody, ResponseDataRetrieve.class);

        if(result.errors == null) {
            return result.response;
        } else {
            throw new ErrorRetrieve(result);
        }
    }

    public static void updateAllEnv (String applicationId) throws IOException {
        try{
            ResponseDetailsRetrieve resp = retrieve_call(envManager.getEnv("auth"), applicationId);

            envManager.updateEnv("applicationId", resp.preRegistrationId);
            envManager.updateEnv("name", resp.demographicDetails.identity.fullName[0].value);
            envManager.updateEnv("dob", resp.demographicDetails.identity.dateOfBirth);
            envManager.updateEnv("gender", resp.demographicDetails.identity.gender[0].value);
            envManager.updateEnv("addressLine", resp.demographicDetails.identity.addressLine1[0].value);
            envManager.updateEnv("region", resp.demographicDetails.identity.region[0].value);
            envManager.updateEnv("province", resp.demographicDetails.identity.province[0].value);
            envManager.updateEnv("city", resp.demographicDetails.identity.city[0].value);
            envManager.updateEnv("zone", resp.demographicDetails.identity.zone[0].value);
            envManager.updateEnv("pincode", resp.demographicDetails.identity.postalCode);
            envManager.updateEnv("phoneNumber", resp.demographicDetails.identity.phone);
            envManager.updateEnv("email", resp.demographicDetails.identity.email);
            envManager.updateEnv("status", resp.statusCode);
            envManager.updateEnv("residenceStat", resp.demographicDetails.identity.residenceStatus[0].value);
            
        } catch (ErrorRetrieve ex) {
            System.err.println(ex.getMessage());
            System.out.println("------------------------------");
        }

    }


    public static void retrieve() throws IOException {
        try {
            ResponseDetailsRetrieve resp = retrieve_call(envManager.getEnv("auth"), envManager.getEnv("applicationId"));

            System.out.println("Please Check your details: ");
            System.out.println("------------------------------");
            System.out.println("Application ID: " + resp.preRegistrationId);
            envManager.updateEnv("applicationId", resp.preRegistrationId);
            System.out.println("Created By: " + resp.createdBy);
            System.out.println("Created On: " + resp.createdDateTime);
            System.out.println("Name: " + resp.demographicDetails.identity.fullName[0].value);
            envManager.updateEnv("name", resp.demographicDetails.identity.fullName[0].value);
            System.out.println("DOB: " + resp.demographicDetails.identity.dateOfBirth);
            envManager.updateEnv("dob", resp.demographicDetails.identity.dateOfBirth);
            System.out.println("Gender: " + resp.demographicDetails.identity.gender[0].value);
            envManager.updateEnv("gender", resp.demographicDetails.identity.gender[0].value);
            System.out.println("Address: " + resp.demographicDetails.identity.addressLine1[0].value);
            envManager.updateEnv("addressLine", resp.demographicDetails.identity.addressLine1[0].value);
            System.out.println("Region: " + resp.demographicDetails.identity.region[0].value);
            envManager.updateEnv("region", resp.demographicDetails.identity.region[0].value);
            System.out.println("Province: " + resp.demographicDetails.identity.province[0].value);
            envManager.updateEnv("province", resp.demographicDetails.identity.province[0].value);
            System.out.println("City: " + resp.demographicDetails.identity.city[0].value);
            envManager.updateEnv("city", resp.demographicDetails.identity.city[0].value);
            System.out.println("Zone: " + resp.demographicDetails.identity.zone[0].value);
            envManager.updateEnv("zone", resp.demographicDetails.identity.zone[0].value);
            System.out.println("Postal Code: " + resp.demographicDetails.identity.postalCode);
            envManager.updateEnv("pincode", resp.demographicDetails.identity.postalCode);
            System.out.println("Phone Number: " + resp.demographicDetails.identity.phone);
            envManager.updateEnv("phoneNumber", resp.demographicDetails.identity.phone);
            System.out.println("E-mail: " + resp.demographicDetails.identity.email);
            envManager.updateEnv("email", resp.demographicDetails.identity.email);
            System.out.println("Status: " + resp.statusCode);
            envManager.updateEnv("status", resp.statusCode);
            System.out.println("------------------------------");

            next(resp.preRegistrationId);

        } catch (ErrorRetrieve ex) {
            System.err.println(ex.getMessage());
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

class ErrorRetrieve extends Exception {
    public ErrorRetrieve (ResponseDataRetrieve result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}