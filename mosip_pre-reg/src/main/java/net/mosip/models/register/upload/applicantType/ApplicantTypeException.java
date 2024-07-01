package net.mosip.models.register.upload.applicantType;

public class ApplicantTypeException extends Exception {
    public ApplicantTypeException (ResponseDataApplicantType result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}
