package net.mosip.models.login.validate;

public class ValidateHeaderError extends Exception {
    public ValidateHeaderError () {
        super ("ERROR: NO HEADERS! LOGIN UNSUCCESSFUL!");
    }
}
