package net.mosip.register.upload;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mosip.envManager;
import okhttp3.*;

public class Choose {
    public static void main(String[] args) throws IOException {
        chooseUploadType();
    }

    public static ResponseDetailsChoose chooseUploadType_call(String auth, String applicantType) throws IOException, ErrorChoose {
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/applicanttype/" + applicantType + "/languages?languages=eng")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + auth)
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataChoose result = objectMapper.readValue(responseBody, ResponseDataChoose.class);

        if (result.errors == null) {
            return result.response;
        } else {
            throw new ErrorChoose(result);
        }
    }

    public static void chooseUploadType() throws IOException{
        try {
            ResponseDetailsChoose resp = chooseUploadType_call(envManager.getEnv("auth"), envManager.getEnv("applicantType"));

            if(resp.isActive){
                int i = 1;
                for(int j = 0; j < resp.documentCategories.length; j++){
                    if(resp.documentCategories[j].isActive){
                        System.out.println(String.valueOf(i) + ". Options for " + resp.documentCategories[j].name + " (" + resp.documentCategories[j].code + ") - " + resp.documentCategories[j].description + ": ");
                        System.out.println("________________________________");
                        
                        int m = 1;
                        ArrayList<String> typesList = new ArrayList<>();
                        for(int n = 0; n < resp.documentCategories[j].documentTypes.length; n++){
                            if(resp.documentCategories[j].documentTypes[n].isActive){
                                System.out.println(String.valueOf(m) + ". " + resp.documentCategories[j].documentTypes[n].name + " (" + resp.documentCategories[j].documentTypes[n].code + ") - " + resp.documentCategories[j].documentTypes[n].description);

                                typesList.add(resp.documentCategories[j].documentTypes[n].code);
                            } else {
                                continue;
                            }
                        }
                        String[] types = typesList.stream().toArray(String[]::new);

                        Console console  = System.console();
                        int option = -1;
                        while(true){
                            option = Integer.parseInt(console.readLine("Please choose an option (enter a number): ")) - 1;
                            System.out.println("------------------------------");
                            if(option > types.length){
                                System.err.println("ERROR: Please enter a valid value!");
                                System.out.println("------------------------------");
                            } else {
                                break;
                            }
                        }

                        envManager.updateEnv(resp.documentCategories[j].code + "docType", types[option]);

                        UploadDoc.uploadDoc(resp.documentCategories[j].code, types[option]);
                    } else {
                        continue;
                    }
                }
            } else {
                System.err.println("ERROR: No data found for applicant type: " + envManager.getEnv("applicantType"));
            }
        } catch (ErrorChoose ex) {
            System.err.println(ex.getMessage());
            System.out.println("------------------------------");
        }
    }
}

class ResponseDataChoose {
    public String id;
    public String version;
    public String responsetime;
    public ResponseDetailsChoose response;
    public MetaDataChoose[] metadata;
    public ErrorsChoose[] errors;
}

class ResponseDetailsChoose {
    public String appTypeCode;
    public String langCode;
    public boolean isActive;
    public DocumentCategories[] documentCategories;
}

class DocumentCategories {
    public String code;
    public String name;
    public String description;
    public String langCode;
    public boolean isActive;
    public DocumentTypes[] documentTypes;
}

class DocumentTypes {
    public String code;
    public String name;
    public String description;
    public String langCode;
    public boolean isActive;
}

class MetaDataChoose {
    //Empty
}

class ErrorsChoose {
    public String errorCode;
    public String message;
}

class ErrorChoose extends Exception {
    public ErrorChoose (ResponseDataChoose result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}