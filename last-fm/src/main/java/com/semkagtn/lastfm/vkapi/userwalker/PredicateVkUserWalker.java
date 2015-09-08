package com.semkagtn.lastfm.vkapi.userwalker;

import com.semkagtn.lastfm.vkapi.response.UserItem;

import java.util.function.Predicate;

/**
 * Created by semkagtn on 08.09.15.
 */
public class PredicateVkUserWalker implements VkUserWalker {

    private VkUserWalker userWalker;
    private Predicate<UserItem> predicate;

    public PredicateVkUserWalker(VkUserWalker userWalker, Predicate<UserItem> predicate) {
        this.userWalker = userWalker;
        this.predicate = predicate;
    }

    @Override
    public UserItem nextUser() {
        UserItem userItem;
        while (true) {
            userItem = userWalker.nextUser();
            if (predicate.test(userItem)) {
                return userItem;
            }
        }
    }
}
