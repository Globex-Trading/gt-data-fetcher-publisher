package com.gt.datafetcher.gtdatafetcher.db;

import com.gt.datafetcher.gtdatafetcher.model.Alert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;

class AlertDBServiceTest {

    private AlertDBService alertDBService;

    @BeforeEach
    void setUp() {
        alertDBService = new AlertDBService();

        MongoTemplate mongoTemplateMock = Mockito.mock(MongoTemplate.class);
        List<Alert> alertsList = Arrays.asList(new Alert("1"), new Alert("2"));
        Mockito.when(mongoTemplateMock.find(any(Query.class), eq(Alert.class), eq("alerts"))).thenReturn(alertsList);
        ReflectionTestUtils.setField(alertDBService, "mongoTemplate", mongoTemplateMock);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAlertsForAPriceRange() {
        List<Alert> alerts = alertDBService.getAlertsForAPriceRange("binance", "BTCUSDT", 16000, 17000);
        assertThat(alerts.size(), equalTo(2));
    }
}