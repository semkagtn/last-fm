package com.semkagtn.lastfm;

import com.semkagtn.lastfm.database.Database;
import com.semkagtn.lastfm.recenttrackscollector.RecentTracksCollector;
import com.semkagtn.lastfm.recenttrackscollector.SimpleRecentTracksCollector;
import com.semkagtn.lastfm.userwalker.RandomRecursiveUserWalker;
import com.semkagtn.lastfm.userwalker.UserWalker;
import com.semkagtn.lastfm.utils.RequestWrapper;
import com.semkagtn.lastfm.utils.Utils;
import de.umass.lastfm.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 2/14/15.
 */
public class CollectData {

    private static final int USER_WALKER_DEPTH = 2;
    private static final int FRIENDS_LIMIT = 10;
    private static final int RECENT_TRACKS_LIMIT = 100;

    private static final int USERS_AMOUNT = 1200;
    private static final String API_KEY = "ca07f2773420c59d127dddd445db202e";

    public static void main(String[] args) throws RequestWrapper.RequestException {
        Database.open();

        List<Integer> visitedIds = Database.users().select()
                .stream()
                .map(Users::getUserId)
                .collect(Collectors.toList());
        System.out.println(visitedIds);
        UserWalker userWalker = new RandomRecursiveUserWalker(visitedIds, USER_WALKER_DEPTH, FRIENDS_LIMIT, API_KEY);
        RecentTracksCollector recentTracksCollector = new SimpleRecentTracksCollector(RECENT_TRACKS_LIMIT, API_KEY);

        for (int i = 0; i < USERS_AMOUNT; i++) {
            User user = userWalker.nextUser();
            int userId = Integer.valueOf(user.getId());
            Users users = new Users(userId, user.getAge(),
                    !user.getGender().equals("") ? user.getGender() : "n",
                    user.getCountry(), user.getPlaycount());

            try {
                Set<RecentTracks> recentTracks = recentTracksCollector.collect(userId)
                        .stream()
                        .map(x -> new RecentTracks(users, x.getArtist(), x.getAlbum(), x.getName(),
                                Utils.dateToString(x.getPlayedWhen())))
                        .collect(Collectors.toSet());
                users.setRecentTrackses(recentTracks);

                Database.users().insert(users);
            } catch (RequestWrapper.RequestException e) {
                i--; // Bad response. Try to get another user.
            }
        }

        Database.close();
    }
}
