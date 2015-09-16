package com.semkagtn.musicdatamining.vkapi.audioextractor;

import com.semkagtn.musicdatamining.vkapi.response.AudioItem;

import java.util.List;

/**
 * Created by semkagtn on 12.09.15.
 */
public interface VkAudioExtractor {

    List<AudioItem> getAudios(int userId);
}
