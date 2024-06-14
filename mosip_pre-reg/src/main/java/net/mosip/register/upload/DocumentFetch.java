package net.mosip.register.upload;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mosip.envManager;
import okhttp3.*;

public class DocumentFetch {
    public static void main(String[] args) throws IOException {
        docFetch(envManager.getEnv("applicationId"));
    }

    public static void docFetch(String applicationId) throws IOException {
        new envManager();

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/documents/preregistration/" + applicationId)
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataFetch result = objectMapper.readValue(responseBody, ResponseDataFetch.class);

        if(result.errors == null){
            System.out.println("MESSAGE: Documents already given for " + applicationId + "!");
            System.out.println("------------------------------");
            int l = result.response.documentsMetaData.length;
            for(int i = 0; i < l; i++){
                System.out.println("Document For: " + result.response.documentsMetaData[i].docCatCode);
                System.out.println("Document Type: " + result.response.documentsMetaData[i].docTypCode);
                System.out.println("Document Name: " + result.response.documentsMetaData[i].docName);
                System.out.println("Document ID: " + result.response.documentsMetaData[i].documentId);
                System.out.println("Document Reference ID: " + result.response.documentsMetaData[i].docRefId);
                System.out.println("------------------------------");
            }
        } else if (result.errors[0].errorCode.equals("PRG_PAM_DOC_005")) {
            envManager.updateEnv("applicationId", applicationId);
            RunUpload.runUpload(applicationId);
        } else {
            int l = result.errors.length;
            for(int i = 0; i < l; i++){
                System.err.println("ERROR: " + result.errors[i].errorCode + ": " + result.errors[i].message);
            }
            System.out.println("------------------------------");
        }
    }
}

class ResponseDataFetch {
    public String id;
    public String version;
    public String responsetime;
    public ResponseDetailsFetch response;
    public ErrorsFetch[] errors;
}

class ResponseDetailsFetch {
    public DocumentsMetaDataFetch[] documentsMetaData;
}

class DocumentsMetaDataFetch {
    public String docName;
    public String documentId;
    public String docCatCode;
    public String docTypCode;
    public String langCode;
    public String docRefId;
}

class ErrorsFetch {
    public String errorCode;
    public String message;
}