package com.semkagtn.lastfm.recenttrackscollector;

import com.semkagtn.lastfm.utils.RequestWrapper;
import de.umass.lastfm.Track;

import java.util.List;

/**
 * Created by semkagtn on 2/15/15.
 */
public interface RecentTracksCollector {

    List<Track> collect(int userId) throws RequestWrapper.RequestException;
}
