package com.semkagtn.lastfm.vkapi.response.users.get;

import com.semkagtn.lastfm.vkapi.response.BaseVkResponse;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created by semkagtn on 30.08.15.
 */
public class UsersGetResponse extends BaseVkResponse {

    @JsonProperty("response")
    private List<UserItem> response;

    public List<UserItem> getResponse() {
        return response;
    }
}
