package net.mosip.models.register.demographic.city;

public class CityException extends Exception {
    public CityException (ResponseDataCity result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}
