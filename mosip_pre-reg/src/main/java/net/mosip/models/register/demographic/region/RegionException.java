package net.mosip.models.register.demographic.region;

public class RegionException extends Exception {
    public RegionException (ResponseDataRegion result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}
