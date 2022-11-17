package com.gt.datafetcher.gtdatafetcher.alerts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class PriceStoreTest {
    private PriceStore priceStore;

    @BeforeEach
    void setUp() {
        priceStore = new PriceStore();
    }

    @Test
    void getOldPriceAndPutNewPrice() {
        Float btcPrice = priceStore.getOldPriceAndPutNewPrice("BTC", 16000);
        Float ethPrice = priceStore.getOldPriceAndPutNewPrice("ETH", 1200);
        assertThat(btcPrice, equalTo(null));
        assertThat(ethPrice, equalTo(null));
        btcPrice = priceStore.getOldPriceAndPutNewPrice("BTC", 17000);
        ethPrice = priceStore.getOldPriceAndPutNewPrice("ETH", 1250);
        assertThat(btcPrice, equalTo(16000f));
        assertThat(ethPrice, equalTo(1200f));
    }
}