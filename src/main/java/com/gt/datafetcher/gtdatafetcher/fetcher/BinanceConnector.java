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
        subscribeToKlineStreams();
        subscribeToTickerStreams();
    }

    private void subscribeToKlineStreams() {
        this.websocketClient = new WebsocketClientImpl();
        this.websocketClient.combineStreams(
                this.klineStreams, new NoopCallback(), ( (event) -> eventHandler.handleKlineStreamEvent(event)),
                new ReconnectKlineStreamCallback(this), new NoopCallback());
    }

    private void subscribeToTickerStreams() {
        this.websocketClient2 = new WebsocketClientImpl();
        this.websocketClient2.combineStreams(this.tickerStreams, new NoopCallback(),
                ( (event) -> eventHandler.handleTickerEvent(event)),
                new ReconnectTickerStreamCallback(this), new NoopCallback());
    }

    public void reconnectToKlineStreamsOnClose() {
        this.subscribeToKlineStreams();
    }

    public void reconnectToTickerStreamsOnClose() {
        this.subscribeToTickerStreams();
    }
}
