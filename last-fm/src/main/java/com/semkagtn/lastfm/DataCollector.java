package com.semkagtn.lastfm;

import com.semkagtn.lastfm.api.*;
import com.semkagtn.lastfm.database.Database;
import com.semkagtn.lastfm.recenttrackscollector.LastRecentTracksCollector;
import com.semkagtn.lastfm.recenttrackscollector.RecentTracksCollector;
import com.semkagtn.lastfm.userwalker.RandomRecursiveUserWalker;
import com.semkagtn.lastfm.userwalker.UserWalker;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 2/14/15.
 */
public class DataCollector /*implements Runnable*/ {
//
//    private static final int USER_WALKER_DEPTH = 2;
//    private static final int FRIENDS_LIMIT = 5;
//    private static final int ARTIST_TAGS_LIMIT = 5;
//    private static final int TRACK_TAGS_LIMIT = 5;
//    private static final int RECENT_TRACKS_LIMIT = 1000;
//
//    private ApiConfig apiConfig;
//    private int userCount;
//
//    public DataCollector(ApiConfig apiConfig, int userCount) {
//        this.apiConfig = apiConfig;
//        this.userCount = userCount;
//    }
//
//    @Override
//    public void run() {
//        Api api = new Api(apiConfig);
//        UserWalker userWalker = new RandomRecursiveUserWalker(USER_WALKER_DEPTH, FRIENDS_LIMIT, api);
//        RecentTracksCollector recentTracksCollector = new LastRecentTracksCollector(RECENT_TRACKS_LIMIT, api);
//
//        for (int i = 0; i < userCount; i++) {
//            User user = userWalker.nextUser();
//            if (!isInformative(user)) {
//                i--;
//                continue; // Uninformative user
//            }
//            List<Track> recentTracks = recentTracksCollector.collect(user.getId());
//            if (recentTracks.size() == 0) {
//                i--;
//                continue; // Can't get user's recent tracks
//            }
//
//            String gender = (user.getGender().equals("m") || user.getGender().equals("f")) ? user.getGender() : "n";
//            Users userEntity = new Users(user.getId(), user.getAge(), gender, user.getCountry(), user.getPlaycount());
//            if (!Database.insert(userEntity)) {
//                i--;
//                continue; // User already exists
//            }
//
//            for (Track recentTrack : recentTracks) {
//                Artists artistEntity = null;
//                if (!recentTrack.getArtist().equals("")) {
//                    artistEntity = insertArtistWithTags(recentTrack.getArtist(), api);
//                }
//
//                Tracks trackEntity = insertTrackWithTags(artistEntity, recentTrack.getArtist(), recentTrack.getName(), api);
//                if (trackEntity != null) {
//                    RecentTracks recentTrackEntity =
//                            new RecentTracks(trackEntity, userEntity, recentTrack.getPlayedWhen());
//                    Database.insert(recentTrackEntity);
//                }
//            }
//        }
//    }
//
//    private static boolean isInformative(User user) {
//        return (user.getGender().equals("m") || user.getGender().equals("f") ||
//                user.getAge() > 0) && user.getPlaycount() >= RECENT_TRACKS_LIMIT;
//    }
//
//    private static Artists insertArtistWithTags(String artistName, Api api) {
//        Request<Artist> artistRequest = Artist.GetInfo.createRequest(artistName);
//        Artists artistEntity = Database.select(Artists.class, artistRequest.hash());
//        if (artistEntity == null) {
//            try {
//                Artist artist = api.call(artistRequest);
//                artistEntity = new Artists(artistRequest.hash(), artist.getName(),
//                        artist.getListeners(), artist.getPlaycount());
//                if (Database.insert(artistEntity)) {
//                    List<String> tagNames = artist.getTags().stream()
//                            .limit(ARTIST_TAGS_LIMIT)
//                            .collect(Collectors.toList());
//                    for (int j = 0; j < tagNames.size(); j++) {
//                        String tagName = artist.getTags().get(j);
//                        Tags tagEntity = insertTag(tagName, api);
//                        ArtistsTags artistTagEntity = new ArtistsTags(tagEntity, artistEntity, j + 1);
//                        Database.insert(artistTagEntity);
//                    }
//                }
//            } catch (Api.ResponseError | Api.NotJsonInResponseError e) {
//                // No info about artist (not exactly)
//            }
//        }
//        return artistEntity;
//    }
//
//    private static Tracks insertTrackWithTags(Artists artistEntity, String artistName, String trackName, Api api) {
//        Request<Track> trackRequest = Track.GetInfo.createRequest(artistName, trackName);
//        Tracks trackEntity = Database.select(Tracks.class, trackRequest.hash());
//        if (trackEntity == null) {
//            try {
//                Track track = api.call(trackRequest);
//                trackEntity = new Tracks(trackRequest.hash(), track.getName(),
//                        track.getDuration(), track.getListeners(), track.getPlaycount());
//                trackEntity.setArtists(artistEntity);
//                if (Database.insert(trackEntity)) {
//                    List<String> tagNames = track.getTags().stream()
//                            .limit(TRACK_TAGS_LIMIT)
//                            .collect(Collectors.toList());
//                    for (int j = 0; j < tagNames.size(); j++) {
//                        String tagName = track.getTags().get(j);
//                        Tags tagEntity = insertTag(tagName, api);
//                        TracksTags trackTagEntity = new TracksTags(tagEntity, trackEntity, j + 1);
//                        Database.insert(trackTagEntity);
//                    }
//                }
//            } catch (Api.ResponseError | Api.NotJsonInResponseError e) {
//                // No info about track. (Not exactly)
//            }
//        }
//        return trackEntity;
//    }
//
//    private static Tags insertTag(String tagName, Api api) {
//        Request<Tag> tagRequest = Tag.GetInfo.createRequest(tagName);
//        Tags tagEntity = Database.select(Tags.class, tagRequest.hash());
//        if (tagEntity == null) {
//            Tag tag;
//            try {
//                tag = api.call(tagRequest);
//                tagEntity = new Tags(
//                        tagRequest.hash(), tag.getName(), tag.getReach(), tag.getTaggings());
//            } catch (Api.NotJsonInResponseError | Api.ResponseError e) {
//                tagEntity = new Tags(tagRequest.hash(), tagName, -1, -1);
//            }
//            Database.insert(tagEntity);
//        }
//        return tagEntity;
//    }
}
