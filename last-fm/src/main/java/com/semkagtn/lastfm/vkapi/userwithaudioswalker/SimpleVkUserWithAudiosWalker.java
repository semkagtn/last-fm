package com.semkagtn.lastfm.vkapi.userwithaudioswalker;

import com.semkagtn.lastfm.vkapi.VkApi;
import com.semkagtn.lastfm.vkapi.response.AudioGetResponse;
import com.semkagtn.lastfm.vkapi.response.AudioItem;
import com.semkagtn.lastfm.vkapi.response.UserItem;
import com.semkagtn.lastfm.vkapi.userwalker.VkUserWalker;

import java.util.List;

/**
 * Created by semkagtn on 08.09.15.
 */
public class SimpleVkUserWithAudiosWalker implements VkUserWithAudiosWalker {

    private VkUserWalker userWalker;
    private VkApi api;

    private int audiosRequestLimit;

    public SimpleVkUserWithAudiosWalker(int audiosRequestLimit, VkUserWalker userWalker, VkApi api) {
        this.audiosRequestLimit = audiosRequestLimit;
        this.userWalker = userWalker;
        this.api = api;
    }

    @Override
    public UserWithAudios nextUser() {
        UserItem user = null;
        List<AudioItem> audios = null;
        while (audios == null) {
            user = userWalker.nextUser();
            AudioGetResponse response = api.audioGet(user.getId(), 0, audiosRequestLimit);
            if (response.getResponse() != null) {
                audios = response.getResponse().getItems();
            }
        }
        return new UserWithAudios(user, audios);
    }
}
