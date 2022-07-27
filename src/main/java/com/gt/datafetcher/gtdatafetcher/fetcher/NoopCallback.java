package com.gt.datafetcher.gtdatafetcher.fetcher;

import com.binance.connector.client.utils.WebSocketCallback;

public class NoopCallback implements WebSocketCallback {
    @Override
    public void onReceive(String s) {
        System.out.println(s);
    }
}
