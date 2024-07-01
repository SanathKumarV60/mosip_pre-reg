package net.mosip.models.register.upload.uploadDoc;

public class UploadException extends Exception {
    public UploadException (ResponseDataUpload result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    }
}
