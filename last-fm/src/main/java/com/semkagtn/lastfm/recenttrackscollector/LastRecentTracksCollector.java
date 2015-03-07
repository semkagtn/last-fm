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

    private final int limit;

    public LastRecentTracksCollector(int limit) {
        this.limit = limit;
    }

    @Override
    public List<Track> collect(int userId) {
        List<Track> result = new ArrayList<>();
        try {
            int tracksLeft = limit;
            int page = 0;
            while (tracksLeft > 0) {
                int tracksCount = Math.min(tracksLeft, REQUEST_LIMIT);
                List<Track> tracks = Api.call(
                        User.GetRecentTracks.createRequest(String.valueOf(userId), page, tracksCount));
                result.addAll(tracks);
                tracksLeft -= tracksCount;
                page += tracksCount;
            }
        } catch (Api.NotJsonInResponseException | Api.ResponseError e) {
            result = new ArrayList<>();
        }
        return result;
    }
}
