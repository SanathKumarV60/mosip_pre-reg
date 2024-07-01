package net.mosip.models.login.captcha;

public class CaptchaError extends Exception {
    public CaptchaError (ResponseDataCaptcha result) {
        super ("ERROR: " + result.errors[0].errorCode + ": " + result.errors[0].message);
    } 
}
