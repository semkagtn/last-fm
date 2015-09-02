package com.semkagtn.lastfm.lastfmapi;

import com.semkagtn.lastfm.lastfmapi.response.ArtistGetInfoResponse;
import com.semkagtn.lastfm.lastfmapi.response.BaseLastFmResponse;
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
 * Created by semkagtn on 02.09.15.
 */
public class LastFmApi {

    private static final String API_URL = "http://ws.audioscrobbler.com/2.0/";
    private static final String ENCODING = "UTF-8";

    private static final String ARTIST_GET_INFO = "artist.getInfo";

    private Logger logger = Logger.getLogger("VK.API");
    private LastFmApiConfig config;
    private CloseableHttpClient client;
    private ObjectMapper objectMapper;

    public LastFmApi(LastFmApiConfig config) {
        this.config = config;
        this.client = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setSocketTimeout(config.getTimeout())
                        .setConnectTimeout(config.getTimeout())
                        .build())
                .build();
        this.objectMapper = new ObjectMapper();
    }

    private <T extends BaseLastFmResponse> T call(String method, List<NameValuePair> parameters, Class<T> resultClass) {
        parameters.add(new BasicNameValuePair("api_key", config.getApiKey()));
        parameters.add(new BasicNameValuePair("method", method));
        parameters.add(new BasicNameValuePair("format", "json"));
        InputStream responseStream = null;
        T result = null;
        boolean responseReceived = false;
        while (!responseReceived) {
            try {
                String requestString = API_URL + "?" + URLEncodedUtils.format(parameters, ENCODING);
                HttpGet getRequest = new HttpGet(requestString);
                HttpResponse response = client.execute(getRequest);
                logger.info("REQUEST: " + requestString);
                StatusLine status = response.getStatusLine();
                if (status.getStatusCode() != HttpStatus.SC_OK) {
                    String stringStatus = status.getStatusCode() + " " + status.getReasonPhrase();
                    logger.info("RESPONSE: " + stringStatus);
                    throw new LastFmApiError("Something wrong: " + stringStatus);
                }
                responseStream = response.getEntity().getContent();
                String jsonString = IOUtils.toString(responseStream, ENCODING);
                logger.info("RESPONSE: " + jsonString);
                result = objectMapper.readValue(jsonString, resultClass);
                responseReceived = result.getError() == null || result.getError() != 29;
            } catch (IOException e) {
                throw new LastFmApiError(e);
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

    public static class LastFmApiError extends Error {

        public LastFmApiError(String message) {
            super(message);
        }

        public LastFmApiError(Throwable throwable) {
            super(throwable);
        }
    }

    public ArtistGetInfoResponse artistGetInfo(String artist) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("artist", artist));
        return call(ARTIST_GET_INFO, parameters, ArtistGetInfoResponse.class);
    }
}
