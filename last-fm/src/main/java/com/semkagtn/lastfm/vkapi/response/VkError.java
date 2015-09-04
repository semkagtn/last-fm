package com.semkagtn.lastfm.vkapi.response;

import com.semkagtn.lastfm.utils.JsonUtils;
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

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
