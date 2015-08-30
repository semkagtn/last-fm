package com.semkagtn.lastfm.vkapi.response.audio.get;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created by semkagtn on 31.08.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AudioList {

    @JsonProperty("count")
    private int count;

    @JsonProperty("items")
    private List<AudioItem> items;

    public int getCount() {
        return count;
    }

    public List<AudioItem> getItems() {
        return items;
    }
}
