package com.gt.datafetcher.gtdatafetcher.constants;

import com.gt.datafetcher.gtdatafetcher.dto.ProviderResponse;
import com.gt.datafetcher.gtdatafetcher.utilities.OkHTTPUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BinanceSymbolsTimeframes {
    private final List<String> currencyPairs;
    private final List<String> timePeriods;

    private final int streamsPerConnection;

    @Autowired
    public BinanceSymbolsTimeframes(OkHTTPUtility okHTTPUtility) {
        this.streamsPerConnection = 5;

        ProviderResponse providerResponse = okHTTPUtility.getAvailableSymbolsAndTimeframes();
        if(providerResponse != null) {
            this.currencyPairs = providerResponse.symbols
                    .stream().map(String::toLowerCase).collect(Collectors.toList());
            this.timePeriods = providerResponse.timeframes;
        } else {
            //Default symbols and timeframes
            this.currencyPairs = new LinkedList<>();
            this.timePeriods = new LinkedList<>();

            currencyPairs.addAll(Arrays.asList("btcusdt", "ethusdt", "bnbusdt", "adausdt",
                    "solusdt", "maticusdt", "dotusdt", "linkusdt"));
            timePeriods.addAll(Arrays.asList("1m", "5m", "15m", "1h", "4h", "1d", "1w", "1M"));
        }


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

    public List<ArrayList<String>> getAllKlineStreamsSeparated() {
        List<ArrayList<String>> streamNames = new LinkedList<>();

        ArrayList<String> tempList = new ArrayList<>(this.streamsPerConnection);
        for (String cp : currencyPairs) {
            for (String tp: timePeriods) {
                if (tempList.size() >= this.streamsPerConnection) {
                    streamNames.add(tempList);
                    tempList = new ArrayList<>(this.streamsPerConnection);
                }
                tempList.add(cp + "@kline_" + tp);
            }
        }
        streamNames.add(tempList);
        return streamNames;
    }

    public ArrayList<String> getAllTickerStreamNames() {
        ArrayList<String> streamNames = new ArrayList<>();
        for (String cp : currencyPairs) {
            streamNames.add(cp + "@ticker");
        }
        return streamNames;
    }

    public List<ArrayList<String>> getAllTickerStreamsSeparated() {
        List<ArrayList<String>> streamNames = new LinkedList<>();

        ArrayList<String> tempList = new ArrayList<>(this.streamsPerConnection);
        for (String cp : currencyPairs) {
            if (tempList.size() >= this.streamsPerConnection) {
                streamNames.add(tempList);
                tempList = new ArrayList<>(this.streamsPerConnection);
            }
            tempList.add(cp + "@ticker");
        }
        streamNames.add(tempList);
        return streamNames;
    }
}
