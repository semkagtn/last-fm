package com.semkagtn.lastfm.recenttrackscollector;

import com.semkagtn.lastfm.api.Api;
import com.semkagtn.lastfm.api.Track;
import com.semkagtn.lastfm.api.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by semkagtn on 2/15/15.
 */
public class LastRecentTracksCollector implements RecentTracksCollector {

    private static final int REQUEST_LIMIT = 200;

    private int limit;
    private Api api;

    public LastRecentTracksCollector(int limit, Api api) {
        this.limit = limit;
        this.api = api;
    }

    @Override
    public List<Track> collect(int userId) {
        List<Track> result = new ArrayList<>();
        try {
            int tracksLeft = limit;
            int page = 1;
            while (tracksLeft > 0) {
                int tracksCount = Math.min(tracksLeft, REQUEST_LIMIT);
                List<Track> tracks = api.call(
                        User.GetRecentTracks.createRequest(String.valueOf(userId), page, tracksCount));
                result.addAll(tracks);
                tracksLeft -= tracks.size();
                page += tracks.size();
            }
        } catch (Api.NotJsonInResponseError | Api.ResponseError e) {
            result = new ArrayList<>();
        }
        return result;
    }
}
