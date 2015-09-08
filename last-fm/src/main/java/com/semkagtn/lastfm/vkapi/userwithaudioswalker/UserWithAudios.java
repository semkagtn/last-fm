package com.semkagtn.lastfm.vkapi.userwithaudioswalker;

import com.semkagtn.lastfm.vkapi.response.AudioItem;
import com.semkagtn.lastfm.vkapi.response.UserItem;

import java.util.List;

/**
 * Created by semkagtn on 08.09.15.
 */
public class UserWithAudios {

    private UserItem user;
    private List<AudioItem> audios;

    public UserWithAudios(UserItem user, List<AudioItem> audios) {
        this.user = user;
        this.audios = audios;
    }

    public UserItem getUser() {
        return user;
    }

    public List<AudioItem> getAudios() {
        return audios;
    }
}
