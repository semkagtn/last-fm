package com.semkagtn.musicdatamining.lastfmapi.response;

import com.semkagtn.musicdatamining.utils.JsonUtils;
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

    @JsonProperty("toptags")
    private TagsItem topTags;

    public String getName() {
        return name;
    }

    public ArtistItem getArtist() {
        return artist;
    }

    public TagsItem getTopTags() {
        return topTags;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
