package net.mosip.login;

import java.io.IOException;

public class Login {
    public static void main(String args[]) throws IOException{
        String response = loginProc();
        System.out.println(response);
    }

    public static String loginProc() throws IOException{
        String resp;

        new Config_json();
        resp = Config_json.config_json();
        System.out.println(resp);
        new Eng_json();
        resp = Eng_json.eng_json();
        new Config();
        resp = Config.config();
        new Captcha();
        resp = Captcha.sendOtpWithCaptcha();
        new Validate();
        resp = Validate.validateOtp();

        return resp;
    }
}
