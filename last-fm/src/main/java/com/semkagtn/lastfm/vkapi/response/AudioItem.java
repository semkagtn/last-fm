package com.semkagtn.lastfm.vkapi.response;

import com.semkagtn.lastfm.utils.JsonUtils;
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

    @JsonProperty("lyrics_id")
    private long lyricsId;

    @JsonProperty("date")
    private long date;

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

    public long getLyricsId() {
        return lyricsId;
    }

    public long getDate() {
        return date;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
