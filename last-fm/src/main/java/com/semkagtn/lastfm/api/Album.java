package com.semkagtn.lastfm.api;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by semkagtn on 29.08.15.
 */
public class Album {

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

    public void setName(String name) {
        this.name = name;
    }

    public void setListeners(int listeners) {
        this.listeners = listeners;
    }

    public void setPlaycount(int playcount) {
        this.playcount = playcount;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    Album() {

    }

    public static class GetInfo extends Request<Album> {

        public static Request<Album> createRequest(String artist, String album) {
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("method", "album.getInfo"));
            parameters.add(new BasicNameValuePair("artist", artist));
            parameters.add(new BasicNameValuePair("album", album));
            return new GetInfo(parameters);
        }

        private GetInfo(List<NameValuePair> parameters) {
            super(parameters);
        }

        @Override
        Album parseResponse(JSONObject response) {
            JSONObject albumJson = response.optJSONObject("album");
            if (albumJson == null) {
                throw new ParseResponseError("No 'album' field", response);
            }
            String name = albumJson.optString("name");
            if (name == null) {
                throw new ParseResponseError("No 'name' field", response);
            }
            int listeners = albumJson.optInt("listeners", -1);
            int playcount = albumJson.optInt("playcount", -1);
            List<String> tags = new ArrayList<>();
            JSONObject tagsJson = albumJson.optJSONObject("tags");
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
            Album result = new Album();
            result.setName(name);
            result.setListeners(listeners);
            result.setPlaycount(playcount);
            result.setTags(tags);
            return result;
        }
    }
}
