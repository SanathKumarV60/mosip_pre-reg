package net.mosip.register.upload;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mosip.envManager;
import net.mosip.register.demographic.Retrieve;
import net.mosip.models.register.upload.documentFetch.*;

import okhttp3.*;

public class DocumentFetch {
    public static void main(String[] args) throws IOException {
        docFetch(envManager.getEnv("applicationId"));
    }

    public static ResponseDetailsFetch docFetch_call(String auth, String applicationId) throws IOException, FetchException {
        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1/documents/preregistration/" + applicationId)
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + auth)
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataFetch result = objectMapper.readValue(responseBody, ResponseDataFetch.class);

        if (result.errors == null) {
            return result.response;
        } else if (result.errors[0].errorCode.equals("PRG_PAM_DOC_005")) {
            Retrieve.updateAllEnv(applicationId);
            ApplicantType.getType(applicationId);
            RunUpload.runUpload(applicationId); 
            return null;
        } else {
            throw new FetchException(result);
        }
    }

    public static String docFetch(String applicationId) throws IOException {
        try {
            ResponseDetailsFetch resp = docFetch_call(envManager.getEnv("auth"), applicationId);

            if (resp == null) {
                return "";
            }

            System.out.println("MESSAGE: Documents already given for " + applicationId + "!");
            System.out.println("------------------------------");
            int l = resp.documentsMetaData.length;
            for(int i = 0; i < l; i++){
                System.out.println("Document For: " + resp.documentsMetaData[i].docCatCode);
                System.out.println("Document Type: " + resp.documentsMetaData[i].docTypCode);
                System.out.println("Document Name: " + resp.documentsMetaData[i].docName);
                System.out.println("Document ID: " + resp.documentsMetaData[i].documentId);
                System.out.println("Document Reference ID: " + resp.documentsMetaData[i].docRefId);
                System.out.println("------------------------------");
            }
            return null;
        } catch (FetchException ex) {
            System.err.println(ex.getMessage());
            System.out.println("------------------------------");
            return "";
        }
    }
}
