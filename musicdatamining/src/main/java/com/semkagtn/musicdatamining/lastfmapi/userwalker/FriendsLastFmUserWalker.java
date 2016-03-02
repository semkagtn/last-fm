package com.semkagtn.musicdatamining.lastfmapi.userwalker;

import com.semkagtn.musicdatamining.lastfmapi.LastFmApi;
import com.semkagtn.musicdatamining.lastfmapi.model.item.LastFmUserItem;
import com.semkagtn.musicdatamining.lastfmapi.model.response.UserGetFriendsResponse;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 09.02.16.
 */
public class FriendsLastFmUserWalker implements LastFmUserWalker {

    private static final int MAX_VISITED_USERS = 2000;
    private static final int MIN_USER_QUEUE_SIZE = 50;

    private LastFmApi api;

    private Queue<LastFmUserItem> userCache = new ArrayDeque<>();
    private Queue<String> userNamesQueue = new ArrayDeque<>();
    private Set<String> visitedUserNames = new HashSet<>();

    public FriendsLastFmUserWalker(LastFmApi api, String initialUser) {
        this.api = api;
        this.userNamesQueue.add(initialUser);
    }

    @Override
    public LastFmUserItem nextUser() {
        while (userCache.isEmpty()) {
            String userName = userNamesQueue.remove();
            UserGetFriendsResponse friendsResponse = api.userGetFriends(userName);
            List<LastFmUserItem> friends = extractFriends(friendsResponse);
            userCache.addAll(friends);
        }
        LastFmUserItem user = userCache.remove();
        addVisitedUser(user);
        addUserToQueue(user);
        return user;
    }

    public void addUserToQueue(LastFmUserItem user) {
        if (userNamesQueue.size() <= MIN_USER_QUEUE_SIZE) {
            userNamesQueue.add(user.getName());
        }
    }

    private void addVisitedUser(LastFmUserItem user) {
        if (visitedUserNames.size() == MAX_VISITED_USERS) {
            visitedUserNames.clear();
        }
        visitedUserNames.add(user.getName());
    }

    private List<LastFmUserItem> extractFriends(UserGetFriendsResponse response) {
        if (response == null || response.getError() != null ||
                response.getFriends() == null || response.getFriends().getUsers() == null) {
            return new ArrayList<>();
        }
        return response.getFriends().getUsers().stream()
                .filter(friend -> friend != null && friend.getGender() != null)
                .filter(friend -> !visitedUserNames.contains(friend.getName()))
                .collect(Collectors.toList());
    }
}
