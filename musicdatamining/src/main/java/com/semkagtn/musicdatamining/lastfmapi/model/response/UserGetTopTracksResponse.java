package com.semkagtn.musicdatamining.lastfmapi.model.response;

import com.semkagtn.musicdatamining.lastfmapi.model.item.TracksItem;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by semkagtn on 08.02.16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGetTopTracksResponse extends BaseLastFmResponse {

    @JsonProperty("toptracks")
    private TracksItem tracks;

    public TracksItem getTracks() {
        return tracks;
    }
}
