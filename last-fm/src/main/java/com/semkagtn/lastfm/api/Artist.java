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
public class Artist {

    private String name;
    private int listeners;
    private int playcount;
    private List<String> tags;

    public String getName() {
        return name;
    }

    public int getListeners() {
        return listeners;
    }

    public int getPlaycount() {
        return playcount;
    }

    public List<String> getTags() {
        return tags;
    }

    void setName(String name) {
        this.name = name;
    }

    void setListeners(int listeners) {
        this.listeners = listeners;
    }

    void setPlaycount(int playcount) {
        this.playcount = playcount;
    }

    void setTags(List<String> tags) {
        this.tags = tags;
    }

    Artist() {

    }

    public static class GetInfo extends Request<Artist> {

        public static Request<Artist> createRequest(String artist) {
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("method", "artist.getInfo"));
            parameters.add(new BasicNameValuePair("artist", artist));
            return new GetInfo(parameters);
        }

        private GetInfo(List<NameValuePair> parameters) {
            super(parameters);
        }

        @Override
        Artist parseResponse(JSONObject response) {
            JSONObject artistJson = response.optJSONObject("artist");
            if (artistJson == null) {
                throw new ParseResponseError("No 'artist' field", response);
            }
            String name = artistJson.optString("name");
            if (name == null) {
                throw new ParseResponseError("no 'name' field", response);
            }
            JSONObject statsJson = artistJson.optJSONObject("stats");
            int listeners = -1;
            int playcount = -1;
            if (statsJson != null) {
                listeners = statsJson.optInt("listeners", -1);
                playcount = statsJson.optInt("playcount", -1);
            }
            List<String> tags = new ArrayList<>();
            JSONObject tagsJson = artistJson.optJSONObject("tags");
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
            Artist result = new Artist();
            result.setName(name);
            result.setListeners(listeners);
            result.setPlaycount(playcount);
            result.setTags(tags);
            return result;
        }
    }
}
