package com.semkagtn.musicdatamining.vkapi.audioextractor;

import com.semkagtn.musicdatamining.vkapi.VkApi;
import com.semkagtn.musicdatamining.vkapi.response.AudioAttachment;
import com.semkagtn.musicdatamining.vkapi.response.AudioItem;
import com.semkagtn.musicdatamining.vkapi.response.ExecuteGetAttachmentsResponse;
import com.semkagtn.musicdatamining.vkapi.response.WallGetFilter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 12.09.15.
 */
public class WallVkAudioExtractor implements VkAudioExtractor {

    private VkApi api;
    private int audioLimit;

    public WallVkAudioExtractor(VkApi api, int audioLimit) {
        this.api = api;
        this.audioLimit = audioLimit;
    }

    public WallVkAudioExtractor(VkApi api) {
        this(api, Integer.MAX_VALUE);
    }

    @Override
    public List<AudioItem> getAudios(int userId) {
        List<AudioItem> result = null;
        ExecuteGetAttachmentsResponse response = api.executeGetAttachments(userId, 0, WallGetFilter.OWNER);
        if (response.getResponse() != null) {
            result = response.getResponse().stream()
                    .filter(x -> x != null)
                    .flatMap(Collection::stream)
                    .map(AudioAttachment::getAudio)
                    .filter(x -> x != null)
                    .collect(Collectors.toList());
        }
        return result;
    }
}
