package net.mosip.models.register.confirmation.onScreenAck;

public class AckException extends Exception {
    public AckException (ResponseDataAck result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}
