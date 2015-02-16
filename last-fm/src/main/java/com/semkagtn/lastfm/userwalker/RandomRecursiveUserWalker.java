package com.semkagtn.lastfm.userwalker;

import com.semkagtn.lastfm.utils.RequestWrapper;
import de.umass.lastfm.User;

import java.util.*;
import java.util.stream.Collectors;

import static com.semkagtn.lastfm.utils.RequestWrapper.request;

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
    private final String apiKey;

    public RandomRecursiveUserWalker(List<Integer> visitedIds, int depth, int friendsLimit, String apiKey) {
        this.visitedUsers.addAll(visitedIds);
        this.depth = depth;
        this.friendsLimit = friendsLimit;
        this.apiKey = apiKey;
    }

    public RandomRecursiveUserWalker(int depth, int friendsLimit, String apiKey) {
        this(new ArrayList<>(), depth, friendsLimit, apiKey);
    }

    @Override
    public User nextUser() throws RequestWrapper.RequestException {
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            User user = null;
            while (user == null) {
                int randomId = random.nextInt(ID_BOUND);
                try {
                    if (!visitedUsers.contains(randomId)) {
                        user = request(User::getInfo, String.valueOf(randomId), apiKey);
                    }
                } catch (RequestWrapper.RequestException e) {
                    continue;
                }
            }
            int userId = Integer.valueOf(user.getId());
            visitedUsers.add(userId);
            iterator = friendsIterator(userId);
            return user;
        }
    }

    private Iterator<User> friendsIterator(int userId) throws RequestWrapper.RequestException {
        List<User> users = new ArrayList<>();
        dfs(userId, 0, users);
        return users.iterator();
    }

    private void dfs(int userId, int currentDepth, List<User> users) throws RequestWrapper.RequestException {
        if (currentDepth == depth) {
            return;
        }
        List<User> friends = request(User::getFriends,
                String.valueOf(userId), false, 0, friendsLimit, apiKey)
                .getPageResults()
                .stream()
                .filter(x -> !visitedUsers.contains(Integer.valueOf(x.getId())))
                .collect(Collectors.toList());
        users.addAll(friends);
        for (User friend : friends) {
            visitedUsers.add(Integer.valueOf(friend.getId()));
        }
        for (User friend : friends) {
            dfs(Integer.valueOf(friend.getId()), currentDepth + 1, users);
        }
    }
}
