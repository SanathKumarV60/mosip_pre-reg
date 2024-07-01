package net.mosip.models.register.confirmation.notify;

public class NotifyException extends Exception{
    public NotifyException (ResponseDataNotif result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}
