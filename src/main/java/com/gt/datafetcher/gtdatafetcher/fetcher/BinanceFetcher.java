package com.gt.datafetcher.gtdatafetcher.fetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class BinanceFetcher implements CommandLineRunner {
    @Autowired
    private BinanceConnector binanceConnector;

    @Override
    public void run(String... args) {
        binanceConnector.startBinanceStreaming();
    }
}
