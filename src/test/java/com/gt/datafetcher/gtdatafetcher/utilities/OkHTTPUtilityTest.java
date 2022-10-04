package com.gt.datafetcher.gtdatafetcher.utilities;

import com.gt.datafetcher.gtdatafetcher.dto.ProviderResponse;
import com.gt.datafetcher.gtdatafetcher.dto.ResponseBodyDTO;
import okhttp3.Request;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;

class OkHTTPUtilityTest {
    private static final String gtAPIHostname = "http://localhost:3000";

    private OkHTTPUtility okHTTPUtility;

    @BeforeEach
    void setUp() {
        okHTTPUtility = new OkHTTPUtility();
        ReflectionTestUtils.setField(okHTTPUtility, "gtAPIHostname", gtAPIHostname);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void postAlertsToBeTriggered() {
        ResponseBodyDTO responseBodyDTO = new ResponseBodyDTO(200, "");

        OkHTTPUtility spy = Mockito.spy(okHTTPUtility);

        Mockito.doReturn(responseBodyDTO).when(spy).sendRequest(any(Request.class));

        List<String> l = Arrays.asList("ID1", "ID2", "ID3");
        assertThat(spy.postAlertsToBeTriggered(l), is(true));
    }

    @Test
    void getAvailableSymbolsAndTimeframes() {
        ResponseBodyDTO responseBodyDTO = new ResponseBodyDTO(200, "", "{\"timeframes\":[\"1m\",\"5m\",\"15m\",\"1h\",\"4h\",\"1d\",\"1W\",\"1M\"],\"symbols\":[\"BTCUSDT\",\"ETHUSDT\",\"SOLUSDT\",\"MATICUSDT\",\"LINKUSDT\",\"BNBUSDT\",\"DOTUSDT\"]}");

        OkHTTPUtility spy = Mockito.spy(okHTTPUtility);

        Mockito.doReturn(responseBodyDTO).when(spy).sendRequest(any(Request.class));
        ProviderResponse providerResponse = spy.getAvailableSymbolsAndTimeframes();
        assertThat(providerResponse, notNullValue());
        assertThat(providerResponse.symbols, containsInAnyOrder("BTCUSDT","ETHUSDT","SOLUSDT","MATICUSDT","LINKUSDT","BNBUSDT","DOTUSDT"));
        assertThat(providerResponse.timeframes, containsInAnyOrder("1m","5m","15m","1h","4h","1d","1W","1M"));

    }

    @Test
    void sendRequest() {

    }
}