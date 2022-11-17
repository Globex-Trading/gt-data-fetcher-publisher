package com.gt.datafetcher.gtdatafetcher.alerts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class PriceTest {
    private Price price1;

    @BeforeEach
    void setUp() {
        price1 = new Price(100);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getOldPriceAndPutNewPrice() {
        float oldPrice1 = price1.getOldPriceAndPutNewPrice(101);
        assertThat(oldPrice1, equalTo(100f));
        float oldPrice2 = price1.getOldPriceAndPutNewPrice(102);
        assertThat(oldPrice2, equalTo(101f));
    }
}