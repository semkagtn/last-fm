package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.database.DatabaseWriterHelper;
import com.semkagtn.musicdatamining.database.EntityConverter;
import com.semkagtn.musicdatamining.httpclient.HttpClient;
import com.semkagtn.musicdatamining.httpclient.HttpClientConfig;
import com.semkagtn.musicdatamining.lastfmapi.LastFmApi;
import com.semkagtn.musicdatamining.lastfmapi.response.*;
import com.semkagtn.musicdatamining.vkapi.VkApi;
import com.semkagtn.musicdatamining.vkapi.audioextractor.CompositeVkAudioExtractor;
import com.semkagtn.musicdatamining.vkapi.audioextractor.PlaylistVkAudioExtractor;
import com.semkagtn.musicdatamining.vkapi.audioextractor.VkAudioExtractor;
import com.semkagtn.musicdatamining.vkapi.audioextractor.WallVkAudioExtractor;
import com.semkagtn.musicdatamining.vkapi.response.AudioItem;
import com.semkagtn.musicdatamining.vkapi.response.UserItem;
import com.semkagtn.musicdatamining.vkapi.userwalker.PredicateVkUserWalker;
import com.semkagtn.musicdatamining.vkapi.userwalker.RandomRecursiveVkUserWalker;
import com.semkagtn.musicdatamining.vkapi.userwalker.VkUserWalker;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.semkagtn.musicdatamining.vkapi.userwalker.UserPredicates.*;

/**
 * Created by semkagtn on 03.09.15.
 */
public class DataCollector {

    private static final int USER_AMOUNT = 50;
    private static final int AUDIOS_REQUEST_LIMIT = 250;
    private static final int MINIMUM_AUDIOS = 100;

    private static final int HTTP_CLIENT_TIMEOUT = 30_000;
    private static final int HTTP_CLIENT_MAX_REPEAT_TIMES = 3;
    private static final boolean HTTP_CLIENT_LOGGER_ENABLED = true;

    private static final int USER_WALKER_DEPTH = 2;
    private static final int USER_WALKER_FRIENDS_LIMIT = 3;

    private LastFmApi lastFmApi;
    private VkUserWalker userWalker;
    private VkAudioExtractor audioExtractor;
    private DatabaseWriterHelper databaseWriterHelper;

    public DataCollector(String lastFmApiKey, String vkAccessToken, Logger logger) {
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

        lastFmApi = new LastFmApi(httpClient, lastFmApiKey);
        VkApi vkApi = new VkApi(httpClient, vkAccessToken);

        userWalker = new PredicateVkUserWalker(
                new RandomRecursiveVkUserWalker(USER_WALKER_DEPTH, USER_WALKER_FRIENDS_LIMIT, vkApi),
                hasAvatar().and(hasAge().or(hasGender())));

        VkAudioExtractor playlistAudioExtractor = new PlaylistVkAudioExtractor(vkApi);
        VkAudioExtractor wallAudioExtractor = new WallVkAudioExtractor(vkApi);
        audioExtractor = new CompositeVkAudioExtractor(
                Arrays.asList(playlistAudioExtractor, wallAudioExtractor), AUDIOS_REQUEST_LIMIT);

        databaseWriterHelper = new DatabaseWriterHelper();
    }

    public DataCollector(String lastFmApiKey, String vkAccessToken) {
        this(lastFmApiKey, vkAccessToken, null);
    }

    public void collect() {
        try {
            for (int i = 0; i < USER_AMOUNT; ) {
                Users user = collectUser();
                if (user != null) {
                    i++;
                }
            }
        } finally {
            databaseWriterHelper.close();
        }
    }

    private Users collectUser() {
        UserItem user = userWalker.nextUser();
        List<AudioItem> audios = audioExtractor.getAudios(user.getId());
        if (audios == null || audios.size() < MINIMUM_AUDIOS) {
            return null;
        }
        Users userEntity = EntityConverter.convertUser(user);
        boolean inserted = databaseWriterHelper.insert(userEntity);
        if (!inserted) {
            return null;
        }
        for (AudioItem audio : audios) {
            Tracks trackEntity = collectAudio(audio);
            if (trackEntity != null) {
                insertUserTrack(userEntity, trackEntity, audio.getDate());
            }
        }
        return userEntity;
    }

