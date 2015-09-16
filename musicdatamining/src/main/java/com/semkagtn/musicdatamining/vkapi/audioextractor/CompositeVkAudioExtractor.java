package com.semkagtn.musicdatamining.vkapi.audioextractor;

import com.semkagtn.musicdatamining.vkapi.response.AudioItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 16.09.15.
 */
public class CompositeVkAudioExtractor implements VkAudioExtractor {

    private List<VkAudioExtractor> audioExtractors;
    private int audiosLimit;

    public CompositeVkAudioExtractor(List<VkAudioExtractor> audioExtractors, int audiosLimit) {
        this.audioExtractors = audioExtractors;
        this.audiosLimit = audiosLimit;
    }

    public CompositeVkAudioExtractor(List<VkAudioExtractor> audioExtractors) {
        this(audioExtractors, Integer.MAX_VALUE);
    }

    @Override
    public List<AudioItem> getAudios(int userId) {
        List<AudioItem> result = new ArrayList<>();
        for (VkAudioExtractor audioExtractor : audioExtractors) {
            List<AudioItem> audios = audioExtractor.getAudios(userId);
            if (audios != null) {
                result.addAll(audios);
            }
        }
        return result.stream().limit(audiosLimit).collect(Collectors.toList());
    }
}
