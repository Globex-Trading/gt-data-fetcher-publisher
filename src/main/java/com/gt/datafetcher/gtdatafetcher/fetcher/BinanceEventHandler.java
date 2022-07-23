package com.gt.datafetcher.gtdatafetcher.fetcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gt.datafetcher.gtdatafetcher.dto.Kline;
import com.gt.datafetcher.gtdatafetcher.dto.Ticker;
import com.gt.datafetcher.gtdatafetcher.pojo.KlineStreamData;
import com.gt.datafetcher.gtdatafetcher.pojo.TickerStreamData;
import com.gt.datafetcher.gtdatafetcher.ws.KlineStreamHandler;
import com.gt.datafetcher.gtdatafetcher.ws.TickerStreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BinanceEventHandler {

    @Autowired
    private KlineStreamHandler klineStreamHandler;
    @Autowired
    private TickerStreamHandler tickerStreamHandler;

    private ObjectMapper klineObjectMapperD;
    private ObjectMapper klineObjectMapperS;
    private ObjectMapper tickerObjectMapperD;
    private ObjectMapper tickerObjectMapperS;


    public BinanceEventHandler() {
        klineObjectMapperD = new ObjectMapper();
        klineObjectMapperS = new ObjectMapper();
        tickerObjectMapperD = new ObjectMapper();
        tickerObjectMapperS = new ObjectMapper();
    }

    public void handleKlineStreamEvent (String event) {
        //long start = System.nanoTime();
        KlineStreamData klineStreamData;
        try {
            klineStreamData = this.klineObjectMapperD.readValue(event, KlineStreamData.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return;
        }

        //Map to a Common DTO Object
        Kline kline = new Kline("binance", "kline_" + klineStreamData.data.k.i, klineStreamData.data.s,
                klineStreamData.data.k.t, klineStreamData.data.k.T, klineStreamData.data.k.i,
                klineStreamData.data.k.o, klineStreamData.data.k.c, klineStreamData.data.k.h, klineStreamData.data.k.l,
                Boolean.parseBoolean(klineStreamData.data.k.x), klineStreamData.data.E);

        String klineString;
        try {
            klineString = klineObjectMapperS.writeValueAsString(kline);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return;
        }
        String eventKey = kline.getProvider()+"_"+kline.getSymbol()+"_"+kline.getInterval();
        //System.out.println(Duration.ofNanos(System.nanoTime() - start));

        //Publish to Websocket
        klineStreamHandler.publishKlineEventToTopic(eventKey, klineString);
    }

    public void handleTickerEvent(String event) {
        //long start = System.nanoTime();
        TickerStreamData tickerStreamData;
        try {
            tickerStreamData = this.tickerObjectMapperD.readValue(event, TickerStreamData.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return;
        }

        //Map to a Common DTO Object
        Ticker ticker = new Ticker("binance", "ticker_" + tickerStreamData.data.s, tickerStreamData.data.E,
                tickerStreamData.data.s, tickerStreamData.data.p, tickerStreamData.data.P, tickerStreamData.data.c,
                tickerStreamData.data.o, tickerStreamData.data.h, tickerStreamData.data.l);

        String tickerString;
        try {
            tickerString = tickerObjectMapperS.writeValueAsString(ticker);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return;
        }

        String eventKey = ticker.getProvider()+"_"+ticker.getSymbol();

        //Publish to Websocket
        tickerStreamHandler.publishTickerEventToTopic(eventKey, tickerString);
    }
}
