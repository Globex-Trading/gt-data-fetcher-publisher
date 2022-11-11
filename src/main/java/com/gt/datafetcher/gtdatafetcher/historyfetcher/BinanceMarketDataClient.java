package com.gt.datafetcher.gtdatafetcher.historyfetcher;

import com.binance.connector.client.impl.spot.Market;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Locale;

@Component
public class BinanceMarketDataClient {
    private final Market binanceMarketClient;

    public BinanceMarketDataClient() {
        binanceMarketClient = new Market("https://api.binance.com", "", true);
    }

    public void getPastKlineData(String currencyPair, String timeframe, Long startTime, Long endTime) {
        LinkedHashMap<String, Object> properties = new LinkedHashMap<>();
        properties.put("symbol", currencyPair.toUpperCase(Locale.ROOT));
        properties.put("interval", timeframe);
        if (startTime != null) properties.put("startTime", startTime);
        if (endTime != null) properties.put("endTime", endTime);
        properties.put("limit", 5);

        String result = binanceMarketClient.klines(properties);
        System.out.println(currencyPair);
        System.out.println(result);
    }
}
