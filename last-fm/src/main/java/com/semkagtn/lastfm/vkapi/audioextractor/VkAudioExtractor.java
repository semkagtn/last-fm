package com.semkagtn.lastfm.vkapi.audioextractor;

import com.semkagtn.lastfm.vkapi.response.AudioItem;

import java.util.List;

/**
 * Created by semkagtn on 12.09.15.
 */
public interface VkAudioExtractor {

    List<AudioItem> getAudios(int userId);
}
