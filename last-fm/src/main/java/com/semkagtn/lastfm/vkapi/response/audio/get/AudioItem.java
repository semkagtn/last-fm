package com.semkagtn.lastfm.vkapi.response.audio.get;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by semkagtn on 31.08.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AudioItem {

    @JsonProperty("artist")
    private String artist;

    @JsonProperty("title")
    private String title;

    @JsonProperty("genre_id")
    private int genreId;

    @JsonProperty("url")
    private String url;

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public int getGenreId() {
        return genreId;
    }

    public String getUrl() {
        return url;
    }
}
