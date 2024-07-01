package net.mosip.models.register.demographic.pincode;

public class PincodeException extends Exception {
    public PincodeException (ResponseDataPin result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}
