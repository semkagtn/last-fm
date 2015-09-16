package com.semkagtn.musicdatamining.vkapi;

import com.semkagtn.musicdatamining.httpclient.HttpClient;
import com.semkagtn.musicdatamining.utils.JsonUtils;
import com.semkagtn.musicdatamining.vkapi.response.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by semkagtn on 30.08.15.
 */
public class VkApi {

    private static final String API_URL = "https://api.vk.com/method/";
    private static final String API_VERSION = "5.37";
    private static final String FIELDS = "sex,bdate,counters,last_seen,photo_50";

    private static final String AUDIO_GET = "audio.get";
    private static final String USERS_GET = "users.get";
    private static final String FRIENDS_GET = "friends.get";
    private static final String WALL_GET = "wall.get";
    private static final String EXECUTE_GET_ATTACHMENTS = "execute.getAttachments";

    private static final int TOO_MANY_REQUESTS_WAIT = 1_100;
    private static final int CAPTCHA_NEEDED_WAIT = 60_000;

    private HttpClient client;
    private String token;

    public VkApi(HttpClient client, String token) {
        this.client = client;
        this.token = token;
    }

    private <T extends BaseVkResponse> T call(String method, List<NameValuePair> parameters, Class<T> resultClass) {
        parameters.add(new BasicNameValuePair("access_token", token));
        parameters.add(new BasicNameValuePair("v", API_VERSION));

        T result = null;
        boolean resultReceived = false;
        while (!resultReceived) {
            String response = client.request(API_URL + method, parameters);
            result = JsonUtils.fromJson(response, resultClass);
            if (result != null && result.getError() != null) {
                int errorCode = result.getError().getErrorCode();
                if (errorCode == VkApiErrors.TOO_MANY_REQUESTS_PER_SECOND) {
                    try {
                        Thread.sleep(TOO_MANY_REQUESTS_WAIT);
                    } catch (InterruptedException e) {
                        // WTF??
                    }
                } else if (errorCode == VkApiErrors.CAPTCHA_NEEDED) {
                    try {
                        Thread.sleep(CAPTCHA_NEEDED_WAIT);
                    } catch (InterruptedException e) {
                        // WTF??
                    }
                } else if (errorCode == VkApiErrors.FLOOD_CONTROL || errorCode == VkApiErrors.INTERNAL_SERVER_ERROR) {
                    throw new ApiError(result.getError().toString());
                } else {
                    resultReceived = true;
                }
            } else {
                resultReceived = true;
            }
        }
        return result;
    }

    public static class ApiError extends Error {

        public ApiError(String message) {
            super(message);
        }
    }

    public UsersGetResponse usersGet(List<String> userIds) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("user_ids", String.join(",", userIds)));
        parameters.add(new BasicNameValuePair("fields", FIELDS));
        return call(USERS_GET, parameters, UsersGetResponse.class);
    }

    public AudioGetResponse audioGet(int ownerId, Integer offset, Integer count) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("owner_id", String.valueOf(ownerId)));
        if (offset != null) {
            parameters.add(new BasicNameValuePair("offset", String.valueOf(offset)));
        }
        if (count != null) {
            parameters.add(new BasicNameValuePair("count", String.valueOf(count)));
        }
        parameters.add(new BasicNameValuePair("need_user", "0"));
        return call(AUDIO_GET, parameters, AudioGetResponse.class);
    }

    public FriendsGetResponse friendsGet(int userId, Integer offset, Integer count) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("user_id", String.valueOf(userId)));
        parameters.add(new BasicNameValuePair("fields", FIELDS));
        if (offset != null) {
            parameters.add(new BasicNameValuePair("offset", String.valueOf(offset)));
        }
        if (count != null) {
            parameters.add(new BasicNameValuePair("count", String.valueOf(count)));
        }
        return call(FRIENDS_GET, parameters, FriendsGetResponse.class);
    }

    public WallGetResponse wallGet(int userId, Integer offset, Integer count, WallGetFilter filter) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("owner_id", String.valueOf(userId)));
        if (offset != null) {
            parameters.add(new BasicNameValuePair("offset", String.valueOf(offset)));
        }
        if (count != null) {
            parameters.add(new BasicNameValuePair("count", String.valueOf(count)));
        }
        if (filter != null) {
            parameters.add(new BasicNameValuePair("filter", filter.getValue()));
        }
        return call(WALL_GET, parameters, WallGetResponse.class);
    }

    public ExecuteGetAttachmentsResponse executeGetAttachments(int userId, int offset, WallGetFilter filter) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("owner_id", String.valueOf(userId)));
        parameters.add(new BasicNameValuePair("offset", String.valueOf(offset)));
        if (filter != null) {
            parameters.add(new BasicNameValuePair("filter", filter.getValue()));
        }
        return call(EXECUTE_GET_ATTACHMENTS, parameters, ExecuteGetAttachmentsResponse.class);
    }
}
