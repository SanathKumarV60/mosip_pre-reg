package net.mosip.models.register.book.centers;

public class WorkingException extends Exception {
    public WorkingException (ResponseWorking result2) {
        super ("ERROR: " + result2.errors[0].errorCode + ": " + result2.errors[0].message);
    }
}
