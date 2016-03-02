package com.semkagtn.musicdatamining.vkapi.response;

import com.semkagtn.musicdatamining.utils.JsonUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by semkagtn on 30.08.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VkError {

    @JsonProperty("error_code")
    private int errorCode;

    @JsonProperty("error_msg")
    private String errorMsg;

    @JsonProperty("captcha_sid")
    private String captchaSid;

    @JsonProperty("captcha_img")
    private String captchaImg;

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String getCaptchaSid() {
        return captchaSid;
    }

    public String getCaptchaImg() {
        return captchaImg;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
