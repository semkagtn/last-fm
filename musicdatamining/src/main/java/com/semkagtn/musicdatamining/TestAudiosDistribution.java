package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.database.DatabaseReaderHelper;
import com.semkagtn.musicdatamining.httpclient.HttpClient;
import com.semkagtn.musicdatamining.httpclient.HttpClientConfig;
import com.semkagtn.musicdatamining.vkapi.VkAccessTokens;
import com.semkagtn.musicdatamining.vkapi.VkApi;
import com.semkagtn.musicdatamining.vkapi.response.AudioItem;
import com.semkagtn.musicdatamining.vkapi.response.AudioSearchResponseOld;
import com.semkagtn.musicdatamining.vkapi.response.UserItem;
import com.semkagtn.musicdatamining.vkapi.userwalker.UserPredicates;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by semkagtn on 24.02.16.
 */
public class TestAudiosDistribution {

    public static void main(String[] args) {
        HttpClient httpClient = new HttpClient(HttpClientConfig.newInstance()
                .withLoggerEnabled(true)
                .build());
        String token = VkAccessTokens.getUnofficialToken();
        VkApi api = VkApi.unofficial(httpClient, token);
        VkApi officialApi = VkApi.official(httpClient, VkAccessTokens.getRandom());

        Map<String, Pair<Long, Long>> result = new HashMap<>();
        try (DatabaseReaderHelper database = new DatabaseReaderHelper()) {
            List<Artists> artistEntities = database.topArtists(200);
            for (Artists artistEntity : artistEntities) {
                AudioSearchResponseOld response = api.audioSearchOld(artistEntity.getArtistName(), true, 300, 0);
                List<String> ownerIds = response.getResponse()
                        .stream()
                        .map(AudioItem::getOwnerId)
                        .filter(ownerId -> ownerId > 0)
                        .map(String::valueOf)
                        .distinct()
                        .collect(Collectors.toList());
                List<UserItem> users = officialApi.usersGet(ownerIds).getResponse();
                long userCount = users.size();
                long withAgeCount = users.stream().filter(UserPredicates.hasAge()).count();
                result.put(artistEntity.getArtistName(), new Pair<>(userCount, withAgeCount));
            }
        }
        long totalUserCount = 0L;
        long totalWithAgeCount = 0L;
        for (Map.Entry<String, Pair<Long, Long>> entry : result.entrySet()) {
            String artistName = entry.getKey();
            long userCount = entry.getValue().getKey();
            long withAgeCount = entry.getValue().getValue();
            totalUserCount += userCount;
            totalWithAgeCount += withAgeCount;
            System.out.println(String.format("%s: users - %d , with age - %d",
                    artistName,
                    userCount,
                    withAgeCount));
        }
        double meanUserCount = totalUserCount / result.size();
        double meanWithAgeCount = totalWithAgeCount / result.size();
        System.out.println(String.format("Mean users: %f ; Mean with age: %f",
                meanUserCount,
                meanWithAgeCount));
    }
}
