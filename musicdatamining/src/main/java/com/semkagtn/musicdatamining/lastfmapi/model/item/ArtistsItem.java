package com.semkagtn.musicdatamining.lastfmapi.model.item;

import com.semkagtn.musicdatamining.utils.JsonUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created by semkagtn on 08.02.16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArtistsItem {

    @JsonProperty("artist")
    private List<ArtistItem> artists;

    public List<ArtistItem> getArtists() {
        return artists;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
