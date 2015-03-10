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
public class CollectData {

    private static final int DEFAULT_USERS_AMOUNT = 10;

    private static final int USER_WALKER_DEPTH = 2;
    private static final int FRIENDS_LIMIT = 5;
    private static final int ARTIST_TAGS_LIMIT = 5;
    private static final int TRACK_TAGS_LIMIT = 5;
    private static final int RECENT_TRACKS_LIMIT = 50;

    public static void main(String[] args) {
        String apiKey = args.length > 0 ? ApiKeys.get(Integer.parseInt(args[0])) : ApiKeys.getRandom();
        int usersAmount = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_USERS_AMOUNT;

        Api.setApiKey(apiKey);
        Api.setRequestRepeats(5);
        Api.setTimeout(20_000);
        Api.setUserAgent("tst");
        Api.enableLogger(true);

        UserWalker userWalker = new RandomRecursiveUserWalker(USER_WALKER_DEPTH, FRIENDS_LIMIT);
        RecentTracksCollector recentTracksCollector = new LastRecentTracksCollector(RECENT_TRACKS_LIMIT);

        Database.open();
        for (int i = 0; i < usersAmount; i++) {
            User user = userWalker.nextUser();
            List<Track> recentTracks = recentTracksCollector.collect(user.getId());
            if (recentTracks.size() == 0) {
                i--;
                continue; // Can't get user's recent tracks
            }
            Users userEntity = insertUserIfItInformative(user);
            if (userEntity == null) {
                i--;
                continue; // Uninformative user or it already exists
            }

            for (Track recentTrack : recentTracks) {
                Artists artistEntity = null;
                if (!recentTrack.getArtist().equals("")) {
                    artistEntity = insertArtistWithTags(recentTrack.getArtist());
                }

                Tracks trackEntity = insertTrackWithTags(artistEntity, recentTrack.getArtist(), recentTrack.getName());
                if (trackEntity != null) {
                    RecentTracks recentTrackEntity =
                            new RecentTracks(trackEntity, userEntity, recentTrack.getPlayedWhen());
                    Database.insert(recentTrackEntity);
                }
            }
        }
        Database.close();
    }

    private static Users insertUserIfItInformative(User user) {
        String gender = (user.getGender().equals("m") || user.getGender().equals("f")) ? user.getGender() : "n";
        Users userEntity = new Users(user.getId(), user.getAge(), gender, user.getCountry(), user.getPlaycount());
        if (user.getPlaycount() >= RECENT_TRACKS_LIMIT &&
                (!gender.equals("n") || user.getAge() > 0) &&
                Database.insert(userEntity)) {
            return userEntity;
        }
        return null;
    }

    private static Artists insertArtistWithTags(String artistName) {
        Request<Artist> artistRequest = Artist.GetInfo.createRequest(artistName);
        Artists artistEntity = Database.select(Artists.class, artistRequest.hash());
        if (artistEntity == null) {
            try {
                Artist artist = Api.call(artistRequest);
                artistEntity = new Artists(artistRequest.hash(), artist.getName(),
                        artist.getListeners(), artist.getPlaycount());
                if (Database.insert(artistEntity)) {
                    List<String> tagNames = artist.getTags().stream()
                            .limit(ARTIST_TAGS_LIMIT)
                            .collect(Collectors.toList());
                    for (int j = 0; j < tagNames.size(); j++) {
                        String tagName = artist.getTags().get(j);
                        Tags tagEntity = insertTag(tagName);
                        ArtistsTags artistTagEntity = new ArtistsTags(tagEntity, artistEntity, (byte) (j + 1));
                        Database.insert(artistTagEntity);
                    }
                }
            } catch (Api.ResponseError | Api.NotJsonInResponseException e) {
                // No info about artist (not exactly)
            }
        }
        return artistEntity;
    }

    private static Tracks insertTrackWithTags(Artists artistEntity, String artistName, String trackName) {
        Request<Track> trackRequest = Track.GetInfo.createRequest(artistName, trackName);
        Tracks trackEntity = Database.select(Tracks.class, trackRequest.hash());
        if (trackEntity == null) {
            try {
                Track track = Api.call(trackRequest);
                trackEntity = new Tracks(trackRequest.hash(), track.getName(),
                        track.getDuration(), track.getListeners(), track.getPlaycount());
                trackEntity.setArtists(artistEntity);
                if (Database.insert(trackEntity)) {
                    List<String> tagNames = track.getTags().stream()
                            .limit(TRACK_TAGS_LIMIT)
                            .collect(Collectors.toList());
                    for (int j = 0; j < tagNames.size(); j++) {
                        String tagName = track.getTags().get(j);
                        Tags tagEntity = insertTag(tagName);
                        TracksTags trackTagEntity = new TracksTags(tagEntity, trackEntity, (byte) (j + 1));
                        Database.insert(trackTagEntity);
                    }
                }
            } catch (Api.ResponseError | Api.NotJsonInResponseException e) {
                // No info about track. (Not exactly)
            }
        }
        return trackEntity;
    }

    private static Tags insertTag(String tagName) {
        Request<Tag> tagRequest = Tag.GetInfo.createRequest(tagName);
        Tags tagEntity = Database.select(Tags.class, tagRequest.hash());
        if (tagEntity == null) {
            Tag tag;
            try {
                tag = Api.call(tagRequest);
                tagEntity = new Tags(
                        tagRequest.hash(), tag.getName(), tag.getReach(), tag.getTaggings());
            } catch (Api.NotJsonInResponseException | Api.ResponseError e) {
                tagEntity = new Tags(tagRequest.hash(), tagName, -1, -1);
            }
            Database.insert(tagEntity);
        }
        return tagEntity;
    }
}
