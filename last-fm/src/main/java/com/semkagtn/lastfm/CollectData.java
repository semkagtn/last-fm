package com.semkagtn.lastfm;

import com.semkagtn.lastfm.database.Database;
import com.semkagtn.lastfm.recenttrackscollector.LastRecentTracksCollector;
import com.semkagtn.lastfm.recenttrackscollector.RecentTracksCollector;
import com.semkagtn.lastfm.userwalker.RandomRecursiveUserWalker;
import com.semkagtn.lastfm.userwalker.UserWalker;
import com.semkagtn.lastfm.utils.RequestWrapper;
import com.semkagtn.lastfm.utils.Utils;
import de.umass.lastfm.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.semkagtn.lastfm.utils.RequestWrapper.request;

/**
 * Created by semkagtn on 2/14/15.
 */
public class CollectData {

    private static final int USER_WALKER_DEPTH = 2;
    private static final int FRIENDS_LIMIT = 15;
    private static final int RECENT_TRACKS_LIMIT = 600;
    private static final int TAGS_LIMIT = 5;

    private static String[] apiKeys = new String[]{
            "b8b099d24df9c92133b729ac71ba0478",
            "ba83737f1d804bbb7499adaf6dfbf478",
            "db4d324783268f2d26a5593b292399cb",
            "c3f08d2af7231974467281edd2836cf3",
            "03269abd0820d42871cda4514e03325e",
            "ea1ea195273e2a918dbff8396bc7e2a4",
            "fc1e0c6d0eb6562b26e808fc92aba272",
            "9926b618f0ca51b2c85488c58e1afe4d",
            "d8f866dfa74f17b7ba9d9c6b418a0058",
            "6f81fde5cee816804e8e12c983dc7502",
            "fd06b14a43753befe6ec4c5a3edc7e8a",
            "3f600866a464d2b770eef80485cafab6",
            "ac8f614967ae1424a78d6d6e9d383b71",
            "7c62d6be8036d151bf3696da5e6645f7",
            "f436890e149816f13c07e8c235501651",
            "b941d8d79abd76fb2acea5ff9ed17b7f",
            "200b9e2a11ae6ebfcfbf5bcee2855a98",
            "39b18e8ff8ea40f2b3e99c0de4e1f5f9",
            "5669f663e5e96f9bd0e3309ff5c05cef",
            "64676c7280b3b8f7ed73cce4741baac7"
    };

    public static void main(String[] args) {
//        Caller.getInstance().setCache(null);
//
//        String apiKey = apiKeys[Integer.valueOf(args[0])];
//        int usersAmount = Integer.valueOf(args[1]);
//
//        Database.open();
//
//        UserWalker userWalker = new RandomRecursiveUserWalker(USER_WALKER_DEPTH, FRIENDS_LIMIT, apiKey);
//        RecentTracksCollector recentTracksCollector = new LastRecentTracksCollector(RECENT_TRACKS_LIMIT, apiKey);
//
//        for (int i = 0; i < usersAmount; i++) {
//            User user;
//            int userId;
//            List<Track> recentTracks;
//            try {
//                user = userWalker.nextUser();
//                if (user.getPlaycount() < RECENT_TRACKS_LIMIT) {
//                    i--; // Bad user. Try to get another.
//                    continue;
//                }
//                userId = Integer.valueOf(user.getId());
//                recentTracks = recentTracksCollector.collect(userId);
//            } catch (RequestWrapper.RequestException e) {
//                i--; // Bad user. Try to get another.
//                continue;
//            }
//            Users userEntity = new Users(userId, user.getAge(),
//                    !user.getGender().equals("") ? user.getGender() : "n",
//                    user.getCountry(), user.getPlaycount());
//            boolean userInserted = Database.insertIfNotExists(userEntity);
//            if (!userInserted) {
//                i--; // User exists. Try to get another.
//                continue;
//            }
//            for (Track recentTrack : recentTracks) {
//                Artists artistEntity = new Artists(recentTrack.getArtist(), 0, 0);
//                try {
//                    insertArtistWithTags(artistEntity, apiKey);
//                } catch (RequestWrapper.RequestException e) {
//                    continue; // Bad artist. Skip this track.
//                }
//                RecentTracks recentTrackEntity = new RecentTracks(artistEntity, userEntity,
//                        recentTrack.getName(), Utils.dateToString(recentTrack.getPlayedWhen()));
//                Database.insert(recentTrackEntity);
//            }
//        }
//
//        Database.close();
    }

    private static void insertArtistWithTags(Artists artistEntity, String apiKey)
            throws RequestWrapper.RequestException {
//        Artist artist;
//        artist = request(Artist::getInfo, artistEntity.getArtistName(), apiKey);
//        artistEntity.setListeners(artist.getListeners());
//        artistEntity.setPlays(artist.getPlaycount());
//        boolean artistInserted = Database.insertIfNotExists(artistEntity);
//        if (!artistInserted) {
//            return;
//        }
//        List<String> tagNames = artist.getTags()
//                .stream()
//                .limit(TAGS_LIMIT)
//                .collect(Collectors.toList());
//        for (int j = 0; j < tagNames.size(); j++) {
//            String tagName = tagNames.get(j);
//            Tags tagEntity = new Tags(tagName, 0, 0);
//            try {
//                insertTag(tagEntity, apiKey);
//            } catch (RequestWrapper.RequestException e) {
//                continue;
//            }
//            ArtistsTags artistTagEntity = new ArtistsTags(artistEntity, tagEntity, (byte) (j + 1));
//            Database.insert(artistTagEntity);
//        }
    }

    private static void insertTag(Tags tagEntity, String apiKey) throws RequestWrapper.RequestException {
//        Tag tag;
//        try {
//            tag = request(Tag::getInfo, tagEntity.getTagName(), apiKey);
//        } catch (NullPointerException e) {
//            throw new RequestWrapper.RequestException(); // Last-fm library bug. Skip this tag.
//        }
//        tagEntity.setReach(tag.getReach());
//        tagEntity.setTaggings(tag.getTaggings());
//        Database.insertIfNotExists(tagEntity);
    }
}
