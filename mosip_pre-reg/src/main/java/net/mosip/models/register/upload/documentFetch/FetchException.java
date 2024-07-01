package net.mosip.models.register.upload.documentFetch;

public class FetchException extends Exception {
    public FetchException (ResponseDataFetch result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}
