package com.semkagtn.lastfm;

import com.semkagtn.lastfm.database.Database;
import com.semkagtn.lastfm.httpclient.HttpClient;
import com.semkagtn.lastfm.httpclient.HttpClientConfig;
import com.semkagtn.lastfm.lastfmapi.LastFmApi;
import com.semkagtn.lastfm.utils.EntityConverter;
import com.semkagtn.lastfm.vkapi.VkApi;
import com.semkagtn.lastfm.vkapi.response.AudioItem;
import com.semkagtn.lastfm.vkapi.userwalker.PredicateVkUserWalker;
import com.semkagtn.lastfm.vkapi.userwalker.RandomRecursiveVkUserWalker;
import com.semkagtn.lastfm.vkapi.userwalker.VkUserWalker;
import com.semkagtn.lastfm.vkapi.userwithaudioswalker.PredicateVkUserWithAudiosWalker;
import com.semkagtn.lastfm.vkapi.userwithaudioswalker.SimpleVkUserWithAudiosWalker;
import com.semkagtn.lastfm.vkapi.userwithaudioswalker.UserWithAudios;
import com.semkagtn.lastfm.vkapi.userwithaudioswalker.VkUserWithAudiosWalker;

import static com.semkagtn.lastfm.vkapi.userwalker.UserPredicates.hasAge;
import static com.semkagtn.lastfm.vkapi.userwalker.UserPredicates.hasGender;
import static com.semkagtn.lastfm.vkapi.userwithaudioswalker.AudioPredicates.minimumAudios;

/**
 * Created by semkagtn on 03.09.15.
 */
public class DataCollector {

    private static final int HTTP_CLIENT_TIMEOUT = 10_000;
    private static final int HTTP_CLIENT_MAX_REPEAT_TIMES = 2;
    private static final boolean HTTP_CLIENT_LOGGER_ENABLED = true;

    private static final int USER_WALKER_DEPTH = 1;
    private static final int USER_WALKER_FRIENDS_LIMIT = 3;

    private static final int AUDIOS_REQUEST_LIMIT = 10;

    private static final int USER_AMOUNT = 10;
    private static final int MINIMUM_AUDIOS = 5;

    private LastFmApi lastFmApi;
    private VkUserWithAudiosWalker userWithAudiosWalker;

    public DataCollector(String lastFmApiKey, String vkAccessToken) {
        HttpClientConfig config = HttpClientConfig
                .newInstance()
                .withTimeout(HTTP_CLIENT_TIMEOUT)
                .withMaxRepeatTimes(HTTP_CLIENT_MAX_REPEAT_TIMES)
                .withLoggerEnabled(HTTP_CLIENT_LOGGER_ENABLED)
                .build();
        HttpClient httpClient = new HttpClient(config);

        lastFmApi = new LastFmApi(httpClient, lastFmApiKey);
        VkApi vkApi = new VkApi(httpClient, vkAccessToken);

        VkUserWalker userWalker = new PredicateVkUserWalker(
                new RandomRecursiveVkUserWalker(USER_WALKER_DEPTH, USER_WALKER_FRIENDS_LIMIT, vkApi),
                hasAge().or(hasGender()));
        userWithAudiosWalker = new PredicateVkUserWithAudiosWalker(
                new SimpleVkUserWithAudiosWalker(AUDIOS_REQUEST_LIMIT, userWalker, vkApi),
                minimumAudios(MINIMUM_AUDIOS));
    }

    public void collect() {
        for (int i = 0; i < USER_AMOUNT;) {
            UserWithAudios user = userWithAudiosWalker.nextUser();
            Users userEntity = EntityConverter.convertUser(user.getUser());
            if (Database.insert(userEntity)) {
                i++;
            } else {
                continue;
            }
            for (AudioItem audio : user.getAudios()) {
                // TODO
            }
        }
    }
}
