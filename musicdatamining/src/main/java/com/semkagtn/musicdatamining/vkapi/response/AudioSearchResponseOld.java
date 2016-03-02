package com.semkagtn.musicdatamining.vkapi.response;

import com.semkagtn.musicdatamining.utils.JsonUtils;
import com.semkagtn.musicdatamining.vkapi.response.deserializer.AudioItemsWithoutCountDeserializer;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.List;

/**
 * Created by semkagtn on 25.02.16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AudioSearchResponseOld extends BaseVkResponse {

    @JsonProperty("response")
    @JsonDeserialize(using = AudioItemsWithoutCountDeserializer.class)
    private List<AudioItem> response;

    public List<AudioItem> getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
