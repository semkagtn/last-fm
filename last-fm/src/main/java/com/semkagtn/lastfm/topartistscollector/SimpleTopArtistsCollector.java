package com.semkagtn.lastfm.topartistscollector;

import com.semkagtn.lastfm.utils.RequestWrapper;
import de.umass.lastfm.Artist;
import de.umass.lastfm.Period;
import de.umass.lastfm.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.semkagtn.lastfm.utils.RequestWrapper.request;

/**
 * Created by semkagtn on 2/17/15.
 */
public class SimpleTopArtistsCollector implements TopArtistsCollector {

    private final Period period;
    private final int limit;
    private final String apiKey;

    public SimpleTopArtistsCollector(Period period, int limit, String apiKey) {
        this.period = period;
        this.limit = limit;
        this.apiKey = apiKey;
    }

    @Override
    public List<Artist> collect(int userId) throws RequestWrapper.RequestException {
        Collection<Artist> result = request(User::getTopArtists, String.valueOf(userId), period, apiKey);
        return result.stream().limit(limit).collect(Collectors.toList());
    }
}
