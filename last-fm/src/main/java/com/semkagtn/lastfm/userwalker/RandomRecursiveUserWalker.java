package com.semkagtn.lastfm.userwalker;

/**
 * Created by semkagtn on 2/14/15.
 */
public class RandomRecursiveUserWalker /*implements UserWalker*/ {

//    private static final int ID_BOUND = 65_000_000;
//
//    private Iterator<UserItem> iterator = new ArrayList<UserItem>().iterator();
//    private Set<Integer> visitedUsers = new HashSet<>();
//    private Random random = new Random();
//
//    private final int depth;
//    private final int friendsLimit;
//    private VkApi api;
//
//    public RandomRecursiveUserWalker(int depth, int friendsLimit, VkApi api) {
//        this.depth = depth;
//        this.friendsLimit = friendsLimit;
//        this.api = api;
//    }
//
//    @Override
//    public UserItem nextUser() {
//        if (iterator.hasNext()) {
//            return iterator.next();
//        } else {
//            UserItem user = null;
//            while (user == null) {
//                int randomId = random.nextInt(ID_BOUND);
//                if (!visitedUsers.contains(randomId)) {
//                    UsersGetResponse response = api.usersGet(Collections.singletonList(String.valueOf(randomId)));
//                    if (response.getError() == null) {
//                        user = response.getResponse().get(0);
//                        visitedUsers.add(user.getId());
//                        iterator = friendsIterator(user.getId());
//                    }
//                }
//
//            }
//            return user;
//        }
//    }
//
//    private Iterator<UserItem> friendsIterator(int userId) throws Api.NotJsonInResponseError {
//        List<User> users = new ArrayList<>();
//        dfs(userId, 0, users);
//        return users.iterator();
//    }
//
//    private void dfs(int userId, int currentDepth, List<User> users) throws Api.NotJsonInResponseError {
//        if (currentDepth == depth) {
//            return;
//        }
//        List<User> friends = api.friendsGet(User.GetFriends.createRequest(String.valueOf(userId), 0, friendsLimit))
//                .stream()
//                .filter(x -> !visitedUsers.contains(x.getId()))
//                .collect(Collectors.toList());
//        users.addAll(friends);
//        for (User friend : friends) {
//            visitedUsers.add(friend.getId());
//        }
//        for (User friend : friends) {
//            dfs(friend.getId(), currentDepth + 1, users);
//        }
//    }
}
