package net.mosip.models.register.demographic.consent;

public class ConsentException extends Exception {
    public ConsentException (ResponseDataConsent result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}
