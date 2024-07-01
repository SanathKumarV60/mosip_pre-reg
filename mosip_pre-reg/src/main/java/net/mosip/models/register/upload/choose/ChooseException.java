package net.mosip.models.register.upload.choose;

public class ChooseException extends Exception {
    public ChooseException (ResponseDataChoose result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}
