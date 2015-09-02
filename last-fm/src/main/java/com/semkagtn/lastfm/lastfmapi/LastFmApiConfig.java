package com.semkagtn.lastfm.lastfmapi;

/**
 * Created by semkagtn on 02.09.15.
 */
public class LastFmApiConfig {

    public static final int DEFAULT_TIMEOUT = 10_000;

    private String apiKey;
    private int timeout = DEFAULT_TIMEOUT;


    public static Builder newInstance() {
        return new Builder();
    }

    private LastFmApiConfig() {

    }

    public String getApiKey() {
        return apiKey;
    }

    public int getTimeout() {
        return timeout;
    }

    public static class Builder {

        private LastFmApiConfig config;

        public Builder() {
            this.config = new LastFmApiConfig();
        }

        public Builder withApiKey(String apiKey) {
            config.apiKey = apiKey;
            return this;
        }

        public Builder withTimeout(int timeout) {
            config.timeout = timeout;
            return this;
        }

        public LastFmApiConfig build() {
            return config;
        }
    }
}
