package com.semkagtn.lastfm.lastfmapi;

import com.semkagtn.lastfm.httpclient.HttpClient;
import com.semkagtn.lastfm.lastfmapi.response.ArtistGetInfoResponse;
import com.semkagtn.lastfm.lastfmapi.response.BaseLastFmResponse;
import com.semkagtn.lastfm.lastfmapi.response.TrackGetInfoResponse;
import com.semkagtn.lastfm.utils.JsonUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by semkagtn on 02.09.15.
 */
public class LastFmApi {

    private static final String API_URL = "http://ws.audioscrobbler.com/2.0/";

    private static final String ARTIST_GET_INFO = "artist.getInfo";
    private static final String TRACK_GET_INFO = "track.getInfo";

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
}
