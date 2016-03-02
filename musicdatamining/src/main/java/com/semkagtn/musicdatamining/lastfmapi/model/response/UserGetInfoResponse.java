package com.semkagtn.musicdatamining.lastfmapi.model.response;

import com.semkagtn.musicdatamining.lastfmapi.model.item.LastFmUserItem;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by semkagtn on 08.02.16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGetInfoResponse extends BaseLastFmResponse {

    @JsonProperty("user")
    private LastFmUserItem user;

    public LastFmUserItem getUser() {
        return user;
    }
}
