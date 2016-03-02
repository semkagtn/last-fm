package com.semkagtn.musicdatamining.lastfmapi.model.item;

import com.semkagtn.musicdatamining.utils.JsonUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by semkagtn on 02.09.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArtistItem {

    @JsonProperty("name")
    private String name;

    @JsonProperty("tags")
    private TagsItem tags;

    @JsonProperty("playcount")
    private Integer playcount;

    public String getName() {
        return name;
    }

    public TagsItem getTags() {
        return tags;
    }

    public Integer getPlaycount() {
        return playcount;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
