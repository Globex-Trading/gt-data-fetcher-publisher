package com.gt.datafetcher.gtdatafetcher.db;

import com.gt.datafetcher.gtdatafetcher.model.Candle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class CandleDBServiceTest {

    private CandleDBService candleDBService;

    @BeforeEach
    void setUp() {
        candleDBService = new CandleDBService();

        MongoTemplate mongoTemplateMock = Mockito.mock(MongoTemplate.class);
        Candle c = new Candle();
        c.setOpen_time(100);
        c.setClose_time(200);
        List<Candle> candleList = Collections.singletonList(c);
        Mockito.when(mongoTemplateMock.find(any(Query.class), eq(Candle.class), any(String.class))).thenReturn(candleList);
        ReflectionTestUtils.setField(candleDBService, "mongoTemplate", mongoTemplateMock);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getEarliestCandleTime() {
        long[] result = candleDBService.getEarliestCandleTime("BTCUSDT", "1m");
        assertThat(result.length, equalTo(2));
        assertThat(result[0], equalTo(100L));
        assertThat(result[1], equalTo(200L));
    }
}