package com.semkagtn.lastfm.lastfmapi.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created by semkagtn on 02.09.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagsItem {

    @JsonProperty("tag")
    private List<TagItem> tag;

    public List<TagItem> getTag() {
        return tag;
    }
}
