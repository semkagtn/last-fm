package com.semkagtn.lastfm.vkapi.response;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by semkagtn on 31.08.15.
 */
public class AudioGetResponse extends BaseVkResponse {

    @JsonProperty("response")
    private AudioList response;

    public AudioList getResponse() {
        return response;
    }
}
