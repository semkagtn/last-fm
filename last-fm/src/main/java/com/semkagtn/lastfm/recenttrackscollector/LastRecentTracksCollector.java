package com.semkagtn.lastfm.recenttrackscollector;

import com.semkagtn.lastfm.utils.RequestWrapper;
import de.umass.lastfm.Track;
import de.umass.lastfm.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.semkagtn.lastfm.utils.RequestWrapper.request;

/**
 * Created by semkagtn on 2/15/15.
 */
public class LastRecentTracksCollector implements RecentTracksCollector {

    private static final int REQUEST_LIMIT = 200;

    private final int limit;
    private final String apiKey;

    public LastRecentTracksCollector(int limit, String apiKey) {
        this.limit = limit;
        this.apiKey = apiKey;
    }

    @Override
    public List<Track> collect(int userId) throws RequestWrapper.RequestException {
        List<Track> result = new ArrayList<>();
        int tracksLeft = limit;
        int page = 0;
        while (tracksLeft > 0) {
            int tracksCount = Math.min(tracksLeft, REQUEST_LIMIT);
            Collection<Track> tracks =
                    request(User::getRecentTracks, String.valueOf(userId), page, tracksCount, apiKey).getPageResults();
            result.addAll(tracks);
            tracksLeft -= tracksCount;
            page += tracksCount;
        }
        return result;
    }
}
