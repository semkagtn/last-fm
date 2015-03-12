package com.semkagtn.lastfm.api;

import com.semkagtn.lastfm.utils.Utils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by semkagtn on 3/6/15.
 */
public class User {

    private int id;
    private int age;
    private String gender;
    private String country;
    private int playcount;

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getCountry() {
        return country;
    }

    public int getPlaycount() {
        return playcount;
    }

    void setId(int id) {
        this.id = id;
    }

    void setAge(int age) {
        this.age = age;
    }

    void setGender(String gender) {
        this.gender = gender;
    }

    void setCountry(String country) {
        this.country = country;
    }

    void setPlaycount(int playcount) {
        this.playcount = playcount;
    }

    User() {

    }

    private static User parseUserJson(JSONObject userJson) {
        int id = userJson.optInt("id", -1);
        if (id == -1) {
            throw new Request.ParseResponseError("no 'id' field", userJson);
        }
        User result = new User();
        result.setId(id);
        result.setAge(userJson.optInt("age", -1));
        result.setCountry(userJson.optString("country", ""));
        result.setGender(userJson.optString("gender", "n"));
        result.setPlaycount(userJson.optInt("playcount", -1));
        return result;
    }

    public static class GetInfo extends Request<User> {

        public static Request<User> createRequest(String user) {
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("method", "user.getInfo"));
            parameters.add(new BasicNameValuePair("user", user));
            return new GetInfo(parameters);
        }

        private GetInfo(List<NameValuePair> parameters) {
            super(parameters);
        }

        @Override
        User parseResponse(JSONObject response) {
            JSONObject userJson = response.optJSONObject("user");
            if (userJson == null) {
                throw new ParseResponseError("no 'user' field", response);
            }
            return parseUserJson(userJson);
        }
    }

    public static class GetFriends extends Request<List<User>> {

        public static Request<List<User>> createRequest(String user, int page, int limit) {
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("method", "user.getFriends"));
            parameters.add(new BasicNameValuePair("user", user));
            parameters.add(new BasicNameValuePair("page", String.valueOf(page)));
            parameters.add(new BasicNameValuePair("limit", String.valueOf(limit)));
            return new GetFriends(parameters);
        }

        private GetFriends(List<NameValuePair> parameters) {
            super(parameters);
        }

        @Override
        List<User> parseResponse(JSONObject response) {
            JSONObject friendsJson = response.optJSONObject("friends");
            if (friendsJson == null) {
                throw new ParseResponseError("no 'friends' field", response);
            }
            JSONArray usersJson = friendsJson.optJSONArray("user");
            List<User> result = new ArrayList<>();
            if (usersJson != null) {
                for (int i = 0; i < usersJson.length(); i++) {
                    JSONObject userJson = usersJson.getJSONObject(i);
                    result.add(parseUserJson(userJson));
                }
            } else {
                JSONObject userJson = friendsJson.optJSONObject("user");
                if (userJson != null) {
                    result.add(parseUserJson(userJson));
                }
            }
            return result;
        }
    }

    public static class GetRecentTracks extends Request<List<Track>> {

        public static Request<List<Track>> createRequest(String user, int page, int limit) {
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("method", "user.getRecentTracks"));
            parameters.add(new BasicNameValuePair("user", user));
            parameters.add(new BasicNameValuePair("page", String.valueOf(page)));
            parameters.add(new BasicNameValuePair("limit", String.valueOf(limit)));
            return new GetRecentTracks(parameters);
        }

        private GetRecentTracks(List<NameValuePair> parameters) {
            super(parameters);
        }

        @Override
        List<Track> parseResponse(JSONObject response) {
            JSONObject recentTracksJson = response.optJSONObject("recenttracks");
            if (recentTracksJson == null) {
                throw new ParseResponseError("no 'recenttracks' field", response);
            }
            JSONArray tracksArrayJson = recentTracksJson.optJSONArray("track");
            List<Track> tracks = new ArrayList<>();
            if (tracksArrayJson != null) {
                for (int i = 0; i < tracksArrayJson.length(); i++) {
                    JSONObject trackJson = tracksArrayJson.getJSONObject(i);
                    Track track = parseTrackJson(trackJson, response);
                    if (track != null) {
                        tracks.add(track);
                    }
                }
            } else {
                JSONObject trackJson = recentTracksJson.optJSONObject("track");
                Track track = parseTrackJson(trackJson, recentTracksJson);
                if (track != null) {
                    tracks.add(track);
                }
            }
            return tracks;
        }

        private static Track parseTrackJson(JSONObject trackJson, JSONObject response) {
            if (trackJson == null) {
                return null;
            }
            String name = trackJson.optString("name");
            if (name == null) {
                throw new ParseResponseError("no 'name' field", response);
            }
            Track track = new Track();
            track.setName(name);
            track.setArtist("");
            JSONObject artistJson = trackJson.optJSONObject("artist");
            if (artistJson != null) {
                track.setArtist(artistJson.optString("#text", ""));
            }
            JSONObject dateJson = trackJson.optJSONObject("date");
            long playedWhen;
            if (dateJson != null) {
                playedWhen = dateJson.optLong("uts", -1) * 1000;
                if (playedWhen < 0) {
                    throw new ParseResponseError("no 'uts' field", response);
                }
                track.setDuration(-1);
                track.setListeners(-1);
                track.setPlaycount(-1);
                track.setTags(new ArrayList<>());
                track.setPlayedWhen(Utils.dateToString(new Date(playedWhen)));
                return track;
            }
            return null;
        }
    }
}
