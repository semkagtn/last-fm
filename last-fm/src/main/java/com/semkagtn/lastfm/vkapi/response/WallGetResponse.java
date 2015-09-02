package com.semkagtn.lastfm.vkapi.response;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by semkagtn on 31.08.15.
 */
public class WallGetResponse extends BaseVkResponse {

    @JsonProperty("response")
    private WallItemList response;

    public WallItemList getResponse() {
        return response;
    }
}
