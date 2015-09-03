package com.semkagtn.lastfm.vkapi.response;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by semkagtn on 31.08.15.
 */
public class FriendsGetResponse extends BaseVkResponse {

    @JsonProperty("response")
    private FriendsList response;

    public FriendsList getResponse() {
        return response;
    }
}