package com.semkagtn.lastfm.vkapi.response;

import com.semkagtn.lastfm.utils.JsonUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created by semkagtn on 11.09.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExecuteGetAttachmentsResponse extends BaseVkResponse {

    @JsonProperty("response")
    private List<List<AudioAttachment>> response;

    public List<List<AudioAttachment>> getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
