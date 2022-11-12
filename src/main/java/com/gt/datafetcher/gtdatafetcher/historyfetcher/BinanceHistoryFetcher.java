package com.gt.datafetcher.gtdatafetcher.historyfetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BinanceHistoryFetcher implements CommandLineRunner {
    @Autowired
    private HistoryFetcher historyFetcher;

    @Override
    public void run(String... args) throws Exception {
        Thread.sleep(6000);
        System.out.println("Starting History Fetcher.");
        historyFetcher.initiateHistoryFetcher();
        System.out.println("Finished History Fetcher.");
    }
}
