package com.semkagtn.musicdatamining.lastfmapi.model.response;

import com.semkagtn.musicdatamining.lastfmapi.model.item.ArtistsItem;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by semkagtn on 08.02.16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGetTopArtistsResponse extends BaseLastFmResponse {

    @JsonProperty("topartists")
    private ArtistsItem topArtists;

    public ArtistsItem getTopArtists() {
        return topArtists;
    }
}
