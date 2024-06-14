package net.mosip.register.upload;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mosip.envManager;
import okhttp3.*;

public class UploadDoc {
    public static void main(String[] args) {
        
    }

    public static void uploadDoc(String doc, String type) throws IOException {
        new envManager();

        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        OffsetDateTime gmtTime = now.withOffsetSameInstant(ZoneOffset.UTC);
        String formattedTime = formatter.format(gmtTime);
        envManager.updateEnv("currentTime", formattedTime);

        Console console = System.console();
        String refId = console.readLine("Enter Document Reference ID: ");
        envManager.updateEnv(doc + "docRefId", refId);

        System.out.println("________");
        System.out.println("WARNING: Please replace backslashes (\\) with a double backslash (\\\\) and periods (.) with a backslash period (\\.). Also, do not enclose the path within quotes.");
        System.out.println("________");
        String filePath = console.readLine("Please enter full file path of document to upload: ");
        String fileName = console.readLine("Please enter file name: ") + "." + getFileExtension(filePath);
        System.out.println("------------------------------");

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("Document request","{\"id\":\"mosip.pre-registration.document.upload\",\"request\":{\"docCatCode\":\"" + doc + "\",\"docTypCode\":\"" + type + "\",\"langCode\":\"eng\",\"docRefId\":\"" + refId + "\"},\"metadata\":{},\"version\":\"1.0\",\"requesttime\":\"" + formattedTime + "\"}")
            .addFormDataPart("file", fileName,
                RequestBody.create(new File(filePath), 
                MediaType.parse("application/octet-stream")))
            .build();
        Request request = new Request.Builder()
        .url("https://uat2.mosip.net//preregistration/v1/documents/" + envManager.getEnv("applicationId"))
        .method("POST", body)
        .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
        .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataUpload result = objectMapper.readValue(responseBody, ResponseDataUpload.class);

        if (result.errors ==  null) {
            System.out.println("MESSAGE: Document uploaded! Details:");
            System.out.println("Document Name: " + result.response.docName);
            System.out.println("Document ID: " + result.response.docId);
            envManager.updateEnv(doc + "docId", result.response.docId);
            System.out.println("------------------------------");
        } else {
            int l = result.errors.length;
            for(int i = 0; i < l; i++){
                System.err.println("ERROR: " + result.errors[i].errorCode + ": " + result.errors[i].message);
            }
            System.out.println("------------------------------");
        }
    }

    public static String getFileExtension(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
          return "";
        }
      
        int dotIndex = filePath.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex < filePath.lastIndexOf('/')) {  // Handle cases without extension or directory after dot
          return "";
        }
        return filePath.substring(dotIndex + 1);
      }
}

class ResponseDataUpload {
    public String id;
    public String version;
    public String responsetime;
    public ResponseDetailsUpload response;
    public ErrorsUpload[] errors;
}

class ResponseDetailsUpload {
    public String preRegistrationId;
    public String docId;
    public String docName;
    public String docRefId;
    public String docCatCode;
    public String docTypCode;
    public String docFileFormat;
}

class ErrorsUpload {
    public String errorCode;
    public String message;
}