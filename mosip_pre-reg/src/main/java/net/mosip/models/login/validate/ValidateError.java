package net.mosip.models.login.validate;

public class ValidateError extends Exception {
    public ValidateError (ResponseDataValidate result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    } 
}
