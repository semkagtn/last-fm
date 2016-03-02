package com.semkagtn.musicdatamining.lastfmapi.userwalker;

import com.semkagtn.musicdatamining.lastfmapi.model.item.LastFmUserItem;

import java.util.function.Predicate;

/**
 * Created by semkagtn on 10.02.16.
 */
public class PredicateLastFmUserWalker implements LastFmUserWalker {

    private LastFmUserWalker userWalker;
    private Predicate<LastFmUserItem> predicate;

    public PredicateLastFmUserWalker(LastFmUserWalker userWalker,
                                     Predicate<LastFmUserItem> predicate) {
        this.userWalker = userWalker;
        this.predicate = predicate;
    }

    @Override
    public LastFmUserItem nextUser() {
        LastFmUserItem user;
        do {
            user = userWalker.nextUser();
        } while (!predicate.test(user));
        return user;
    }
}
