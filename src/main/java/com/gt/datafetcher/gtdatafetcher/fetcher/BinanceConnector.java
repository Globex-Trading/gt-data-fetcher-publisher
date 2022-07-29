package com.gt.datafetcher.gtdatafetcher.fetcher;

import com.binance.connector.client.impl.WebsocketClientImpl;
import com.gt.datafetcher.gtdatafetcher.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class BinanceConnector {
    private WebsocketClientImpl websocketClient;

    private final Map<String, Integer> connectionIDs;

    ArrayList<String> klineStreams;
    ArrayList<String> tickerStreams;

    @Autowired
    private BinanceEventHandler eventHandler;

    @Autowired
    public BinanceConnector(Constants constants) {
        klineStreams = constants.getAllKlineStreamNames();
        tickerStreams = constants.getAllMiniTickerStreamNames();

        this.connectionIDs = new HashMap<>();
    }

    public void startBinanceStreaming() {
        createConnections();
    }

    private void createConnections() {
        this.websocketClient = new WebsocketClientImpl();
        subscribeToKlineStreams();
        subscribeToTickerStreams();
    }

    private void subscribeToKlineStreams() {
        int id = this.websocketClient.combineStreams(
                this.klineStreams, new NoopCallback(), ( (event) -> eventHandler.handleKlineStreamEvent(event)),
                new NoopCallback(), new NoopCallback());
        this.connectionIDs.put("KlineStreamID", id);
    }

    private void subscribeToTickerStreams() {
        int id = this.websocketClient.combineStreams(this.tickerStreams, new NoopCallback(),
                ( (event) -> eventHandler.handleTickerEvent(event)),
                new NoopCallback(), new NoopCallback());
        this.connectionIDs.put("TickerStreamID", id);
    }

    public void reconnectToKlineStreams() {
        if (this.connectionIDs.containsKey("KlineStreamID")) {
            System.out.println("A kline stream connection already exists. Dropping now.");
            closeKlineStreams();
        }
        this.subscribeToKlineStreams();
    }

    public void reconnectToTickerStreams() {
        if (this.connectionIDs.containsKey("TickerStreamID")) {
            System.out.println("A ticker stream connection already exists. Dropping now.");
            closeTickerStreams();
        }
        this.subscribeToTickerStreams();
    }

    public void closeKlineStreams() {
        if(this.connectionIDs.containsKey("KlineStreamID")) {
            this.websocketClient.closeConnection(this.connectionIDs.get("KlineStreamID"));
            this.connectionIDs.remove("KlineStreamID");
        } else {
            System.out.println("Kline stream already closed.");
        }
    }

    public void closeTickerStreams() {
        if(this.connectionIDs.containsKey("TickerStreamID")) {
            this.websocketClient.closeConnection(this.connectionIDs.get("TickerStreamID"));
            this.connectionIDs.remove("TickerStreamID");
        } else {
            System.out.println("Ticker stream already closed.");
        }
    }

    public void closeAllStreams() {
        closeKlineStreams();
        closeTickerStreams();
    }

    public synchronized void dropConnectionsAndReconnectToStreams () {
        System.out.println("Lock acquired.\nClosing all streams.");
        this.closeAllStreams();
        System.out.println("Waiting 15 seconds till closing.");
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Start Reconnecting.");
        this.reconnectToKlineStreams();
        this.reconnectToTickerStreams();
        System.out.println("Reconnected.");
    }
}
