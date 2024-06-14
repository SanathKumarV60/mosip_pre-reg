package net.mosip.register.demographic;

import java.io.IOException;

public class runDemographicDetails {
    public static void main(String[] args) throws IOException {
        fill();
    }

    public static void fill() throws IOException{
        boolean consented = Consent.giveConsent();

        if(consented){
            Page0.fillPage();
            Region.getRegion();
            Province.getProvince();
            City.getCity();
            Zone.getZone();
            Pincode.getPin();
            Page1.fillPage();
            MakeApplication.makeApplication();
            Retrieve.retrieve();
        }
    }
}
