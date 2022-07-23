package com.gt.datafetcher.gtdatafetcher.constants;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
public class Constants {
    private List<String> currencyPairs;
    private List<String> timePeriods;

    public Constants() {
        currencyPairs = new LinkedList<>();
        timePeriods = new LinkedList<>();

        currencyPairs.addAll(Arrays.asList("btcusdt", "ethusdt", "bnbusdt", "adausdt",
                "solusdt", "maticusdt", "dotusdt", "linkusdt"));
        timePeriods.addAll(Arrays.asList("1m", "5m", "15m", "1h", "4h", "1d", "1w", "1M"));
    }

    public ArrayList<String> getAllKlineStreamNames() {
        ArrayList<String> streamNames = new ArrayList<>();
        for (String cp : currencyPairs) {
            for (String tp: timePeriods) {
                streamNames.add(cp + "@kline_" + tp);
            }
        }
        return streamNames;
    }

    public ArrayList<String> getAllMiniTickerStreamNames() {
        ArrayList<String> streamNames = new ArrayList<>();
        for (String cp : currencyPairs) {
            streamNames.add(cp + "@ticker");
        }
        return streamNames;
    }
}
