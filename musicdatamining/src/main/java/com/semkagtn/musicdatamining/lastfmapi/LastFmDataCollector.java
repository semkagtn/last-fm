package com.semkagtn.musicdatamining.lastfmapi;

import com.semkagtn.musicdatamining.LastfmArtists;
import com.semkagtn.musicdatamining.LastfmTopTracks;
import com.semkagtn.musicdatamining.LastfmTracks;
import com.semkagtn.musicdatamining.LastfmUsers;
import com.semkagtn.musicdatamining.database.DatabaseWriterHelper;
import com.semkagtn.musicdatamining.database.EntityConverter;
import com.semkagtn.musicdatamining.httpclient.HttpClient;
import com.semkagtn.musicdatamining.httpclient.HttpClientConfig;
import com.semkagtn.musicdatamining.lastfmapi.model.item.ArtistItem;
import com.semkagtn.musicdatamining.lastfmapi.model.item.LastFmUserItem;
import com.semkagtn.musicdatamining.lastfmapi.model.item.TrackItem;
import com.semkagtn.musicdatamining.lastfmapi.model.response.UserGetTopTracksResponse;
import com.semkagtn.musicdatamining.lastfmapi.userwalker.FriendsLastFmUserWalker;
import com.semkagtn.musicdatamining.lastfmapi.userwalker.LastFmUserWalker;
import com.semkagtn.musicdatamining.lastfmapi.userwalker.PredicateLastFmUserWalker;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.semkagtn.musicdatamining.lastfmapi.userwalker.LastFmUserPredicates.hasGender;

/**
 * Created by semkagtn on 09.02.16.
 */
public class LastFmDataCollector {

    private static final int USER_AMOUNT = 5_000;

    private static final int HTTP_CLIENT_TIMEOUT = 30_000;
    private static final int HTTP_CLIENT_MAX_REPEAT_TIMES = 3;
    private static final boolean HTTP_CLIENT_LOGGER_ENABLED = true;

    private LastFmApi api;
    private LastFmUserWalker userWalker;
    private DatabaseWriterHelper database;

    public LastFmDataCollector(String apiKey, String initialUser, Logger logger) {
        HttpClientConfig config = HttpClientConfig
                .newInstance()
                .withTimeout(HTTP_CLIENT_TIMEOUT)
                .withMaxRepeatTimes(HTTP_CLIENT_MAX_REPEAT_TIMES)
                .withLoggerEnabled(HTTP_CLIENT_LOGGER_ENABLED)
                .build();
        HttpClient httpClient = new HttpClient(config);
        if (logger != null) {
            httpClient.setLogger(logger);
        }

        api = new LastFmApi(httpClient, apiKey);
        userWalker = new PredicateLastFmUserWalker(
                new FriendsLastFmUserWalker(api, initialUser),
                hasGender());
        database = new DatabaseWriterHelper();
    }

    public LastFmDataCollector(String apiKey, String initialUser) {
        this(apiKey, initialUser, null);
    }

    public void collect() {
        try {
            for (int i = 0; i < USER_AMOUNT; ) {
                boolean inserted = collectUser();
                if (inserted) {
                    i++;
                }
            }
        } finally {
            database.close();
        }
    }

    private boolean collectUser() {
        LastFmUserItem user = userWalker.nextUser();
        LastfmUsers userEntity = EntityConverter.convertLastFmUser(user);
        boolean inserted = database.insert(userEntity);
        if (!inserted) {
            return false;
        }
        collectTopTracks(userEntity, user.getName());
        return true;
    }

    private void collectTopTracks(LastfmUsers userEntity, String userName) {
        UserGetTopTracksResponse response = api.userGetTopTracks(userName);
        if (!isValidTopTracks(response)) {
            return;
        }
        List<TrackItem> tracks = response.getTracks().getTracks().stream()
                .filter(track -> track != null)
                .filter(track -> track.getPlaycount() != null)
                .collect(Collectors.toList());
        for (TrackItem track : tracks) {
            LastfmTracks trackEntity = EntityConverter.convertLastFmTrack(track);
            ArtistItem artist = track.getArtist();
            if (artist != null) {
                LastfmArtists artistEntity = EntityConverter.convertLastFmArtist(artist);
                database.insert(artistEntity);
                trackEntity.setLastfmArtists(artistEntity);
            }
            database.insert(trackEntity);
            LastfmTopTracks topTrackEntity = new LastfmTopTracks();
            topTrackEntity.setLastfmTracks(trackEntity);
            topTrackEntity.setLastfmUsers(userEntity);
            topTrackEntity.setPlaycount(track.getPlaycount());
            database.insert(topTrackEntity);
        }
    }

    private boolean isValidTopTracks(UserGetTopTracksResponse response) {
        return response.getTracks() != null &&
                response.getTracks().getTracks() != null;
    }
}
