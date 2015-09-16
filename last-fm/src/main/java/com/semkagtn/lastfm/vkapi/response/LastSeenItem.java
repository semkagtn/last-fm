package com.semkagtn.lastfm.vkapi.response;

import com.semkagtn.lastfm.utils.JsonUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by semkagtn on 12.09.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LastSeenItem {

    @JsonProperty("time")
    private Long time;

    public Long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
