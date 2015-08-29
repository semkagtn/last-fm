package com.semkagtn.lastfm.api;

/**
 * Created by semkagtn on 20.07.15.
 */
public class ApiConfig {

    public static int DEFAULT_TIMEOUT = 20_000;
    public static int DEFAULT_REQUEST_REPEATS = 2;
    public static String DEFAULT_USER_AGENT = "tst";
    public static boolean DEFAULT_ENABLE_LOGGER = true;

    private String apiKey;
    private int timeout = DEFAULT_TIMEOUT;
    private int requestRepeats = DEFAULT_REQUEST_REPEATS;
    private String userAgent = DEFAULT_USER_AGENT;
    private boolean enableLogger = DEFAULT_ENABLE_LOGGER;

    public int getRequestRepeats() {
        return requestRepeats;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public boolean isEnableLogger() {
        return enableLogger;
    }

    public String getApiKey() {
        return apiKey;
    }

    public static Builder newInstance() {
        return new Builder();
    }

    private ApiConfig() {

    }

    public static class Builder {

        private ApiConfig apiConfig = new ApiConfig();

        public Builder withApiKey(String apiKey) {
            apiConfig.apiKey = apiKey;
            return this;
        }

        public Builder withTimeout(int timeout) {
            apiConfig.timeout = timeout;
            return this;
        }

        public Builder withRequestRepeats(int requestRepeats) {
            apiConfig.requestRepeats = requestRepeats;
            return this;
        }

        public Builder withUserAgent(String userAgent) {
            apiConfig.userAgent = userAgent;
            return this;
        }

        public Builder withEnableLogger(boolean enableLogger) {
            apiConfig.enableLogger = enableLogger;
            return this;
        }

        public ApiConfig build() {
            return apiConfig;
        }

        private Builder() {

        }
    }
}
