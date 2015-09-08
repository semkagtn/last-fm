package com.semkagtn.lastfm.vkapi.userwithaudioswalker;

import com.semkagtn.lastfm.vkapi.response.AudioItem;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by semkagtn on 08.09.15.
 */
public class AudioPredicates {

    public static Predicate<List<AudioItem>> minimumAudios(int minimum) {
        return audios -> audios.size() >= minimum;
    }

    private AudioPredicates() {

    }
}
