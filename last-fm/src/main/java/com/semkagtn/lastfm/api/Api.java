package com.semkagtn.lastfm.api;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by semkagtn on 3/6/15.
 */
public class Api {

    private static final String API_URL = "http://ws.audioscrobbler.com/2.0/";

    private static final int DEFAULT_REQUEST_REPEATS = 5;
    private static final int DEFAULT_CONNECTION_TIMEOUT = 20_000;
    private static final String DEFAULT_API_KEY = "03269abd0820d42871cda4514e03325e";
    private static final String DEFAULT_USER_AGENT = "tst";

    private static Logger logger = Logger.getLogger("LastFM.API");
    private static CloseableHttpClient client;
    private static String apiKey;
    private static String userAgent;
    private static int requestRepeats;

    static {
        setRequestRepeats(DEFAULT_REQUEST_REPEATS);
        setTimeout(DEFAULT_CONNECTION_TIMEOUT);
        setApiKey(DEFAULT_API_KEY);
        setUserAgent(DEFAULT_USER_AGENT);
        enableLogger(true);
    }

    public static void setRequestRepeats(int requestRepeats) {
        Api.requestRepeats = requestRepeats;
    }

    public static void setTimeout(int timeout) {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                // WTF??
            }
        }
        client = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setSocketTimeout(timeout)
                        .setConnectTimeout(timeout)
                        .build())
                .build();
    }

    public static void setApiKey(String apiKey) {
        Api.apiKey = apiKey;
    }

    public static void setUserAgent(String userAgent) {
        Api.userAgent = userAgent;
    }

    public static void enableLogger(boolean enable) {
        logger.setLevel(enable ? Level.ALL : Level.OFF);
    }

    public static <T> T call(Request<T> request) throws NotJsonInResponseException {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.addAll(request.getParameters());
        parameters.add(new BasicNameValuePair("api_key", apiKey));
        parameters.add(new BasicNameValuePair("format", "json"));

        JSONObject result = null;
        int requestCount = 0;
        while (result == null) {
            if (requestCount == requestRepeats) {
                throw new ConnectionError();
            }
            requestCount++;
            InputStream responseStream = null;
            try {
                HttpPost postRequest = new HttpPost(API_URL);
                postRequest.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
                postRequest.setHeader("User-Agent", userAgent);
                HttpResponse response = client.execute(postRequest);
                StatusLine status = response.getStatusLine();
                if (status.getStatusCode() != HttpStatus.SC_OK) {
                    continue;
                }
                responseStream = response.getEntity().getContent();
                String jsonString = IOUtils.toString(responseStream, "UTF-8");
                logger.info("REQUEST: " + EntityUtils.toString(postRequest.getEntity()) + "\n"
                        + "RESPONSE: " + jsonString.trim());
                try {
                    result = new JSONObject(jsonString);
                } catch (JSONException e) {
                    throw new NotJsonInResponseException();
                }
            } catch (IOException e) {
                logger.info("Something wrong: " + e.getMessage());
                continue;
            } finally {
                try {
                    if (responseStream != null) {
                        responseStream.close();
                    }
                } catch (IOException e) {
                    // WTF??
                }
            }
            if (result.has("error")) {
                throw new ResponseError(result.getString("message"));
            }
        }
        return request.parseResponse(result);
    }


    public static class ResponseError extends Error {

        public ResponseError(String message) {
            super(message);
        }
    }

    public static class ConnectionError extends Error {

    }

    public static class NotJsonInResponseException extends Exception {

    }
}
