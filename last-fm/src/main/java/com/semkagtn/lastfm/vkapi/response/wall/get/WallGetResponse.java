package com.semkagtn.lastfm.vkapi.response.wall.get;

import com.semkagtn.lastfm.vkapi.response.BaseVkResponse;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by semkagtn on 31.08.15.
 */
public class WallGetResponse extends BaseVkResponse {

    @JsonProperty("response")
    private Object response;

    public Object getResponse() {
        return response;
    }
}
