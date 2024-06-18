package net.mosip.login;

import java.io.IOException;

public class Login {
    public static void main(String args[]) throws IOException{
        loginProc();
    }

    public static String loginProc() throws IOException{
        String response = null;

        new Config_json();
        response = Config_json.config_json();
        System.out.println(response);

        new Eng_json();
        Eng_json.eng_json();
        new Config();
        Config.config();
        new Captcha();
        Captcha.main(null);
        new Validate();
        Validate.main(null);

        return null;
    }
}
