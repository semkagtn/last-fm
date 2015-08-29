package com.semkagtn.lastfm.api;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by semkagtn on 3/6/15.
 */
public class Track {

    private String name;
    private String album;
    private String artist;
    private int duration;
    private int listeners;
    private int playcount;
    private List<String> tags;
    private String playedWhen;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getListeners() {
        return listeners;
    }

    public void setListeners(int listeners) {
        this.listeners = listeners;
    }

    public int getPlaycount() {
        return playcount;
    }

    public void setPlaycount(int playcount) {
        this.playcount = playcount;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getPlayedWhen() {
        return playedWhen;
    }

    public void setPlayedWhen(String playedWhen) {
        this.playedWhen = playedWhen;
    }

    Track() {

    }

    public static class GetInfo extends Request<Track> {

        public static Request<Track> createRequest(String artist, String track) {
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("method", "track.getInfo"));
            parameters.add(new BasicNameValuePair("track", track));
            parameters.add(new BasicNameValuePair("artist", artist));
            return new GetInfo(parameters);
        }

        private GetInfo(List<NameValuePair> parameters) {
            super(parameters);
        }

        @Override
        Track parseResponse(JSONObject response) {
            JSONObject trackJson = response.optJSONObject("track");
            if (trackJson == null) {
                throw new ParseResponseError("no 'track' fields", response);
            }
            String name = trackJson.optString("name");
            if (name == null) {
                throw new ParseResponseError("no 'name' field", response);
            }
            String artist = "";
            JSONObject artistJson = trackJson.optJSONObject("artist");
            if (artistJson != null) {
                artist = artistJson.optString("name", "");
            }
            String album = "";
            JSONObject albumJson = trackJson.optJSONObject("album");
            if (albumJson != null) {
                album = albumJson.optString("title", "");
            }
            List<String> tags = new ArrayList<>();
            JSONObject tagsJson = trackJson.optJSONObject("toptags");
            if (tagsJson != null) {
                JSONArray tagsArrayJson = tagsJson.optJSONArray("tag");
                if (tagsArrayJson != null) {
                    for (int i = 0; i < tagsArrayJson.length(); i++) {
                        JSONObject tagJson = tagsArrayJson.getJSONObject(i);
                        String tag = tagJson.optString("name");
                        if (tag != null) {
                            tags.add(tag);
                        }
                    }
                }
            }
            Track track = new Track();
            track.setName(name);
            track.setArtist(artist);
            track.setAlbum(album);
            track.setDuration(trackJson.optInt("duration", -1));
            track.setListeners(trackJson.optInt("listeners", -1));
            track.setPlaycount(trackJson.optInt("playcount", -1));
            track.setTags(tags);
            track.setPlayedWhen("");
            return track;
        }
    }
}
