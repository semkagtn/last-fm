package com.semkagtn.lastfm.vkapi.userwalker;

import com.semkagtn.lastfm.vkapi.response.UserItem;

import java.util.function.Predicate;

/**
 * Created by semkagtn on 08.09.15.
 */
public class UserPredicates {

    public static Predicate<UserItem> hasAge() {
        return user -> user.getBdate() != null && user.getBdate().split("\\\\.").length == 3;
    }

    public static Predicate<UserItem> hasGender() {
        return user -> user.getSex() != null && (user.getSex().equals(1) || user.getSex().equals(2));
    }

    private UserPredicates() {

    }
}
