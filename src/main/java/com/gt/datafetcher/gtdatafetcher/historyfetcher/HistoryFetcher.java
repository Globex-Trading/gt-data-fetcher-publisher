package com.gt.datafetcher.gtdatafetcher.historyfetcher;

import com.gt.datafetcher.gtdatafetcher.constants.BinanceSymbolsTimeframes;
import com.gt.datafetcher.gtdatafetcher.db.CandleDBService;
import com.gt.datafetcher.gtdatafetcher.dto.Kline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
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
                long[] earliestCandle = getEarliestCandleTime(cp, tf);
                long numberOfPastCandles = 0;
                Long earliestCandleOpenTime = null;
                if (earliestCandle != null) {
                    earliestCandleOpenTime = earliestCandle[0];
                    long earliestCandleCloseTime = earliestCandle[1];
                    long currentTime = Instant.now().toEpochMilli();
                    long interval = earliestCandleCloseTime - earliestCandleOpenTime + 1;

                    numberOfPastCandles = (currentTime - earliestCandleOpenTime)/interval;
                }
                if (numberOfPastCandles > 900) continue;

                //Fetch Data from Binance
                List<Kline> pastKlines = binanceMarketDataClient.getPastKlineData(cp, tf, null, earliestCandleOpenTime);

                //Save Data in database
                persistPastKlines(pastKlines, cp, tf);
            }
        }
    }

    private long[] getEarliestCandleTime(String currencyPair, String timeframe){
        return candleDBService.getEarliestCandleTime(currencyPair, timeframe);
    }

    private void persistPastKlines(List<Kline> klines, String currencyPair, String timeframe) {
        candleDBService.insertManyCandles(klines, currencyPair, timeframe);
    }

}
