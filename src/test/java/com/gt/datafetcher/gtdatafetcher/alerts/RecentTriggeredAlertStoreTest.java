package com.gt.datafetcher.gtdatafetcher.alerts;

import com.gt.datafetcher.gtdatafetcher.model.Alert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

class RecentTriggeredAlertStoreTest {

    private RecentTriggeredAlertStore store;

    @BeforeEach
    void setUp() {
        store = new RecentTriggeredAlertStore();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void filterTriggeredAlerts() {
        store.filterTriggeredAlerts("BTC", Arrays.asList(new Alert("1"), new Alert("2"), new Alert("3")));
        store.filterTriggeredAlerts("ETH", Arrays.asList(new Alert("10"), new Alert("11"), new Alert("12")));

        List<String> btcFilter = store.filterTriggeredAlerts("BTC",
                Arrays.asList(new Alert("2"), new Alert("4"), new Alert("5")));
        assertThat(btcFilter.size(), equalTo(2));
        assertThat(btcFilter, containsInAnyOrder("4", "5"));

        List<String> ethFilter = store.filterTriggeredAlerts("ETH",
                Arrays.asList(new Alert("12"), new Alert("14"), new Alert("11")));
        assertThat(ethFilter.size(), equalTo(1));
        assertThat(ethFilter, containsInAnyOrder("14"));

    }
}