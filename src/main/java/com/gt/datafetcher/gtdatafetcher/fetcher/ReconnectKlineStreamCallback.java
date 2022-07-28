package com.gt.datafetcher.gtdatafetcher.fetcher;

import com.binance.connector.client.utils.WebSocketCallback;

public class ReconnectKlineStreamCallback implements WebSocketCallback {
    private BinanceConnector binanceConnector;

    public ReconnectKlineStreamCallback(BinanceConnector binanceConnector) {
        this.binanceConnector = binanceConnector;
    }

    @Override
    public void onReceive(String s) {
        System.out.println("Connection has been closed.");
        System.out.println(s);

        //Reconnect
        //binanceConnector.reconnectToKlineStreamsOnClose();
    }
}
