package com.semkagtn.lastfm;

import com.semkagtn.lastfm.lastfmapi.LastFmApi;
import com.semkagtn.lastfm.lastfmapi.LastFmApiConfig;
import com.semkagtn.lastfm.lastfmapi.LastFmApiKeys;
import com.semkagtn.lastfm.lastfmapi.response.ArtistGetInfoResponse;
import com.semkagtn.lastfm.lastfmapi.response.TagItem;
import com.semkagtn.lastfm.vkapi.VkAccessTokens;
import com.semkagtn.lastfm.vkapi.VkApi;
import com.semkagtn.lastfm.vkapi.VkApiConfig;
import com.semkagtn.lastfm.vkapi.response.AudioGetResponse;
import com.semkagtn.lastfm.vkapi.response.AudioItem;

import java.util.stream.Collectors;

/**
 * Created by semkagtn on 02.09.15.
 */
public class Test6 {

    public static void main(String[] args) {
        VkApi vkApi = new VkApi(VkApiConfig.newInstance()
                .withToken(VkAccessTokens.getRandom())
                .build());
        LastFmApi lastFmApi = new LastFmApi(LastFmApiConfig.newInstance()
                .withApiKey(LastFmApiKeys.getRandom())
                .build());

        AudioGetResponse audioGetResponse = vkApi.audioGet(597538, 0, 1000);
        for (AudioItem audioItem : audioGetResponse.getResponse().getItems()) {
            ArtistGetInfoResponse artistGetInfoResponse = lastFmApi.artistGetInfo(audioItem.getArtist());
            if (artistGetInfoResponse.getError() == null) {
                System.out.println(artistGetInfoResponse.getArtist().getTags().getTag()
                        .stream()
                        .map(TagItem::getName)
                        .collect(Collectors.toList()));
            }
        }
    }
}
