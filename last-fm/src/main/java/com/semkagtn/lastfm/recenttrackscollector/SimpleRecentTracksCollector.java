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
public class SimpleRecentTracksCollector implements RecentTracksCollector {

    private final int limit;
    private final String apiKey;

    public SimpleRecentTracksCollector(int limit, String apiKey) {
        this.limit = limit;
        this.apiKey = apiKey;
    }

    @Override
    public List<Track> collect(int userId) throws RequestWrapper.RequestException {
        Collection<Track> tracks =
                request(User::getRecentTracks, String.valueOf(userId), 0, limit, apiKey).getPageResults();
        return new ArrayList<>(tracks);
    }
}
