package com.semkagtn.musicdatamining.vkapi.response;

import com.semkagtn.musicdatamining.utils.JsonUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by semkagtn on 30.08.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseVkResponse {

    @JsonProperty("error")
    private VkError error;

    public VkError getError() {
        return error;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
