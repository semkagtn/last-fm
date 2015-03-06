package com.semkagtn.lastfm.api;

import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
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

    private static final int REQUEST_REPEATS = 3;
    private static final int TIMEOUT = 10_000;
    private static final String API_URL = "http://ws.audioscrobbler.com/2.0/";

    private static final CloseableHttpClient client = HttpClients.custom()
            .setDefaultRequestConfig(RequestConfig.custom()
                    .setSocketTimeout(TIMEOUT)
                    .setConnectTimeout(TIMEOUT)
                    .build())
            .build();

    private static String apiKey = "03269abd0820d42871cda4514e03325e";
    private static String userAgent = "tst";
    private static Logger logger = Logger.getLogger("LastFM.API");

    public static void setApiKey(String apiKey) {
        Api.apiKey = apiKey;
    }

    public static void setUserAgent(String userAgent) {
        Api.userAgent = userAgent;
    }

    public static void enableLogger(boolean enable) {
        logger.setLevel(enable ? Level.ALL : Level.OFF);
    }

    public static <T> T call(Request<T> request) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.addAll(request.getParameters());
        parameters.add(new BasicNameValuePair("api_key", apiKey));
        parameters.add(new BasicNameValuePair("format", "json"));

        JSONObject result = null;
        int requestCount = 0;
        while (result == null) {
            if (requestCount == REQUEST_REPEATS) {
                throw new ConnectionError();
            }
            requestCount++;
            try {
                HttpPost postRequest = new HttpPost(API_URL);
                postRequest.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
                postRequest.setHeader("User-Agent", userAgent);
                logger.info("REQUEST: " + EntityUtils.toString(postRequest.getEntity()));
                HttpResponse response = client.execute(postRequest);
                StatusLine status = response.getStatusLine();
                if (status.getStatusCode() != HttpStatus.SC_OK) {
                    continue;
                }
                InputStream responseStream = response.getEntity().getContent();
                result = new JSONObject(IOUtils.toString(responseStream, "UTF-8"));
                logger.info("RESPONSE: " + result.toString());
                responseStream.close();
            } catch (IOException e) {
                continue;
            }
            if (result.has("error")) {
                throw new CallError(result.toString());
            }
        }
        return request.parseResponse(result);
    }


    public static class CallError extends Error {

        public CallError(String message) {
            super(message);
        }
    }

    public static class ConnectionError extends Error {

    }
}
