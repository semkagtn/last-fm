package com.semkagtn.lastfm;

import com.semkagtn.lastfm.database.Database;
import com.semkagtn.lastfm.recenttrackscollector.LastRecentTracksCollector;
import com.semkagtn.lastfm.recenttrackscollector.RecentTracksCollector;
import com.semkagtn.lastfm.topartistscollector.SimpleTopArtistsCollector;
import com.semkagtn.lastfm.topartistscollector.TopArtistsCollector;
import com.semkagtn.lastfm.userwalker.RandomRecursiveUserWalker;
import com.semkagtn.lastfm.userwalker.UserWalker;
import com.semkagtn.lastfm.utils.RequestWrapper;
import com.semkagtn.lastfm.utils.Utils;
import de.umass.lastfm.Artist;
import de.umass.lastfm.Tag;
import de.umass.lastfm.Track;
import de.umass.lastfm.User;

import java.util.List;
import java.util.stream.Collectors;

import static com.semkagtn.lastfm.utils.RequestWrapper.request;
import static de.umass.lastfm.Period.OVERALL;

/**
 * Created by semkagtn on 2/14/15.
 */
public class CollectData {

    private static final int USER_WALKER_DEPTH = 2;
    private static final int FRIENDS_LIMIT = 10;
    private static final int RECENT_TRACKS_LIMIT = 100;
    private static final int TOP_ARTISTS_LIMIT = 5;
    private static final int TAGS_LIMIT = 3;

    private static final int USERS_AMOUNT = 500;
    private static final String API_KEY = "ca07f2773420c59d127dddd445db202e";

    public static void main(String[] args) {
        Database.open();

        List<Integer> visitedIds = Database.select(Users.class)
                .stream()
                .map(Users::getUserId)
                .collect(Collectors.toList());
        UserWalker userWalker = new RandomRecursiveUserWalker(visitedIds, USER_WALKER_DEPTH, FRIENDS_LIMIT, API_KEY);
        RecentTracksCollector recentTracksCollector = new LastRecentTracksCollector(RECENT_TRACKS_LIMIT, API_KEY);
        TopArtistsCollector topArtistsCollector = new SimpleTopArtistsCollector(OVERALL, TOP_ARTISTS_LIMIT,  API_KEY);

        for (int i = 0; i < USERS_AMOUNT; i++) {
            User user;
            int userId;
            List<Track> recentTracks;
            List<Artist> topArtists;
            try {
                user = userWalker.nextUser();
                userId = Integer.valueOf(user.getId());
                recentTracks = recentTracksCollector.collect(userId);
                topArtists = topArtistsCollector.collect(userId);
            } catch (RequestWrapper.RequestException e) {
                i--; // Bad user. Try to get another.
                continue;
            }
            Users userEntity = new Users(userId, user.getAge(),
                    !user.getGender().equals("") ? user.getGender() : "n",
                    user.getCountry(), user.getPlaycount());
            Database.insert(userEntity);

            for (Track recentTrack : recentTracks) {
                Artists artistEntity = new Artists(recentTrack.getArtist(), 0, 0);
                if (!Database.objectExists(artistEntity)) {
                    boolean artistInserted = insertArtistWithTags(artistEntity);
                    if (!artistInserted) {
                        continue; // Bad artist. Skip this track.
                    }
                }
                RecentTracks recentTrackEntity = new RecentTracks(artistEntity, userEntity,
                        recentTrack.getName(), Utils.dateToString(recentTrack.getPlayedWhen()));
                Database.insert(recentTrackEntity);
            }

            for (Artist artist : topArtists) {
                Artists artistEntity = new Artists(artist.getName(), 0, 0);
                if (!Database.objectExists(artistEntity)) {
                    boolean artistInserted = insertArtistWithTags(artistEntity);
                    if (!artistInserted) {
                        continue; // Bad artist. Skip it.
                    }
                }
                TopArtists topArtistEntity = new TopArtists(artistEntity, userEntity, artist.getPlaycount());
                Database.insert(topArtistEntity);
            }
        }

        Database.close();
    }

    private static boolean insertArtistWithTags(Artists artistEntity) {
        Artist artist;
        try {
            artist = request(Artist::getInfo, artistEntity.getArtistName(), API_KEY);
        } catch (RequestWrapper.RequestException e) {
            return false; // Bad artist.
        }
        artistEntity.setListeners(artist.getListeners());
        artistEntity.setPlays(artist.getPlaycount());
        Database.insert(artistEntity);
        List<String> tagNames = artist.getTags()
                .stream()
                .limit(TAGS_LIMIT)
                .collect(Collectors.toList());
        for (int j = 0; j < tagNames.size(); j++) {
            String tagName = tagNames.get(j);
            Tags tagEntity = new Tags(tagName, 0, 0);
            if (!Database.objectExists(tagEntity)) {
                Tag tag;
                try {
                    tag = request(Tag::getInfo, tagName, API_KEY);
                } catch (NullPointerException e) {
                    continue; // Last-fm library bug. Skip this tag.
                } catch (RequestWrapper.RequestException e) {
                    continue; // Bad tag. Skip it.
                }
                tagEntity.setReach(tag.getReach());
                tagEntity.setTaggings(tag.getTaggings());
                Database.insert(tagEntity);
            }
            ArtistsTags artistTagEntity = new ArtistsTags(artistEntity, tagEntity, (byte) (j + 1));
            Database.insert(artistTagEntity);
        }
        return true;
    }
}
