package net.mosip.models.login.captcha;

public class CaptchaHeaderError extends Exception {
    public CaptchaHeaderError () {
        super ("ERROR: NO HEADERS! LOGIN UNSUCCESSFUL!");
    }
}
