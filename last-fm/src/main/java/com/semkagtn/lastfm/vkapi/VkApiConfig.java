package com.semkagtn.lastfm.vkapi;

/**
 * Created by semkagtn on 30.08.15.
 */
public class VkApiConfig {

    public static final int DEFAULT_TIMEOUT = 10_000;

    private String token;
    private int timeout = DEFAULT_TIMEOUT;


    public static Builder newInstance() {
        return new Builder();
    }

    private VkApiConfig() {

    }

    public String getToken() {
        return token;
    }

    public int getTimeout() {
        return timeout;
    }

    public static class Builder {

        private VkApiConfig config;

        public Builder() {
            this.config = new VkApiConfig();
        }

        public Builder withToken(String token) {
            config.token = token;
            return this;
        }

        public Builder withTimeout(int timeout) {
            config.timeout = timeout;
            return this;
        }

        public VkApiConfig build() {
            return config;
        }
    }
}
