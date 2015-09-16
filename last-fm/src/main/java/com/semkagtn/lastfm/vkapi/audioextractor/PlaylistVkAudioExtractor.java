package com.semkagtn.lastfm.vkapi.audioextractor;

import com.semkagtn.lastfm.vkapi.VkApi;
import com.semkagtn.lastfm.vkapi.response.AudioGetResponse;
import com.semkagtn.lastfm.vkapi.response.AudioItem;

import java.util.List;

/**
 * Created by semkagtn on 12.09.15.
 */
public class PlaylistVkAudioExtractor implements VkAudioExtractor {

    private VkApi api;
    private int audioLimit;

    public PlaylistVkAudioExtractor(VkApi api, int audioLimit) {
        this.api = api;
        this.audioLimit = audioLimit;
    }

    public PlaylistVkAudioExtractor(VkApi api) {
        this(api, Integer.MAX_VALUE);
    }

    @Override
    public List<AudioItem> getAudios(int userId) {
        List<AudioItem> result = null;
        AudioGetResponse response = api.audioGet(userId, 0, audioLimit);
        if (response.getResponse() != null) {
            result = response.getResponse().getItems();
        }
        return result;
    }
}
