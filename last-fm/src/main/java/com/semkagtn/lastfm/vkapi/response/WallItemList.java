package com.semkagtn.lastfm.vkapi.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created by semkagtn on 31.08.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WallItemList {

    @JsonProperty("count")
    private int count;

    @JsonProperty("items")
    private List<WallItem> items;

    public int getCount() {
        return count;
    }

    public List<WallItem> getItems() {
        return items;
    }
}
