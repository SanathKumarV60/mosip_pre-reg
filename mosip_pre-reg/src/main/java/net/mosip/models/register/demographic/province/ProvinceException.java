package net.mosip.models.register.demographic.province;

public class ProvinceException extends Exception {
    public ProvinceException (ResponseDataProvince result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}
