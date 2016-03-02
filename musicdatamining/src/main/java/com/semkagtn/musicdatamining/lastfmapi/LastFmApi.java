package com.semkagtn.musicdatamining.lastfmapi;

import com.semkagtn.musicdatamining.httpclient.HttpClient;
import com.semkagtn.musicdatamining.lastfmapi.model.response.*;
import com.semkagtn.musicdatamining.utils.JsonUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by semkagtn on 02.09.15.
 */
public class LastFmApi {

    private static final String API_URL = "http://ws.audioscrobbler.com/2.0/";

    private static final String ARTIST_GET_INFO = "artist.getInfo";
    private static final String TRACK_GET_INFO = "track.getInfo";
    private static final String USER_GET_INFO = "user.getInfo";
    private static final String USER_GET_TOP_TRACKS = "user.getTopTracks";
    private static final String USER_GET_TOP_ARTISTS = "user.getTopArtists";
    private static final String USER_GET_FRIENDS = "user.getFriends";

    private HttpClient client;
    private String apiKey;

    public LastFmApi(HttpClient client, String apiKey) {
        this.client = client;
        this.apiKey = apiKey;
    }

    private <T extends BaseLastFmResponse> T call(String method, List<NameValuePair> parameters, Class<T> resultClass) {
        parameters.add(new BasicNameValuePair("api_key", apiKey));
        parameters.add(new BasicNameValuePair("method", method));
        parameters.add(new BasicNameValuePair("format", "json"));

        T result = null;
        boolean resultReceived = false;
        while (!resultReceived) {
            String response = client.request(API_URL, parameters);
            result = JsonUtils.fromJson(response, resultClass);
            resultReceived = result != null && (result.getError() == null
                    || result.getError() != LastFmApiErrors.RATE_LIMIT_EXCEEDED);
        }
        return result;
    }

    public ArtistGetInfoResponse artistGetInfo(String artist) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("artist", artist));
        return call(ARTIST_GET_INFO, parameters, ArtistGetInfoResponse.class);
    }

    public TrackGetInfoResponse trackGetInfo(String track, String artist) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("track", track));
        parameters.add(new BasicNameValuePair("artist", artist));
        parameters.add(new BasicNameValuePair("autocorrect", "1"));
        return call(TRACK_GET_INFO, parameters, TrackGetInfoResponse.class);
    }

    public UserGetInfoResponse userGetInfo(String user) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("user", user));
        return call(USER_GET_INFO, parameters, UserGetInfoResponse.class);
    }

    public UserGetTopTracksResponse userGetTopTracks(String user) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("user", user));
        return call(USER_GET_TOP_TRACKS, parameters, UserGetTopTracksResponse.class);
    }

    public UserGetTopArtistsResponse userGetTopArtists(String user) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("user", user));
        return call(USER_GET_TOP_ARTISTS, parameters, UserGetTopArtistsResponse.class);
    }

    public UserGetFriendsResponse userGetFriends(String user) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("user", user));
        return call(USER_GET_FRIENDS, parameters, UserGetFriendsResponse.class);
    }
}
