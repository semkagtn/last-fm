package com.semkagtn.musicdatamining.httpclient;

/**
 * Created by semkagtn on 03.09.15.
 */
public class HttpClientConfig {

    public static final int DEFAULT_TIMEOUT = 10_000;
    public static final boolean DEFAULT_LOGGER_ENABLED = true;
    public static final int DEFAULT_MAX_REPEAT_TIMES = 2;

    private int timeout = DEFAULT_TIMEOUT;
    private boolean loggerEnabled = DEFAULT_LOGGER_ENABLED;
    private int maxRepeatTimes = DEFAULT_MAX_REPEAT_TIMES;

    public static Builder newInstance() {
        return new Builder();
    }

    private HttpClientConfig() {

    }

    public int getTimeout() {
        return timeout;
    }

    public boolean isLoggerEnabled() {
        return loggerEnabled;
    }

    public int getMaxRepeatTimes() {
        return maxRepeatTimes;
    }

    public static class Builder {

        private HttpClientConfig config;

        public Builder() {
            this.config = new HttpClientConfig();
        }


        public Builder withTimeout(int timeout) {
            config.timeout = timeout;
            return this;
        }

        public Builder withLoggerEnabled(boolean loggerEnabled) {
            config.loggerEnabled = loggerEnabled;
            return this;
        }

        public Builder withMaxRepeatTimes(int maxRepeatTimes) {
            config.maxRepeatTimes = maxRepeatTimes;
            return this;
        }

        public HttpClientConfig build() {
            return config;
        }
    }
}
