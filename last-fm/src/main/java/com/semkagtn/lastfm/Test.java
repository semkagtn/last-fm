package com.semkagtn.lastfm;

import com.semkagtn.lastfm.httpclient.HttpClient;
import com.semkagtn.lastfm.httpclient.HttpClientConfig;
import com.semkagtn.lastfm.lastfmapi.LastFmApi;
import com.semkagtn.lastfm.lastfmapi.LastFmApiKeys;
import com.semkagtn.lastfm.lastfmapi.response.TrackGetInfoResponse;
import com.semkagtn.lastfm.vkapi.VkAccessTokens;
import com.semkagtn.lastfm.vkapi.VkApi;
import com.semkagtn.lastfm.vkapi.response.AudioGetResponse;
import com.semkagtn.lastfm.vkapi.response.AudioItem;
import com.semkagtn.lastfm.vkapi.response.UserItem;
import com.semkagtn.lastfm.vkapi.userwalker.RandomRecursiveVkUserWalker;
import com.semkagtn.lastfm.vkapi.userwalker.VkUserWalker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by semkagtn on 03.09.15.
 */
public class Test {

    public static void main(String[] args) {
        HttpClientConfig config = HttpClientConfig.newInstance().build();
        HttpClient httpClient = new HttpClient(config);

        String apiKey = LastFmApiKeys.getRandom();
        String accessToken = VkAccessTokens.getRandom();

        LastFmApi lastFmApi = new LastFmApi(httpClient, apiKey);
        VkApi vkApi = new VkApi(httpClient, accessToken);

        VkUserWalker userWalker = new RandomRecursiveVkUserWalker(2, 5, vkApi);
        List<UserItem> users = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            users.add(userWalker.nextUser());
        }
        System.out.println();
    }
}
