package com.semkagtn.lastfm;

import com.semkagtn.lastfm.database.Database;
import com.semkagtn.lastfm.httpclient.HttpClient;
import com.semkagtn.lastfm.httpclient.HttpClientConfig;
import com.semkagtn.lastfm.lastfmapi.LastFmApi;
import com.semkagtn.lastfm.lastfmapi.response.*;
import com.semkagtn.lastfm.utils.EntityConverter;
import com.semkagtn.lastfm.vkapi.VkApi;
import com.semkagtn.lastfm.vkapi.audioextractor.PlaylistVkAudioExtractor;
import com.semkagtn.lastfm.vkapi.audioextractor.VkAudioExtractor;
import com.semkagtn.lastfm.vkapi.response.AudioItem;
import com.semkagtn.lastfm.vkapi.response.UserItem;
import com.semkagtn.lastfm.vkapi.userwalker.PredicateVkUserWalker;
import com.semkagtn.lastfm.vkapi.userwalker.RandomRecursiveVkUserWalker;
import com.semkagtn.lastfm.vkapi.userwalker.VkUserWalker;

import java.util.List;
import java.util.stream.Collectors;

import static com.semkagtn.lastfm.vkapi.userwalker.UserPredicates.*;

/**
 * Created by semkagtn on 03.09.15.
 */
public class DataCollector {

    private static final int HTTP_CLIENT_TIMEOUT = 10_000;
    private static final int HTTP_CLIENT_MAX_REPEAT_TIMES = 2;
    private static final boolean HTTP_CLIENT_LOGGER_ENABLED = false;

    private static final int USER_WALKER_DEPTH = 0;
    private static final int USER_WALKER_FRIENDS_LIMIT = 4;

    private static final int AUDIOS_REQUEST_LIMIT = 50;

    private static final int USER_AMOUNT = 2;
    private static final int MINIMUM_AUDIOS = 50;

    private LastFmApi lastFmApi;
    private VkUserWalker userWalker;
    private VkAudioExtractor audioExtractor;

    public DataCollector(String lastFmApiKey, String vkAccessToken) {
        HttpClientConfig config = HttpClientConfig
                .newInstance()
                .withTimeout(HTTP_CLIENT_TIMEOUT)
                .withMaxRepeatTimes(HTTP_CLIENT_MAX_REPEAT_TIMES)
                .withLoggerEnabled(HTTP_CLIENT_LOGGER_ENABLED)
                .build();
        HttpClient httpClient = new HttpClient(config);

        lastFmApi = new LastFmApi(httpClient, lastFmApiKey);
        VkApi vkApi = new VkApi(httpClient, vkAccessToken);

        userWalker = new PredicateVkUserWalker(
                new RandomRecursiveVkUserWalker(USER_WALKER_DEPTH, USER_WALKER_FRIENDS_LIMIT, vkApi),
                minimumAudios(MINIMUM_AUDIOS).and(hasAge().or(hasGender())));
        audioExtractor = new PlaylistVkAudioExtractor(vkApi, AUDIOS_REQUEST_LIMIT);
    }

    public void collect() {
        for (int i = 0; i < USER_AMOUNT;) {
            Users user = collectUser();
            if (user != null) {
                i++;
            }
        }
    }

    private Users collectUser() {
        UserItem user = userWalker.nextUser();
        List<AudioItem> audios = audioExtractor.getAudios(user.getId());
        if (audios == null) {
            return null;
        }
        Users userEntity = EntityConverter.convertUser(user);
        boolean inserted = Database.insert(userEntity);
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
        Tracks trackEntity = EntityConverter.convertTrack(track);
        Artists artistEntity = null;
        if (track.getArtist() != null) {
            artistEntity = collectArtist(track.getArtist());
        }
        trackEntity.setArtists(artistEntity);
        Database.insert(trackEntity);
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
        boolean artistInserted = Database.insert(artistEntity);
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

    private static List<Tags> collectTags(List<TagItem> tagItems) {
        List<Tags> tagEntities = tagItems.stream().map(EntityConverter::convertTag).collect(Collectors.toList());
        tagEntities.forEach(Database::insert);
        return tagEntities;
    }

    private static void insertUserTrack(Users user, Tracks track, long addedWhen) {
        UsersTracksId id = new UsersTracksId();
        id.setUserId(user.getId());
        id.setTrackId(track.getId());

        UsersTracks usersTracks = new UsersTracks();
        usersTracks.setId(id);
        usersTracks.setUsers(user);
        usersTracks.setTracks(track);
        usersTracks.setAddedWhen(addedWhen);
        Database.insert(usersTracks);
    }

    private static void insertArtistTags(Artists artist, List<Tags> tags) {
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
            Database.insert(artistsTags);
        }
    }

    private static void insertTrackTags(Tracks track, List<Tags> tags) {
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
            Database.insert(trackTags);
        }
    }
}
