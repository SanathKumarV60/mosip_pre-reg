package net.mosip.models.register.demographic.makeApplication;

public class ApplicationException extends Exception {
    public ApplicationException (ResponseDataApp result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}
