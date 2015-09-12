package com.semkagtn.lastfm.vkapi.response;

import com.semkagtn.lastfm.utils.JsonUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by semkagtn on 12.09.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountersItem {

    @JsonProperty("audios")
    private Integer audios;

    public Integer getAudios() {
        return audios;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
