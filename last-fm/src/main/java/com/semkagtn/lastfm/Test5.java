package com.semkagtn.lastfm;

import com.semkagtn.lastfm.api.Api;
import com.semkagtn.lastfm.api.ApiConfig;
import com.semkagtn.lastfm.api.ApiKeys;
import com.semkagtn.lastfm.api.Track;
import com.semkagtn.lastfm.vkapi.VkAccessTokens;
import com.semkagtn.lastfm.vkapi.VkApi;
import com.semkagtn.lastfm.vkapi.VkApiConfig;
import com.semkagtn.lastfm.vkapi.response.AudioGetResponse;
import com.semkagtn.lastfm.vkapi.response.AudioItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by semkagtn on 02.09.15.
 */
public class Test5 {

    public static void main(String[] args) {
        VkApi vkApi = new VkApi(VkApiConfig.newInstance()
                .withToken(VkAccessTokens.getRandom())
                .build());
        Api lastFmApi = new Api(ApiConfig.newInstance()
                .withApiKey(ApiKeys.getRandom())
                .build());

        AudioGetResponse response = vkApi.audioGet(613960, 0, 1000);
        List<Track> tracks = new ArrayList<>();
        List<AudioItem> audioItems = response.getResponse().getItems();
        for (AudioItem audioItem : audioItems) {
            try {
                Track track = lastFmApi.call(Track.GetInfo.createRequest(audioItem.getArtist(), audioItem.getTitle()));
                tracks.add(track);
            } catch (Api.ResponseError e) {
                continue;
            }
        }
        // Me: 134/174
        // myhighlove: 479/697
        // kelle: 452/763
        // eldes: 593/990
        System.out.println(audioItems.size());
        System.out.println(tracks.stream().filter(track -> track.getTags().size() > 0).count());
    }
}
