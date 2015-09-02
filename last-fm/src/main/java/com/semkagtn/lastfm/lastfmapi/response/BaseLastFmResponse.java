package com.semkagtn.lastfm.lastfmapi.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by semkagtn on 02.09.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseLastFmResponse {

    @JsonProperty("error")
    private Integer error;

    @JsonProperty("message")
    private String message;

    public Integer getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
