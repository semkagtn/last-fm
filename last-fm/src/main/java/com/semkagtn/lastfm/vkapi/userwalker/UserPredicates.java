package com.semkagtn.lastfm.vkapi.userwalker;

import com.semkagtn.lastfm.vkapi.response.UserItem;
import org.joda.time.DateTime;
import org.joda.time.Days;

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

    @Deprecated
    public static Predicate<UserItem> minimumAudios(int minimumAudios) {
        // VK API not returns required field in several ways. Better not to use this.
        return user -> user.getCounters() != null && user.getCounters().getAudios() >= minimumAudios;
    }

    public static Predicate<UserItem> hasAvatar() {
        return user -> !user.getPhoto50().equals("http://vk.com/images/camera_50.png");
    }

    @Deprecated
    public static Predicate<UserItem> lastSeenNoMoreDaysThen(int days) {
        // VK API returns invalid last seen time. Do not use this!
        return user -> {
            if (user.getLastSeen() == null || user.getLastSeen().getTime() == null) {
                return false;
            }
            DateTime now = DateTime.now();
            DateTime lastSeen = new DateTime(user.getLastSeen().getTime());
            return Days.daysBetween(lastSeen, now).getDays() <= days;
        };
    }

    private UserPredicates() {

    }
}
