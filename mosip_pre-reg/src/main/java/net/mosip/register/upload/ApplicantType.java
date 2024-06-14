package net.mosip.register.upload;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mosip.envManager;
import okhttp3.*;

public class ApplicantType {
    public static void main(String[] args) throws IOException {
        getType(envManager.getEnv("applicationId"));
    }

    public static void getType(String applicationId) throws IOException {
        new envManager();

        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        OffsetDateTime gmtTime = now.withOffsetSameInstant(ZoneOffset.UTC);
        String formattedTime = formatter.format(gmtTime);
        envManager.updateEnv("currentTime", formattedTime);

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create("{\r\n    \"id\": \"mosip.applicanttype.fetch\",\r\n    \"request\": {\r\n        \"attributes\": [\r\n            {\r\n                \"attribute\": \"individualTypeCode\",\r\n                \"value\": \"" + envManager.getEnv("residenceStat") + "\"\r\n            },\r\n            {\r\n                \"attribute\": \"dateofbirth\",\r\n                \"value\": \"" + dateConverter(envManager.getEnv("dob")) + "\"\r\n            },\r\n            {\r\n                \"attribute\": \"genderCode\",\r\n                \"value\": \"" + envManager.getEnv("gender") + "\"\r\n            },\r\n            {\r\n                \"attribute\": \"biometricAvailable\",\r\n                \"value\": false\r\n            }\r\n        ]\r\n    },\r\n    \"metadata\": {},\r\n    \"version\": \"1.0\",\r\n    \"requesttime\": \"" + formattedTime + "\"\r\n}", mediaType);
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/getApplicantType")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataApplicantType result = objectMapper.readValue(responseBody, ResponseDataApplicantType.class);

        if (result.errors == null){
            envManager.updateEnv("applicantType", result.response.applicantType.applicantTypeCode);
        } else {
            int l = result.errors.length;
            for(int i = 0; i < l; i++){
                System.err.println("ERROR: " + result.errors[i].errorCode + ": " + result.errors[i].message);
            }
            System.out.println("------------------------------");
        }
    }

    public static String dateConverter(String inputDateStr){
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate inputDate = LocalDate.parse(inputDateStr, inputFormatter);
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);

        LocalDateTime combinedDateTime = inputDate.atTime(currentTime.getHour(), currentTime.getMinute(), currentTime.getSecond(), currentTime.getNano());
        
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String outputDateStr = combinedDateTime.format(outputFormatter);

        return outputDateStr;
    }
}

class ResponseDataApplicantType {
    public String id;
    public String version;
    public String responsetime;
    public MetaDataApplicantType[] metadata; // Can be null as specified
    public ResponseDetailsApplicantType response;
    public ErrorsApplicantType[] errors; // Can be null as specified
}

class MetaDataApplicantType {
    //can be null
}

class ResponseDetailsApplicantType {
    public ApplicantTypeVal applicantType;
}

class ApplicantTypeVal {
    public String applicantTypeCode;
}

class ErrorsApplicantType {
    public String errorCode;
    public String message;
}
  