package com.semkagtn.musicdatamining.vkapi;

import com.semkagtn.musicdatamining.httpclient.HttpClient;
import com.semkagtn.musicdatamining.utils.JsonUtils;
import com.semkagtn.musicdatamining.vkapi.enums.IdsSort;
import com.semkagtn.musicdatamining.vkapi.response.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by semkagtn on 30.08.15.
 */
public class VkApi {

    private static final String UNOFFICIAL_API_URL = "http://api.wm-scripts.ru/api.php";
    private static final String API_URL = "https://api.vk.com/method/";
    private static final String API_VERSION = "5.45";
    private static final String FORMAT = "json";
    private static final String FIELDS = "sex,bdate,counters,photo_50";

    private static final String AUDIO_GET = "audio.get";
    private static final String AUDIO_SEARCH = "audio.search";
    private static final String GROUPS_GET_MEMBERS = "groups.getMembers";
    private static final String FRIENDS_GET = "friends.get";
    private static final String USERS_GET = "users.get";
    private static final String WALL_GET = "wall.get";
    private static final String EXECUTE_GET_ATTACHMENTS = "execute.getAttachments";
    private static final String EXECUTE_GET_GROUP_MEMBERS = "execute.getGroupMembers";

    private static final int TOO_MANY_REQUESTS_WAIT = 1_100;
    private static final int CAPTCHA_NEEDED_WAIT = 60_000;

    private static BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));

    private HttpClient client;
    private String token;
    private String apiUrl;

    private Captcha captcha;

    public static VkApi official(HttpClient client, String token) {
        return new VkApi(client, token, API_URL);
    }

    public static VkApi unofficial(HttpClient client, String token) {
        return new VkApi(client, token, UNOFFICIAL_API_URL);
    }

    private VkApi(HttpClient client, String token, String apiUrl) {
        this.client = client;
        this.token = token;
        this.apiUrl = apiUrl;
    }

    public void setCaptcha(Captcha captcha) {
        this.captcha = captcha;
    }

    private <T extends BaseVkResponse> T call(String method, List<NameValuePair> parameters, Class<T> resultClass) {
        parameters.add(new BasicNameValuePair("v", API_VERSION));
        parameters.add(new BasicNameValuePair("format", FORMAT));

        T result = null;
        boolean resultReceived = false;
        while (!resultReceived) {
            String requestUrl;
            if (apiUrl.equals(API_URL)) {
                requestUrl = apiUrl + method;
                parameters.add(new BasicNameValuePair("access_token", token));
            } else {
                requestUrl = apiUrl;
                parameters.add(new BasicNameValuePair("method", method));
                parameters.add(new BasicNameValuePair("key", token));
            }
            String response = client.request(requestUrl, parameters);
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
                    throw new CaptchaNeededError(result.getError());
                } else if (errorCode == VkApiErrors.FLOOD_CONTROL) {
                    throw new ApiError(result.getError());
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

        private VkError vkError;

        public ApiError(VkError vkError) {
            super(vkError.toString());
            this.vkError = vkError;
        }

        public VkError getVkError() {
            return vkError;
        }
    }

    public static class CaptchaNeededError extends ApiError {

        public CaptchaNeededError(VkError vkError) {
            super(vkError);
        }

        public Captcha getCaptcha() {
            System.out.println(getVkError().getCaptchaImg().replaceAll("\\\\/", "/"));
            System.out.print("ENTER CAPTCHA: ");
            String captchaKey = null;
            try {
                captchaKey = userInputReader.readLine().trim();
            } catch (IOException e) {
                // WTF???
            }
            return new Captcha(getVkError().getCaptchaSid(), captchaKey);
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
        if (captcha != null) {
            parameters.add(new BasicNameValuePair("captcha_sid", captcha.getCaptchaSid()));
            parameters.add(new BasicNameValuePair("captcha_key", captcha.getCaptchaKey()));
            captcha = null;
        }
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

    private <T extends BaseVkResponse> T audioSearch(String query,
                                                     Boolean autoComplete,
                                                     Integer count,
                                                     Integer offset,
                                                     Class<T> clazz) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("q", query));
        if (autoComplete != null) {
            parameters.add(new BasicNameValuePair("auto_complete", autoComplete ? "1" : "0"));
        }
        if (count != null) {
            parameters.add(new BasicNameValuePair("count", String.valueOf(count)));
        }
        if (offset != null) {
            parameters.add(new BasicNameValuePair("offset", String.valueOf(offset)));
        }
        return call(AUDIO_SEARCH, parameters, clazz);
    }

    public AudioSearchResponse audioSearchNew(String query, Boolean autoComplete, Integer count, Integer offset) {
        return audioSearch(query, autoComplete, count, offset, AudioSearchResponse.class);
    }

    public AudioSearchResponseOld audioSearchOld(String query, Boolean autoComplete, Integer count, Integer offset) {
        return audioSearch(query, autoComplete, count, offset, AudioSearchResponseOld.class);
    }

    public GroupsGetMembersResponse groupsGetMembers(long groupId, IdsSort sort) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("group_id", String.valueOf(groupId)));
        parameters.add(new BasicNameValuePair("sort", sort.getValue()));
        return call(GROUPS_GET_MEMBERS, parameters, GroupsGetMembersResponse.class);
    }

    public ExecuteGetGroupMembersResponse executeGetGroupMembers(long groupId, long offset, IdsSort sort) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("group_id", String.valueOf(groupId)));
        parameters.add(new BasicNameValuePair("offset", String.valueOf(offset)));
        parameters.add(new BasicNameValuePair("sort", sort.getValue()));
        return call(EXECUTE_GET_GROUP_MEMBERS, parameters, ExecuteGetGroupMembersResponse.class);
    }
}
