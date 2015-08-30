package com.semkagtn.lastfm.vkapi;

import com.semkagtn.lastfm.vkapi.response.BaseVkResponse;
import com.semkagtn.lastfm.vkapi.response.audio.get.AudioGetResponse;
import com.semkagtn.lastfm.vkapi.response.friends.get.FriendsGetResponse;
import com.semkagtn.lastfm.vkapi.response.users.get.UsersGetResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by semkagtn on 30.08.15.
 */
public class VkApi {

    private static final String BASE_URL = "https://api.vk.com/method/";
    private static final String VERSION = "5.37";
    private static final String FIELDS = "sex,bdate,site,music";

    private static final String AUDIO_GET = "audio.get";
    private static final String USERS_GET = "users.get";
    private static final String FRIENDS_GET = "friends.get";
    private static final String WALL_GET = "wall.get";

    private Logger logger = Logger.getLogger("VK.API");
    private VkApiConfig config;
    private CloseableHttpClient client;
    private ObjectMapper objectMapper;

    public VkApi(VkApiConfig config) {
        this.config = config;
        this.client = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setSocketTimeout(config.getTimeout())
                        .setConnectTimeout(config.getTimeout())
                        .build())
                .build();
        this.objectMapper = new ObjectMapper();
    }

    private <T extends BaseVkResponse> T call(String method, List<NameValuePair> parameters, Class<T> resultClass) {
        parameters.add(new BasicNameValuePair("access_token", config.getToken()));
        parameters.add(new BasicNameValuePair("v", VERSION));
        InputStream responseStream = null;
        T result = null;
        boolean responseReceived = false;
        while (!responseReceived) {
            try {
                String requestString = BASE_URL + method + "?" + URLEncodedUtils.format(parameters, "UTF-8");
                HttpGet getRequest = new HttpGet(requestString);
                HttpResponse response = client.execute(getRequest);
                logger.info("REQUEST: " + requestString);
                StatusLine status = response.getStatusLine();
                if (status.getStatusCode() != HttpStatus.SC_OK) {
                    String stringStatus = status.getStatusCode() + " " + status.getReasonPhrase();
                    logger.info("RESPONSE: " + stringStatus);
                    throw new VkApiError("Something wrong: " + stringStatus);
                }
                responseStream = response.getEntity().getContent();
                String jsonString = IOUtils.toString(responseStream, "UTF-8");
                logger.info("RESPONSE: " + jsonString);
                result = objectMapper.readValue(jsonString, resultClass);
                if (result.getError() != null && result.getError().getErrorCode() == 6) {
                    responseReceived = false;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // WTF??
                    }
                } else {
                    responseReceived = true;
                }
            } catch (IOException e) {
                throw new VkApiError(e);
            } finally {
                try {
                    if (responseStream != null) {
                        responseStream.close();
                    }
                } catch (IOException e) {
                    // WTF??
                }
            }
        }
        return result;
    }

    public static class VkApiError extends Error {

        public VkApiError(String message) {
            super(message);
        }

        public VkApiError(Throwable throwable) {
            super(throwable);
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
}
