package com.semkagtn.lastfm.httpclient;

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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by semkagtn on 03.09.15.
 */
public class HttpClient {

    private static final String ENCODING = "UTF-8";

    private CloseableHttpClient client;
    private Logger logger;
    private int maxRepeatTimes;

    public HttpClient(HttpClientConfig config) {
        this.client = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setSocketTimeout(config.getTimeout())
                        .setConnectTimeout(config.getTimeout())
                        .build())
                .build();

        this.logger = Logger.getLogger("VK.API");
        this.logger.setLevel(config.isLoggerEnabled() ? Level.ALL : Level.OFF);

        this.maxRepeatTimes = config.getMaxRepeatTimes();
    }

    public String request(String url, List<NameValuePair> parameters) {
        String requestString = url + "?" + URLEncodedUtils.format(parameters, ENCODING);
        HttpGet getRequest = new HttpGet(requestString);

        String result;
        String errorReason = null;
        int repeats = 0;
        while (repeats < maxRepeatTimes) {
            try {
                HttpResponse response = client.execute(getRequest);
                logger.info("REQUEST: " + requestString);
                StatusLine status = response.getStatusLine();
                if (status.getStatusCode() != HttpStatus.SC_OK) {
                    String stringStatus = status.getStatusCode() + " " + status.getReasonPhrase();
                    logger.info("RESPONSE: " + stringStatus);
                    errorReason = stringStatus;
                } else {
                    InputStream responseStream = response.getEntity().getContent();
                    result = IOUtils.toString(responseStream, ENCODING);
                    logger.info("RESPONSE: " + result);
                    return result;
                }
            } catch (IOException e) {
                errorReason = e.getMessage();
            }
            repeats++;
        }
        throw new HttpClientError(errorReason);
    }

    public static class HttpClientError extends Error {

        public HttpClientError() {

        }

        public HttpClientError(String message) {
            super(message);
        }

        public HttpClientError(Throwable cause) {
            super(cause);
        }
    }
}