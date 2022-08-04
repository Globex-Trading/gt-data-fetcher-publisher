package com.gt.datafetcher.gtdatafetcher.enums;

public enum Endpoints {
    ALERT_TRIGGER_ENDPOINT("/alerts/trigger-alerts"),
    SYMBOL_FETCH_ENDPOINT("/providers/for-fetcher/slug/binance");

    private String endpoint;

    Endpoints(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return this.endpoint;
    }

}
