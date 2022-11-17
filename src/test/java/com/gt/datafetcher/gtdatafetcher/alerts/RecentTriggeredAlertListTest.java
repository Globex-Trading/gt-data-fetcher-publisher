package com.gt.datafetcher.gtdatafetcher.alerts;

import com.gt.datafetcher.gtdatafetcher.model.Alert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class RecentTriggeredAlertListTest {

    private RecentTriggeredAlertList recentTriggeredAlertList;

    @BeforeEach
    void setUp() {
        recentTriggeredAlertList = new RecentTriggeredAlertList(3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void filterTriggeredAlerts() {
        recentTriggeredAlertList.filterTriggeredAlerts(Arrays.asList(new Alert("1"), new Alert("2")));
        List<String> filteredAlerts = recentTriggeredAlertList
                .filterTriggeredAlerts(Arrays.asList(new Alert("2"), new Alert("3")));
        assertThat(filteredAlerts.size(), equalTo(1));
        assertThat(filteredAlerts.get(0), equalTo("3"));
    }
}