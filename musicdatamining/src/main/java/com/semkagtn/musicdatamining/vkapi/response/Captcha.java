package com.semkagtn.musicdatamining.vkapi.response;

/**
 * Created by semkagtn on 25.02.16.
 */
public class Captcha {

    private String captchaSid;
    private String captchaKey;

    public Captcha(String captchaSid, String captchaKey) {
        this.captchaSid = captchaSid;
        this.captchaKey = captchaKey;
    }

    public String getCaptchaSid() {
        return captchaSid;
    }

    public String getCaptchaKey() {
        return captchaKey;
    }
}
