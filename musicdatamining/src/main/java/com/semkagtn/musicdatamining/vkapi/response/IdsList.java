package com.semkagtn.musicdatamining.vkapi.response;

import com.semkagtn.musicdatamining.utils.JsonUtils;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created by semkagtn on 24.02.16.
 */
public class IdsList {

    @JsonProperty("count")
    private int count;

    @JsonProperty("items")
    private List<Long> ids;

    public int getCount() {
        return count;
    }

    public List<Long> getIds() {
        return ids;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
