package com.semkagtn.musicdatamining.lastfmapi.model.response;

import com.semkagtn.musicdatamining.lastfmapi.model.item.LastFmUsersItem;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by semkagtn on 08.02.16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGetFriendsResponse extends BaseLastFmResponse {

    @JsonProperty("friends")
    private LastFmUsersItem friends;

    public LastFmUsersItem getFriends() {
        return friends;
    }
}
