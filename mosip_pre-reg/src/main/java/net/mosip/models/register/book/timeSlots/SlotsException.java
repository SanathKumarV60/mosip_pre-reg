package net.mosip.models.register.book.timeSlots;

public class SlotsException extends Exception {
    public SlotsException (ResponseDataSlots result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}
