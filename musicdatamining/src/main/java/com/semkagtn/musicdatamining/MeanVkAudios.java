package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.httpclient.HttpClient;
import com.semkagtn.musicdatamining.httpclient.HttpClientConfig;
import com.semkagtn.musicdatamining.vkapi.VkAccessTokens;
import com.semkagtn.musicdatamining.vkapi.VkApi;
import com.semkagtn.musicdatamining.vkapi.response.UserItem;
import com.semkagtn.musicdatamining.vkapi.userwalker.RandomRecursiveVkUserWalker;
import com.semkagtn.musicdatamining.vkapi.userwalker.VkUserWalker;

/**
 * Created by semkagtn on 25.02.16.
 */
public class MeanVkAudios {

    public static void main(String[] args) {
        HttpClient httpClient = new HttpClient(HttpClientConfig.newInstance()
                .withLoggerEnabled(false)
                .build());
        String token = VkAccessTokens.getRandom();
        VkApi api = VkApi.official(httpClient, token);
        VkUserWalker userWalker = new RandomRecursiveVkUserWalker(1, 20, api);
        long totalAudios = 0;
        long userCount = 0;
        final long maxUserCount = 100_000;
        for (int i = 0; i < maxUserCount; i++) {
            System.out.println(i + 1);
            UserItem userItem = userWalker.nextUser();
            if (userItem != null
                    && userItem.getCounters() != null
                    && userItem.getCounters().getAudios() != null) {
                totalAudios += userItem.getCounters().getAudios();
                userCount++;
            }
        }
        System.out.println(String.format("mean audios: %f", (double) totalAudios / userCount));
    }
}
