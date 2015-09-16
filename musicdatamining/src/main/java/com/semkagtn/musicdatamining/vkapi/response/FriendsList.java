package com.semkagtn.musicdatamining.vkapi.response;

import com.semkagtn.musicdatamining.utils.JsonUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created by semkagtn on 31.08.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FriendsList {

    @JsonProperty("count")
    private int count;

    @JsonProperty("items")
    private List<UserItem> items;

    public int getCount() {
        return count;
    }

    public List<UserItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
