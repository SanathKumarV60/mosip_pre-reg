package net.mosip.models.register.demographic.retrieve;

public class RetrieveException extends Exception {
    public RetrieveException (ResponseDataRetrieve result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}
