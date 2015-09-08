package com.semkagtn.lastfm.vkapi.userwithaudioswalker;

import com.semkagtn.lastfm.vkapi.response.AudioItem;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by semkagtn on 08.09.15.
 */
public class PredicateVkUserWithAudiosWalker implements VkUserWithAudiosWalker {

    private VkUserWithAudiosWalker userWalker;
    private Predicate<List<AudioItem>> predicate;

    public PredicateVkUserWithAudiosWalker(VkUserWithAudiosWalker userWalker,
                                           Predicate<List<AudioItem>> predicate) {
        this.userWalker = userWalker;
        this.predicate = predicate;
    }

    @Override
    public UserWithAudios nextUser() {
        while (true) {
            UserWithAudios user = userWalker.nextUser();
            List<AudioItem> audios = user.getAudios();
            if (predicate.test(audios)) {
                return user;
            }
        }
    }
}
