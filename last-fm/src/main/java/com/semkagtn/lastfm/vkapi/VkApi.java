package com.semkagtn.lastfm.vkapi;

import com.semkagtn.lastfm.httpclient.HttpClient;
import com.semkagtn.lastfm.vkapi.response.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by semkagtn on 30.08.15.
 */
public class VkApi {

    private static final String API_URL = "https://api.vk.com/method/";
    private static final String API_VERSION = "5.37";
    private static final String FIELDS = "sex,bdate,music";

    private static final String AUDIO_GET = "audio.get";
    private static final String USERS_GET = "users.get";
    private static final String FRIENDS_GET = "friends.get";
    private static final String WALL_GET = "wall.get";

    private HttpClient client;
    private String token;
    private ObjectMapper objectMapper;

    public VkApi(HttpClient client, String token) {
        this.client = client;
        this.token = token;
        this.objectMapper = new ObjectMapper();
    }

    private <T extends BaseVkResponse> T call(String method, List<NameValuePair> parameters, Class<T> resultClass) {
        parameters.add(new BasicNameValuePair("access_token", token));
        parameters.add(new BasicNameValuePair("v", API_VERSION));

        T result = null;
        boolean resultReceived = false;
        while (!resultReceived) {
            String response = client.request(API_URL + method, parameters);
            try {
                result = objectMapper.readValue(response, resultClass);
            } catch (IOException e) {
                throw new VkResponseParseError(e);
            }
            resultReceived = result.getError() == null || result.getError().getErrorCode() != 6;
        }
        return result;
    }

    public static class VkResponseParseError extends Error {

        public VkResponseParseError(Throwable cause) {
            super(cause);
        }
    }

    public UsersGetResponse usersGet(List<String> userIds) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("user_ids", String.join(",", userIds)));
        parameters.add(new BasicNameValuePair("fields", FIELDS));
        return call(USERS_GET, parameters, UsersGetResponse.class);
    }

    public AudioGetResponse audioGet(int ownerId, int offset, int count) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("owner_id", String.valueOf(ownerId)));
        parameters.add(new BasicNameValuePair("offset", String.valueOf(offset)));
        parameters.add(new BasicNameValuePair("count", String.valueOf(count)));
        parameters.add(new BasicNameValuePair("need_user", "0"));
        return call(AUDIO_GET, parameters, AudioGetResponse.class);
    }

    public FriendsGetResponse friendsGet(int userId, int offset, int count) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("user_id", String.valueOf(userId)));
        parameters.add(new BasicNameValuePair("fields", FIELDS));
        parameters.add(new BasicNameValuePair("offset", String.valueOf(offset)));
        parameters.add(new BasicNameValuePair("count", String.valueOf(count)));
        return call(FRIENDS_GET, parameters, FriendsGetResponse.class);
    }

    public WallGetResponse wallGet(int userId, int offset, int count, WallGetFilter filter) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("owner_id", String.valueOf(userId)));
        parameters.add(new BasicNameValuePair("offset", String.valueOf(offset)));
        parameters.add(new BasicNameValuePair("count", String.valueOf(count)));
        parameters.add(new BasicNameValuePair("filter", filter.getValue()));
        return call(WALL_GET, parameters, WallGetResponse.class);
    }
}
