package com.gt.datafetcher.gtdatafetcher.constants;

import com.gt.datafetcher.gtdatafetcher.dto.ProviderResponse;
import com.gt.datafetcher.gtdatafetcher.utilities.OkHTTPUtility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;


class BinanceSymbolsTimeframesTest {
    private BinanceSymbolsTimeframes binanceSymbolsTimeframes;

    private OkHTTPUtility okHTTPUtility;

    @BeforeEach
    void setUp() {
        // Mock OkHTTPUtility
        okHTTPUtility = Mockito.mock(OkHTTPUtility.class);
        ProviderResponse providerResponse = new ProviderResponse();
        providerResponse.symbols = new ArrayList<>(Arrays.asList("btcusdt", "ethusdt"));
        providerResponse.timeframes = new ArrayList<>(
                Arrays.asList("1m", "5m"));
        Mockito.when(okHTTPUtility.getAvailableSymbolsAndTimeframes()).thenReturn(providerResponse);

        binanceSymbolsTimeframes = new BinanceSymbolsTimeframes(okHTTPUtility);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Get all Kline Stream Names")
    void getAllKlineStreamNames() {
        assertThat(binanceSymbolsTimeframes.getAllKlineStreamNames(),
                containsInAnyOrder("btcusdt@kline_1m", "btcusdt@kline_5m",
                        "ethusdt@kline_1m", "ethusdt@kline_5m"));
    }

    @Test
    @DisplayName("Get all Kline Stream Names separated with a size of 5")
    void getAllKlineStreamsSeparated() {
        Mockito.when(okHTTPUtility.getAvailableSymbolsAndTimeframes()).thenReturn(null);
        binanceSymbolsTimeframes = new BinanceSymbolsTimeframes(okHTTPUtility);

        List<ArrayList<String>> lists = binanceSymbolsTimeframes.getAllKlineStreamsSeparated();
        assertThat(lists.size(), equalTo(13));

        int totalStreamSize = 0;
        for (ArrayList<String> arrayList : lists) {
            totalStreamSize += arrayList.size();
        }
        assertThat(totalStreamSize, equalTo(64));
    }

    @Test
    @DisplayName("Get all Ticker Stream Names")
    void getAllTickerStreamNames() {
        assertThat(binanceSymbolsTimeframes.getAllTickerStreamNames(),
                containsInAnyOrder("btcusdt@ticker", "ethusdt@ticker"));
    }

    @Test
    @DisplayName("Get all Ticker Stream Names separated with a size of 5")
    void getAllTickerStreamsSeparated() {
        Mockito.when(okHTTPUtility.getAvailableSymbolsAndTimeframes()).thenReturn(null);
        binanceSymbolsTimeframes = new BinanceSymbolsTimeframes(okHTTPUtility);

        List<ArrayList<String>> lists = binanceSymbolsTimeframes.getAllTickerStreamsSeparated();
        assertThat(lists.size(), equalTo(2));

        int totalStreamSize = 0;
        for (ArrayList<String> arrayList : lists) {
            totalStreamSize += arrayList.size();
        }
        assertThat(totalStreamSize, equalTo(8));
    }
}