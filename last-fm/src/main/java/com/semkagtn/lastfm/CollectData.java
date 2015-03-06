package com.semkagtn.lastfm;

//import com.semkagtn.lastfm.database.Database;
//import com.semkagtn.lastfm.recenttrackscollector.LastRecentTracksCollector;
//import com.semkagtn.lastfm.recenttrackscollector.RecentTracksCollector;
//import com.semkagtn.lastfm.userwalker.RandomRecursiveUserWalker;
//import com.semkagtn.lastfm.userwalker.UserWalker;
//import com.semkagtn.lastfm.utils.RequestWrapper;
//import com.semkagtn.lastfm.utils.Utils;
//import de.umass.lastfm.Artist;
//import de.umass.lastfm.Tag;
//import de.umass.lastfm.Track;
//import de.umass.lastfm.User;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static com.semkagtn.lastfm.utils.RequestWrapper.request;

/**
 * Created by semkagtn on 2/14/15.
 */
public class CollectData {

//    private static final int USER_WALKER_DEPTH = 2;
//    private static final int FRIENDS_LIMIT = 5;
//    private static final int ARTIST_TAGS_LIMIT = 5;
//    private static final int TRACK_TAGS_LIMIT = 5;
//    private static final int RECENT_TRACKS_LIMIT = 600;
//    private static final int RECENT_TRACKS_MINIMUM = RECENT_TRACKS_LIMIT / 5;
//
//    private static String[] apiKeys = new String[]{
//            "b8b099d24df9c92133b729ac71ba0478",
//            "ba83737f1d804bbb7499adaf6dfbf478",
//            "db4d324783268f2d26a5593b292399cb",
//            "c3f08d2af7231974467281edd2836cf3",
//            "03269abd0820d42871cda4514e03325e",
//            "ea1ea195273e2a918dbff8396bc7e2a4",
//            "fc1e0c6d0eb6562b26e808fc92aba272",
//            "9926b618f0ca51b2c85488c58e1afe4d",
//            "d8f866dfa74f17b7ba9d9c6b418a0058",
//            "6f81fde5cee816804e8e12c983dc7502",
//            "fd06b14a43753befe6ec4c5a3edc7e8a",
//            "3f600866a464d2b770eef80485cafab6",
//            "ac8f614967ae1424a78d6d6e9d383b71",
//            "7c62d6be8036d151bf3696da5e6645f7",
//            "f436890e149816f13c07e8c235501651",
//            "b941d8d79abd76fb2acea5ff9ed17b7f",
//            "200b9e2a11ae6ebfcfbf5bcee2855a98",
//            "39b18e8ff8ea40f2b3e99c0de4e1f5f9",
//            "5669f663e5e96f9bd0e3309ff5c05cef",
//            "64676c7280b3b8f7ed73cce4741baac7"
//    };
//
//    public static void main(String[] args) {
//        String apiKey = apiKeys[Integer.valueOf(args[0])];
//        int usersAmount = Integer.valueOf(args[1]);
//
//        UserWalker userWalker = new RandomRecursiveUserWalker(USER_WALKER_DEPTH, FRIENDS_LIMIT, apiKey);
//        RecentTracksCollector tracksCollector = new LastRecentTracksCollector(RECENT_TRACKS_LIMIT, apiKey);
//        Database.open();
//        for (int i = 0; i < usersAmount; i++) {
//            User user;
//            try {
//                user = userWalker.nextUser();
//            } catch (RequestWrapper.RequestException e) {
//                i--;
//                continue; // Can't parse user. Try to get another user.
//            }
//            String gender = user.getGender().equals("") ? "n" : user.getGender();
//            if (user.getPlaycount() < RECENT_TRACKS_MINIMUM || gender.equals("n") && user.getAge() < 0) {
//                i--;
//                continue; // Uninformative user. Try to get another.
//            }
//            int userId = Integer.valueOf(user.getId());
//
//            List<Track> tracks;
//            try {
//                tracks = tracksCollector.collect(userId);
//            } catch (RequestWrapper.RequestException e) {
//                i--;
//                continue; // Can't parse recent tracks. Try to get another user;
//            }
//
//            Users userEntity = new Users(
//                    userId, user.getAge(), user.getGender(), user.getCountry(), user.getPlaycount());
//            boolean userInserted = Database.insertIfNotExists(userEntity, "user_id");
//            if (!userInserted) {
//                i--;
//                continue; // User exists. Try to get another.
//            }
//
//            for (Track recentTrack : tracks) {
//                Artist artist;
//                Track track;
//                try {
//                    artist = request(Artist::getInfo, recentTrack.getArtist(), apiKey);
//                    track = request(Track::getInfo, recentTrack.getArtist(), recentTrack.getName(), apiKey);
//                } catch (RequestWrapper.RequestException e) {
//                    continue; // Can't parse artist or track info. Skip this recent track.
//                }
//                Artists artistEntity = insertArtistWithTags(artist, apiKey);
//                Tracks trackEntity = insertTrackWithTags(track, artistEntity, apiKey);
//                RecentTracks recentTrackEntity = new RecentTracks(
//                        artistEntity, userEntity, trackEntity, Utils.dateToString(recentTrack.getPlayedWhen()));
//                Database.insert(recentTrackEntity);
//            }
//        }
//        Database.close();
//    }
//
//    private static Artists insertArtistWithTags(Artist artist, String apiKey) {
//        Artists artistEntity = new Artists(artist.getName(), artist.getListeners(), artist.getPlaycount());
//        boolean artistInserted = Database.insertIfNotExists(artistEntity, "artist_name");
//        if (artistInserted) {
//            List<String> tagNames = artist.getTags().stream().limit(ARTIST_TAGS_LIMIT).collect(Collectors.toList());
//            for (int i = 0; i < tagNames.size(); i++) {
//                String tagName = tagNames.get(i);
//                Tag tag;
//                try {
//                    tag = request(Tag::getInfo, tagName, apiKey);
//                } catch (RequestWrapper.RequestException e) {
//                    continue; // Can't parse tag. Skip it.
//                } catch (NullPointerException e) {
//                    continue; // Last-fm library bug. Skip this tag.
//                }
//                Tags tagEntity = new Tags(tag.getName(), tag.getReach(), tag.getTaggings());
//                Database.insertIfNotExists(tagEntity, "tag_name");
//                ArtistsTags artistsTagsEntity = new ArtistsTags(artistEntity, tagEntity, (byte) (i + 1));
//                Database.insert(artistsTagsEntity);
//            }
//        }
//        return artistEntity;
//    }
//
//    private static Tracks insertTrackWithTags(Track track, Artists artistEntity, String apiKey) {
//        Tracks trackEntity = new Tracks(
//                artistEntity, track.getName(), track.getDuration(), track.getListeners(), track.getPlaycount());
//        boolean trackInserted = Database.insertIfNotExists(trackEntity, "artist_id", "track_name");
//        if (trackInserted) {
//            List<String> tagNames = track.getTags().stream().limit(TRACK_TAGS_LIMIT).collect(Collectors.toList());
//            for (int i = 0; i < tagNames.size(); i++) {
//                String tagName = tagNames.get(i);
//                Tag tag;
//                try {
//                    tag = request(Tag::getInfo, tagName, apiKey);
//                } catch (RequestWrapper.RequestException e) {
//                    continue; // Can't parse tag. Skip it.
//                } catch (NullPointerException e) {
//                    continue; // Last-fm library bug. Skip this tag.
//                }
//                Tags tagEntity = new Tags(tag.getName(), tag.getReach(), tag.getTaggings());
//                Database.insertIfNotExists(tagEntity, "tag_name");
//                TracksTags tracksTagsEntity = new TracksTags(tagEntity, trackEntity, (byte) (i + 1));
//                Database.insert(tracksTagsEntity);
//            }
//        }
//        return trackEntity;
//    }
}