    private Tracks collectAudio(AudioItem audio) {
        TrackGetInfoResponse trackGetInfoResponse = lastFmApi.trackGetInfo(audio.getTitle(), audio.getArtist());
        if (trackGetInfoResponse.getTrack() == null) {
            return null;
        }
        TrackItem track = trackGetInfoResponse.getTrack();
        Tracks trackEntity = EntityConverter.convertTrack(track, audio.getGenreId());
        Artists artistEntity = null;
        if (track.getArtist() != null) {
            artistEntity = collectArtist(track.getArtist());
        }
        trackEntity.setArtists(artistEntity);
        databaseWriterHelper.insert(trackEntity);
        if (track.getTopTags() == null || track.getTopTags().getTag() == null) {
            return trackEntity;
        }
        List<TagItem> tags = track.getTopTags().getTag();
        List<Tags> tagEntities = collectTags(tags);
        insertTrackTags(trackEntity, tagEntities);
        return trackEntity;
    }

    private Artists collectArtist(ArtistItem artist) {
        Artists artistEntity = EntityConverter.convertArtist(artist);
        boolean artistInserted = databaseWriterHelper.insert(artistEntity);
        if (!artistInserted) {
            return artistEntity;
        }
        ArtistGetInfoResponse artistGetInfoResponse = lastFmApi.artistGetInfo(artist.getName());
        if (artistGetInfoResponse.getArtist() == null
                || artistGetInfoResponse.getArtist().getTags() == null
                || artistGetInfoResponse.getArtist().getTags().getTag() == null) {
            return artistEntity;
        }
        List<TagItem> tags = artistGetInfoResponse.getArtist().getTags().getTag();
        List<Tags> tagEntities = collectTags(tags);
        insertArtistTags(artistEntity, tagEntities);
        return artistEntity;
    }

    private List<Tags> collectTags(List<TagItem> tagItems) {
        List<Tags> tagEntities = tagItems.stream().map(EntityConverter::convertTag).collect(Collectors.toList());
        tagEntities.forEach(databaseWriterHelper::insert);
        return tagEntities;
    }

    private void insertUserTrack(Users user, Tracks track, long addedWhen) {
        UsersTracksId id = new UsersTracksId();
        id.setUserId(user.getId());
        id.setTrackId(track.getId());

        UsersTracks usersTracks = new UsersTracks();
        usersTracks.setId(id);
        usersTracks.setUsers(user);
        usersTracks.setTracks(track);
        usersTracks.setAddedWhen(addedWhen);
        databaseWriterHelper.insert(usersTracks);
    }

    private void insertArtistTags(Artists artist, List<Tags> tags) {
        for (int i = 0; i < tags.size(); i++) {
            Tags tagEntity = tags.get(i);

            ArtistsTagsId id = new ArtistsTagsId();
            id.setArtistId(artist.getId());
            id.setTagId(tagEntity.getId());

            ArtistsTags artistsTags = new ArtistsTags();
            artistsTags.setId(id);
            artistsTags.setTags(tagEntity);
            artistsTags.setArtists(artist);
            artistsTags.setPosition(i + 1);
            databaseWriterHelper.insert(artistsTags);
        }
    }

    private void insertTrackTags(Tracks track, List<Tags> tags) {
        for (int i = 0; i < tags.size(); i++) {
            Tags tagEntity = tags.get(i);

            TracksTagsId id = new TracksTagsId();
            id.setTrackId(track.getId());
            id.setTagId(tagEntity.getId());

            TracksTags trackTags = new TracksTags();
            trackTags.setId(id);
            trackTags.setTags(tagEntity);
            trackTags.setTracks(track);
            trackTags.setPosition(i + 1);
            databaseWriterHelper.insert(trackTags);
        }
    }
}
