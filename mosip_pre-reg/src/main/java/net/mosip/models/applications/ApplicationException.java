package net.mosip.models.applications;

public class ApplicationException extends Exception {
    public ApplicationException (ResponseData result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}
