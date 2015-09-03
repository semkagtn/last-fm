package com.semkagtn.lastfm.lastfmapi.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by semkagtn on 03.09.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackItem {

    @JsonProperty("name")
    private String name;

    @JsonProperty("artist")
    private ArtistItem artist;

    public String getName() {
        return name;
    }

    public ArtistItem getArtist() {
        return artist;
    }
}
