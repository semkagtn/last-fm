package com.semkagtn.musicdatamining.httpclient;

import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by semkagtn on 03.09.15.
 */
public class HttpClient {

    private static final Charset ENCODING = Consts.UTF_8;

    private CloseableHttpClient client;
    private Logger logger;
    private int maxRepeatTimes;

    public HttpClient(HttpClientConfig config) {
        this.client = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setSocketTimeout(config.getTimeout())
                        .setConnectTimeout(config.getTimeout())
                        .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                        .build())
                .build();

        this.logger = Logger.getLogger(this.getClass().getName());
        this.logger.setLevel(config.isLoggerEnabled() ? Level.ALL : Level.OFF);

        this.maxRepeatTimes = config.getMaxRepeatTimes();
    }

    public String request(String url, List<NameValuePair> parameters) {
        String requestString = url + "?" + URLEncodedUtils.format(parameters, ENCODING);
        HttpGet getRequest = new HttpGet(requestString);

        String result;
        String errorReason = null;
        InputStream responseStream = null;
        int repeats = 0;
        while (repeats < maxRepeatTimes) {
            try {
                logger.info("REQUEST: " + requestString);
                HttpResponse response = client.execute(getRequest);
                StatusLine status = response.getStatusLine();
                if (status.getStatusCode() != HttpStatus.SC_OK) {
                    String stringStatus = status.getStatusCode() + " " + status.getReasonPhrase();
                    logger.info("RESPONSE: " + stringStatus);
                    errorReason = stringStatus;
                } else {
                    responseStream = response.getEntity().getContent();
                    result = IOUtils.toString(responseStream, ENCODING);
                    logger.info("RESPONSE: " + result);
                    return result;
                }
            } catch (IOException e) {
                errorReason = e.getMessage();
            } finally {
                if (responseStream != null) {
                    try {
                        responseStream.close();
                    } catch (IOException e) {
                        // WTF??
                    }
                }
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
