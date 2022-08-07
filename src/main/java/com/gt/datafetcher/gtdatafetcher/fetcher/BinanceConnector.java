package com.gt.datafetcher.gtdatafetcher.fetcher;

import com.binance.connector.client.impl.WebsocketClientImpl;
import com.gt.datafetcher.gtdatafetcher.alerts.PriceStore;
import com.gt.datafetcher.gtdatafetcher.constants.BinanceSymbolsTimeframes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BinanceConnector {
    private WebsocketClientImpl websocketClient;

    private final Map<String, Set<Integer>> connectionIDs;

    @Autowired
    private BinanceSymbolsTimeframes binanceSymbolsTimeframes;

    @Autowired
    private BinanceEventHandler eventHandler;

    public BinanceConnector() {
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
        List<ArrayList<String>> allStreamsList = this.binanceSymbolsTimeframes.getAllKlineStreamsSeparated();
        for (ArrayList<String> streamsList: allStreamsList) {
            int id = this.websocketClient.combineStreams(
                    streamsList, new NoopCallback(), ( (event) -> eventHandler.handleKlineStreamEvent(event)),
                    new NoopCallback(), new NoopCallback());
            this.putConnectionIDToMap("KlineStreamID", id);
        }


    }

    private void subscribeToTickerStreams() {
        List<ArrayList<String>> allStreamsList = this.binanceSymbolsTimeframes.getAllTickerStreamsSeparated();
        for (ArrayList<String> streamsList: allStreamsList) {
            PriceStore priceStore = new PriceStore();

            int id = this.websocketClient.combineStreams(
                    streamsList,
                    new NoopCallback(),
                    ( (event) -> eventHandler.handleTickerEvent(event, priceStore)),
                    new NoopCallback(), new NoopCallback());
            this.putConnectionIDToMap("TickerStreamID", id);
        }

    }

    private void putConnectionIDToMap(String key, int id) {
        if (!this.connectionIDs.containsKey(key)) {
            this.connectionIDs.put(key, new HashSet<>());
        }
        this.connectionIDs.get(key).add(id);
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
            Set<Integer> currentConnectionIDs = this.connectionIDs.get("KlineStreamID");
            for (int id : currentConnectionIDs) {
                this.websocketClient.closeConnection(id);
                this.connectionIDs.remove("KlineStreamID");
            }
        } else {
            System.out.println("No Kline streams available to be closed.");
        }
    }

    public void closeTickerStreams() {
        if(this.connectionIDs.containsKey("TickerStreamID")) {
            Set<Integer> currentConnectionIDs = this.connectionIDs.get("TickerStreamID");
            for (int id : currentConnectionIDs) {
                this.websocketClient.closeConnection(id);
                this.connectionIDs.remove("TickerStreamID");
            }

        } else {
            System.out.println("No Ticker streams to be closed.");
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
