package net.mosip.models.register.demographic.zone;

public class ZoneException extends Exception {
    public ZoneException (ResponseDataZone result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}
