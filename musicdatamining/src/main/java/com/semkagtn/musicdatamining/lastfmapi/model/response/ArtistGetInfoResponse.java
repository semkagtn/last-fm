package com.semkagtn.musicdatamining.lastfmapi.model.response;

import com.semkagtn.musicdatamining.lastfmapi.model.item.ArtistItem;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by semkagtn on 02.09.15.
 */
public class ArtistGetInfoResponse extends BaseLastFmResponse {

    @JsonProperty("artist")
    private ArtistItem artist;

    public ArtistItem getArtist() {
        return artist;
    }
}
