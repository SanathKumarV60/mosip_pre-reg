package net.mosip.register.upload;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mosip.envManager;
import net.mosip.models.register.upload.applicantType.*;

import okhttp3.*;

public class ApplicantType {
    public static void main(String[] args) throws IOException {
        getType(envManager.getEnv("applicationId"));
    }

    public static ResponseDetailsApplicantType getType_call(String auth, String residenceStat, String dob, String gender) throws IOException, ApplicantTypeException {
        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        OffsetDateTime gmtTime = now.withOffsetSameInstant(ZoneOffset.UTC);
        String formattedTime = formatter.format(gmtTime);

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create("{\r\n    \"id\": \"mosip.applicanttype.fetch\",\r\n    \"request\": {\r\n        \"attributes\": [\r\n            {\r\n                \"attribute\": \"individualTypeCode\",\r\n                \"value\": \"" + residenceStat + "\"\r\n            },\r\n            {\r\n                \"attribute\": \"dateofbirth\",\r\n                \"value\": \"" + dob + "\"\r\n            },\r\n            {\r\n                \"attribute\": \"genderCode\",\r\n                \"value\": \"" + gender + "\"\r\n            },\r\n            {\r\n                \"attribute\": \"biometricAvailable\",\r\n                \"value\": false\r\n            }\r\n        ]\r\n    },\r\n    \"metadata\": {},\r\n    \"version\": \"1.0\",\r\n    \"requesttime\": \"" + formattedTime + "\"\r\n}", mediaType);
        Request request = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/getApplicantType")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Cookie", "Authorization=" + auth)
            .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        //Create json Parse using classes
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDataApplicantType result = objectMapper.readValue(responseBody, ResponseDataApplicantType.class);

        if (result.errors == null) {
            return result.response;
        } else {
            throw new ApplicantTypeException(result);
        }
    }

    public static void getType(String applicationId) throws IOException {
        try {
            ResponseDetailsApplicantType resp = getType_call(envManager.getEnv("auth"), envManager.getEnv("residenceStat"), dateConverter(envManager.getEnv("dob")), envManager.getEnv("gender"));

            envManager.updateEnv("applicantType", resp.applicantType.applicantTypeCode);
        } catch (ApplicantTypeException ex) {
            System.err.println(ex.getMessage());
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
