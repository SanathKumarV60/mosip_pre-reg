package net.mosip.models.cancel;

public class CancelError extends Exception {
    public CancelError (ResponseDataCancel result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}
