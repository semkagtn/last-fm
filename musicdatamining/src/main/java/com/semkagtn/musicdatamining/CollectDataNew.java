package com.semkagtn.musicdatamining;

import com.semkagtn.musicdatamining.database.DatabaseReaderHelper;
import com.semkagtn.musicdatamining.database.DatabaseWriterHelper;
import com.semkagtn.musicdatamining.database.EntityConverter;
import com.semkagtn.musicdatamining.httpclient.HttpClient;
import com.semkagtn.musicdatamining.httpclient.HttpClientConfig;
import com.semkagtn.musicdatamining.vkapi.VkAccessTokens;
import com.semkagtn.musicdatamining.vkapi.VkApi;
import com.semkagtn.musicdatamining.vkapi.response.*;
import com.semkagtn.musicdatamining.vkapi.userwalker.PredicateVkUserWalker;
import com.semkagtn.musicdatamining.vkapi.userwalker.RandomRecursiveVkUserWalker;
import com.semkagtn.musicdatamining.vkapi.userwalker.VkUserWalker;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.semkagtn.musicdatamining.vkapi.userwalker.UserPredicates.hasAge;
import static com.semkagtn.musicdatamining.vkapi.userwalker.UserPredicates.hasGender;

/**
 * Created by semkagtn on 25.02.16.
 */
public class CollectDataNew {

    private static final int USER_COUNT = 34;
    private static final int MIN_AUDIOS = 30;
    private static final int MAX_AUDIOS = 50;

    private static DatabaseWriterHelper databaseWriterHelper = new DatabaseWriterHelper();
    private static DatabaseReaderHelper databaseReaderHelper = new DatabaseReaderHelper();
    private static HttpClient httpClient = new HttpClient(HttpClientConfig
            .newInstance()
            .build());
    private static VkApi officialApi = VkApi.official(httpClient, VkAccessTokens.getRandom());
    private static VkApi unofficialApi = VkApi.unofficial(httpClient, VkAccessTokens.getUnofficialToken());
    private static VkUserWalker userWalker = newUserWalker(officialApi, MIN_AUDIOS);

    public static void main(String[] args) {
        try {
            for (int i = 0; i < USER_COUNT; ) {
                UserItem userItem = userWalker.nextUser();
                DUsers userEntity = EntityConverter.convertUserToDUser(userItem);
                boolean userInserted = databaseWriterHelper.insert(userEntity);
                if (!userInserted) {
                    continue;
                }
                AudioGetResponse audioGetResponse = null;
                while (audioGetResponse == null) {
                    try {
                        audioGetResponse = officialApi.audioGet(userItem.getId(), 0, MAX_AUDIOS);
                    } catch (VkApi.CaptchaNeededError e) {
                        Captcha captcha = e.getCaptcha();
                        officialApi.setCaptcha(captcha);
                    }
                }
                boolean userTracksInserted = insertUserAudios(userEntity, audioGetResponse);
                if (!userTracksInserted) {
                    continue;
                }
                i++;
            }
        } finally {
            databaseReaderHelper.close();
            databaseWriterHelper.close();
        }
    }

    private static boolean insertUserAudios(DUsers userEntity, AudioGetResponse audioGetResponse) {
        if (audioGetResponse == null ||
                audioGetResponse.getResponse() == null ||
                audioGetResponse.getResponse().getItems() == null ||
                audioGetResponse.getResponse().getItems().size() < MIN_AUDIOS) {
            return false;
        }
        List<AudioItem> audios = audioGetResponse.getResponse().getItems();
        for (AudioItem audio : audios) {
            DTracks trackEntity = EntityConverter.convertTrackToDTrack(audio);
            databaseWriterHelper.insert(trackEntity);
            DUsersTracks usersTracksEntity = new DUsersTracks();
            usersTracksEntity.setDUsers(userEntity);
            usersTracksEntity.setDTracks(trackEntity);
            databaseWriterHelper.insert(usersTracksEntity);
            if (!databaseReaderHelper.dTracksUsersExists(trackEntity.getId())) {
                String query = trackEntity.getArtistName() + " " + trackEntity.getTrackName();
                AudioSearchResponseOld audioSearchResponse = unofficialApi.audioSearchOld(query, true, 300, 0);
                insertTracksUsers(trackEntity, audioSearchResponse);
            }
        }
        return true;
    }

    private static void insertTracksUsers(DTracks trackEntity, AudioSearchResponseOld audioSearchResponse) {
        if (audioSearchResponse == null ||
                audioSearchResponse.getResponse() == null ||
                audioSearchResponse.getResponse().size() == 0) {
            return;
        }
        List<String> ownerIds = audioSearchResponse.getResponse()
                .stream()
                .filter(audio -> audio.getOwnerId() > 0)
                .map(AudioItem::getOwnerId)
                .distinct()
                .map(String::valueOf)
                .collect(Collectors.toList());
        UsersGetResponse usersGetResponse = officialApi.usersGet(ownerIds);
        if (usersGetResponse == null || usersGetResponse.getResponse() == null) {
            return;
        }
        usersGetResponse.getResponse()
                .stream()
                .filter(hasAge().or(hasGender()))
                .map(EntityConverter::convertUserToDUser)
                .forEach(userEntity -> {
                    databaseWriterHelper.insert(userEntity);
                    DTracksUsers tracksUsersEntity = new DTracksUsers();
                    tracksUsersEntity.setDTracks(trackEntity);
                    tracksUsersEntity.setDUsers(userEntity);
                    databaseWriterHelper.insert(tracksUsersEntity);
                });
    }

    private static VkUserWalker newUserWalker(final VkApi officialApi, final int minAudios) {
        return new VkUserWalker() {

            private VkUserWalker userWalker = new PredicateVkUserWalker(
                    new RandomRecursiveVkUserWalker(2, 3, officialApi),
                    hasAge().and(hasGender()));

            @Override
            public UserItem nextUser() {
                UserItem userItem;
                while (true) {
                    userItem = userWalker.nextUser();
                    UsersGetResponse usersGetResponse = officialApi.usersGet(
                            Arrays.asList(String.valueOf(userItem.getId())));
                    if (usersGetResponse != null &&
                            usersGetResponse.getResponse() != null &&
                            usersGetResponse.getResponse().size() == 1 &&
                            usersGetResponse.getResponse().get(0).getCounters() != null &&
                            usersGetResponse.getResponse().get(0).getCounters().getAudios() != null &&
                            usersGetResponse.getResponse().get(0).getCounters().getAudios() >= minAudios) {
                        return userItem;
                    }
                }
            }
        };
    }
}
