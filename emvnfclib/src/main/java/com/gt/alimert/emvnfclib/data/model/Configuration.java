package com.gt.alimert.emvnfclib.data.model;

/**
 * @author AliMertOzdemir
 * @class Configuration
 * @created 30.04.2020
 */
public class Configuration {

    private String hostUrl;
    private long acquirerId;

    // Optional
    private int readTimeout;
    private int connectTimeout;
    private boolean emvSupport;

    public String getHostUrl() {
        return hostUrl;
    }

    public long getAcquirerId() {
        return acquirerId;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public boolean isEmvSupport() {
        return emvSupport;
    }

    private Configuration(Builder builder) {
        this.hostUrl = builder.hostUrl;
        this.acquirerId = builder.acquirerId;
        this.readTimeout = builder.readTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.emvSupport = builder.emvSupport;
    }

    public static class Builder {

        private String hostUrl;
        private long acquirerId;

        // Optional
        private int readTimeout = 3000;
        private int connectTimeout = 30000;
        private boolean emvSupport;

        public Builder(String hostUrl, long acquirerId) {
            this.hostUrl = hostUrl;
            this.acquirerId = acquirerId;
        }

        public Builder setReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder setEmvSupport(boolean emvSupport) {
            this.emvSupport = emvSupport;
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }

    }
}
