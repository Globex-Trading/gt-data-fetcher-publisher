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

    @Autowired
    BinanceMarketDataClient binanceMarketDataClient;

    public void initiateHistoryFetcher(){
        start();
    }

    public void start(){
        List<String> currencyPairs = binanceSymbolsTimeframes.getCurrencyPairs();
        List<String> timeframes = binanceSymbolsTimeframes.getTimePeriods();

        for (String cp :currencyPairs) {
            for (String tf : timeframes) {
                Long earliestCandleTime = getEarliestCandleTime(cp, tf);
                binanceMarketDataClient.getPastKlineData(cp, tf, earliestCandleTime, null);
            }
        }
    }

    public Long getEarliestCandleTime(String currencyPair, String timeframe){
        long t = candleDBService.getEarliestCandleTime(currencyPair, timeframe);
        if (t == -1) return null;
        return t;
    }
}
