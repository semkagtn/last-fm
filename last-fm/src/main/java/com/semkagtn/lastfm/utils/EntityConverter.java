package com.semkagtn.lastfm.utils;

import com.semkagtn.lastfm.Users;
import com.semkagtn.lastfm.vkapi.response.UserItem;

/**
 * Created by semkagtn on 08.09.15.
 */
public class EntityConverter {

    public static Users convertUser(UserItem userItem) {
        Users users = new Users();
        users.setId(userItem.getId());
        users.setGender(userItem.getSex() == 1 ? "f" : userItem.getSex() == 2 ? "m" : null);
        users.setAge(DateTimeUtils.calculateAge(userItem.getBdate()));
        return users;
    }

    private EntityConverter() {

    }
}
