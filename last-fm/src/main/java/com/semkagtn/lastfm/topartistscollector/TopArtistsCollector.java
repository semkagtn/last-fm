package com.semkagtn.lastfm.topartistscollector;

import com.semkagtn.lastfm.utils.RequestWrapper;
import de.umass.lastfm.Artist;

import java.util.List;

/**
 * Created by semkagtn on 2/17/15.
 */
public interface TopArtistsCollector {

    List<Artist> collect(int userId) throws RequestWrapper.RequestException;
}
