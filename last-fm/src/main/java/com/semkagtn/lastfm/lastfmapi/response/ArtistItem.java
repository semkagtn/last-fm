package com.semkagtn.lastfm.lastfmapi.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created by semkagtn on 02.09.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArtistItem {

    @JsonProperty("name")
    private String name;

    @JsonProperty("tags")
    private TagsItem tags;

    public String getName() {
        return name;
    }

    public TagsItem getTags() {
        return tags;
    }
}
