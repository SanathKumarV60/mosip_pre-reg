package net.mosip.models.register.confirmation.generateQR;

public class QRException extends Exception {
    public QRException (ResponseDataQR result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}
