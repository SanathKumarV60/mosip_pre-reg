package net.mosip.models.register.book.centerDetailsCheck;

public class SelectException extends Exception {
    public SelectException (ResponseDataSelect result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}
