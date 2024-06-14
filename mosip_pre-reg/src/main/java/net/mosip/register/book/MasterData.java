package net.mosip.register.book;

import java.io.IOException;

import net.mosip.envManager;
import okhttp3.*;

public class MasterData {
    public static void main(String[] args) throws IOException {
        getMasterData();
    }

    @SuppressWarnings("unused")
    public static void getMasterData() throws IOException {
        new envManager();

        //REGION
        OkHttpClient client1 = new OkHttpClient().newBuilder()
            .build();
        Request request1 = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/info/" + envManager.getEnv("region") + "/eng")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response1 = client1.newCall(request1).execute();
        
        //Province
        OkHttpClient client2 = new OkHttpClient().newBuilder()
            .build();
        Request request2 = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/info/" + envManager.getEnv("province") + "/eng")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response2 = client2.newCall(request2).execute();

        //City
        OkHttpClient client3 = new OkHttpClient().newBuilder()
            .build();
        Request request3 = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/info/" + envManager.getEnv("city") + "/eng")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response3 = client3.newCall(request3).execute();

        //Zone
        OkHttpClient client4 = new OkHttpClient().newBuilder()
            .build();
        Request request4 = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/info/" + envManager.getEnv("zone") + "/eng")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response4 = client4.newCall(request4).execute();

        //Pincode
        OkHttpClient client5 = new OkHttpClient().newBuilder()
            .build();
        Request request5 = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/info/" + envManager.getEnv("pincode") + "/eng")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response5 = client5.newCall(request5).execute();

        //All Masterdata
        OkHttpClient client6 = new OkHttpClient().newBuilder()
            .build();
        Request request6 = new Request.Builder()
            .url("https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/eng")
            .method("GET", null)
            .addHeader("Cookie", "Authorization=" + envManager.getEnv("auth"))
            .build();
        Response response6 = client6.newCall(request6).execute();
    }
}
