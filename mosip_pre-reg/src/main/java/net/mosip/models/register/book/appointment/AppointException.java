package net.mosip.models.register.book.appointment;

public class AppointException extends Exception {
    public AppointException (ResponseDataAppoint result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}
