package com.gt.datafetcher.gtdatafetcher.fetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BinanceReconnectFetcher {
    @Autowired
    private BinanceConnector binanceConnector;

    @Scheduled(cron = "5 2 1 * * *", zone = "UTC")
    public void reconnectBinanceFetcherEveryDay() {
        System.out.println("Reconnecting Binance Fetcher.");
        System.out.println("Waiting to acquire lock...");
        binanceConnector.dropConnectionsAndReconnectToStreams();
    }
}
