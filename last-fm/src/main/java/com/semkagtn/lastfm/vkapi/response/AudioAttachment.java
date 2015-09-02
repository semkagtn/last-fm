package com.semkagtn.lastfm.vkapi.response;

import com.semkagtn.lastfm.vkapi.response.AudioItem;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by semkagtn on 31.08.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AudioAttachment {

    @JsonProperty("audio")
    private AudioItem audio;

    public AudioItem getAudio() {
        return audio;
    }
}
