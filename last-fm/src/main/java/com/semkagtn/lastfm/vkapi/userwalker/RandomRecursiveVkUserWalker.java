package com.semkagtn.lastfm.vkapi.userwalker;

import com.semkagtn.lastfm.vkapi.VkApi;
import com.semkagtn.lastfm.vkapi.response.FriendsGetResponse;
import com.semkagtn.lastfm.vkapi.response.UserItem;
import com.semkagtn.lastfm.vkapi.response.UsersGetResponse;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 04.09.15.
 */
public class RandomRecursiveVkUserWalker implements VkUserWalker {

    private static final int USER_ID_BOUND = 322_000_000;

    private Queue<UserItem> usersQueue = new ArrayDeque<>();
    private Random random = new Random();
    private Set<Integer> visitedUsers = new HashSet<>();

    private int maxDepth;
    private int friendsLimit;
    private VkApi api;

    public RandomRecursiveVkUserWalker(int maxDepth, int friendsLimit, VkApi api) {
        this.maxDepth = maxDepth;
        this.friendsLimit = friendsLimit;
        this.api = api;
    }

    @Override
    public UserItem nextUser() {
        if (usersQueue.isEmpty()) {
            UserItem user = null;
            while (user == null) {
                int randomUserId = random.nextInt(USER_ID_BOUND);
                if (!visitedUsers.contains(randomUserId)) {
                    UsersGetResponse response = api.usersGet(Arrays.asList(String.valueOf(randomUserId)));
                    if (response.getError() == null && response.getResponse().size() > 0) {
                        user = response.getResponse().get(0);
                        visitedUsers.add(user.getId());
                        usersQueue.addAll(dfs(user.getId()));
                    }
                }
            }
            return user;
        } else {
            return usersQueue.remove();
        }
    }

    private List<UserItem> dfs(int userId) {
        List<UserItem> users = new ArrayList<>();
        dfs(userId, 0, users);
        return users;
    }

    private void dfs(int userId, int currentDepth, List<UserItem> users) {
        if (currentDepth == maxDepth) {
            return;
        }
        List<UserItem> friends;
        FriendsGetResponse response = api.friendsGet(userId, 0, friendsLimit);
        if (response.getError() != null) {
            if (response.getError().getErrorCode() == 15) {
                friends = new ArrayList<>();
            } else {
                throw new RuntimeException("Error while getting user friends: " + response.getError());
            }
        } else {
            friends = response.getResponse().getItems().stream()
                    .filter(friend -> !visitedUsers.contains(friend.getId()))
                    .collect(Collectors.toList());
        }
        users.addAll(friends);
        for (UserItem friend : friends) {
            visitedUsers.add(friend.getId());
        }
        for (UserItem friend: friends) {
            dfs(friend.getId(), currentDepth + 1, users);
        }
    }
}
