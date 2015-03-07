package com.semkagtn.lastfm.recenttrackscollector;

import com.semkagtn.lastfm.api.Track;

import java.util.List;

/**
 * Created by semkagtn on 2/15/15.
 */
public interface RecentTracksCollector {

    List<Track> collect(int userId);
}
