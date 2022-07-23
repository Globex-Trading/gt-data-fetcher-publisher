package com.gt.datafetcher.gtdatafetcher.fetcher;

import com.binance.connector.client.impl.WebsocketClientImpl;
import com.gt.datafetcher.gtdatafetcher.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BinanceConnector {
    private WebsocketClientImpl websocketClient;
    private WebsocketClientImpl websocketClient2;

    ArrayList<String> klineStreams;
    ArrayList<String> tickerStreams;

    private Constants constants;

    @Autowired
    private BinanceEventHandler eventHandler;

    @Autowired
    public BinanceConnector(Constants constants) {
        this.constants = constants;
        klineStreams = constants.getAllKlineStreamNames();
        tickerStreams = constants.getAllMiniTickerStreamNames();
    }

    public void startBinanceStreaming() {
        createConnections();
    }

    private void createConnections() {
        this.websocketClient = new WebsocketClientImpl();
        this.websocketClient2 = new WebsocketClientImpl();
        subscribeToKlineStreams();
        subscribeToTickerStreams();
    }

    private void subscribeToKlineStreams() {
        this.websocketClient.combineStreams(this.klineStreams, ( (event) -> eventHandler.handleKlineStreamEvent(event)));
    }

    private void subscribeToTickerStreams() {
        this.websocketClient2.combineStreams(this.tickerStreams, ( (event) -> eventHandler.handleTickerEvent(event)));
    }
}
