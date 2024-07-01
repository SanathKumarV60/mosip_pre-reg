package net.mosip.models.delete;

public class DeleteException extends Exception {
    public DeleteException (ResponseDataDelete result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}
