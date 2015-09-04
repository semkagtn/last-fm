package com.semkagtn.lastfm.vkapi.response;

import com.semkagtn.lastfm.utils.JsonUtils;
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

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
