package net.mosip.models.register.book.centers;

public class CentersException extends Exception {
    public CentersException (ResponseDataCenters result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}
