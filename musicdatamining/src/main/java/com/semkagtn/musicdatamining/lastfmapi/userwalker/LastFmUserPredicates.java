package com.semkagtn.musicdatamining.lastfmapi.userwalker;

import com.semkagtn.musicdatamining.lastfmapi.model.item.LastFmUserItem;

import java.util.function.Predicate;

/**
 * Created by semkagtn on 10.02.16.
 */
public class LastFmUserPredicates {

    private LastFmUserPredicates() {

    }

    public static Predicate<LastFmUserItem> hasGender() {
        return user -> "f".equals(user.getGender()) || "m".equals(user.getGender());
    }
}
