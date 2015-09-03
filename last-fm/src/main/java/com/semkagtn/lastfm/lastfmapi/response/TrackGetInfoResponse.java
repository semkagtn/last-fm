package com.semkagtn.lastfm.lastfmapi.response;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by semkagtn on 03.09.15.
 */
public class TrackGetInfoResponse extends BaseLastFmResponse {

    @JsonProperty("track")
    private TrackItem track;

    public TrackItem getTrack() {
        return track;
    }
}
