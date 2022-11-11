package com.gt.datafetcher.gtdatafetcher.historyfetcher;

import com.gt.datafetcher.gtdatafetcher.constants.BinanceSymbolsTimeframes;
import com.gt.datafetcher.gtdatafetcher.db.CandleDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HistoryFetcher {
    @Autowired
    private BinanceSymbolsTimeframes binanceSymbolsTimeframes;

    @Autowired
    private CandleDBService candleDBService;

    public void initiateHistoryFetcher(){
        start();
    }

    public void start(){
        List<String> currencyPairs = binanceSymbolsTimeframes.getCurrencyPairs();
        List<String> timeframes = binanceSymbolsTimeframes.getTimePeriods();

        for (String cp :currencyPairs) {
            for (String tf : timeframes) {
                System.out.println(getEarliestCandleTime(cp, tf));
            }
        }
    }

    public long getEarliestCandleTime(String currencyPair, String timeframe){
        return candleDBService.getEarliestCandleTime(currencyPair, timeframe);
    }
}
