package com.semkagtn.lastfm.userwalker;

import com.semkagtn.lastfm.api.Api;
import com.semkagtn.lastfm.api.User;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by semkagtn on 2/14/15.
 */
public class RandomRecursiveUserWalker implements UserWalker {

    private static final int ID_BOUND = 65_000_000;

    private Iterator<User> iterator = new ArrayList<User>().iterator();
    private Set<Integer> visitedUsers = new HashSet<>();
    private Random random = new Random();

    private final int depth;
    private final int friendsLimit;

    public RandomRecursiveUserWalker(int depth, int friendsLimit) {
        this.depth = depth;
        this.friendsLimit = friendsLimit;
    }

    @Override
    public User nextUser() {
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            User user = null;
            while (user == null) {
                int randomId = random.nextInt(ID_BOUND);
                try {
                    if (!visitedUsers.contains(randomId)) {
                        user = Api.call(User.GetInfo.createRequest(String.valueOf(randomId)));
                        visitedUsers.add(user.getId());
                        iterator = friendsIterator(user.getId());
                    }
                } catch (Api.ResponseError | Api.NotJsonInResponseException e) {
                    continue;
                }
            }
            return user;
        }
    }

    private Iterator<User> friendsIterator(int userId) throws Api.NotJsonInResponseException {
        List<User> users = new ArrayList<>();
        dfs(userId, 0, users);
        return users.iterator();
    }

    private void dfs(int userId, int currentDepth, List<User> users) throws Api.NotJsonInResponseException {
        if (currentDepth == depth) {
            return;
        }
        List<User> friends = Api.call(User.GetFriends.createRequest(String.valueOf(userId), 0, friendsLimit))
                .stream()
                .filter(x -> !visitedUsers.contains(x.getId()))
                .collect(Collectors.toList());
        users.addAll(friends);
        for (User friend : friends) {
            visitedUsers.add(friend.getId());
        }
        for (User friend : friends) {
            dfs(friend.getId(), currentDepth + 1, users);
        }
    }
}
