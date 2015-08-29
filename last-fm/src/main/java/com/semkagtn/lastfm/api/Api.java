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

    private Logger logger = Logger.getLogger("LastFM.API");
    private ApiConfig config;
    private CloseableHttpClient client;

    public Api(ApiConfig config) {
        this.config = config;
        logger.setLevel(config.isEnableLogger() ? Level.ALL : Level.OFF);
        client = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setSocketTimeout(config.getTimeout())
                        .setConnectTimeout(config.getTimeout())
                        .build())
                .build();
    }

    public <T> T call(Request<T> request) throws NotJsonInResponseError {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.addAll(request.getParameters());
        parameters.add(new BasicNameValuePair("api_key", config.getApiKey()));
        parameters.add(new BasicNameValuePair("format", "json"));

        JSONObject result = null;
        int requestCount = 0;
        while (result == null) {
            if (requestCount == config.getRequestRepeats()) {
                throw new ConnectionError();
            }
            requestCount++;
            InputStream responseStream = null;
            try {
                HttpPost postRequest = new HttpPost(API_URL);
                postRequest.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
                postRequest.setHeader("User-Agent", config.getUserAgent());
                HttpResponse response = client.execute(postRequest);
                logger.info("REQUEST: " + API_URL + "?" + EntityUtils.toString(postRequest.getEntity()));
                StatusLine status = response.getStatusLine();
                if (status.getStatusCode() != HttpStatus.SC_OK) {
                    logger.info("RESPONSE: " + status.getStatusCode() + " " + status.getReasonPhrase());
                    continue;
                }
                responseStream = response.getEntity().getContent();
                String jsonString = IOUtils.toString(responseStream, "UTF-8");
                logger.info("RESPONSE: " + jsonString.trim());
                try {
                    result = new JSONObject(jsonString);
                } catch (JSONException e) {
                    throw new NotJsonInResponseError();
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
                int errorCode = result.getInt("error");
                throw new ResponseError(result.getString("message"), errorCode);
            }
        }
        return request.parseResponse(result);
    }


    public static class ResponseError extends Error {

        private int errorCode;

        public int getErrorCode() {
            return errorCode;
        }

        public ResponseError(String message, int errorCode) {
            super(message);
            this.errorCode = errorCode;
        }
    }

    public static class ConnectionError extends Error {

    }

    public static class NotJsonInResponseError extends Error {

    }
}
