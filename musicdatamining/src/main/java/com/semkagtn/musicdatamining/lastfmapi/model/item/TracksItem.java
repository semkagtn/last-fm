package com.semkagtn.musicdatamining.lastfmapi.model.item;

import com.semkagtn.musicdatamining.utils.JsonUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created by semkagtn on 08.02.16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TracksItem {

    @JsonProperty("track")
    private List<TrackItem> tracks;

    public List<TrackItem> getTracks() {
        return tracks;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
