package com.gt.datafetcher.gtdatafetcher.historyfetcher;

import com.binance.connector.client.impl.spot.Market;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gt.datafetcher.gtdatafetcher.dto.Kline;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BinanceMarketDataClient {
    private final Market binanceMarketClient;
    private final ObjectMapper pastKlineArrayObjMapper;
    private final ObjectMapper pastKlineEntryObjMapper;

    public BinanceMarketDataClient() {
        binanceMarketClient = new Market("https://api.binance.com", "", true);
        pastKlineArrayObjMapper = new ObjectMapper();
        pastKlineEntryObjMapper = new ObjectMapper();
    }

    public List<Kline> getPastKlineData(String currencyPair, String timeframe, Long startTime, Long endTime) {
        LinkedHashMap<String, Object> properties = new LinkedHashMap<>();
        properties.put("symbol", currencyPair.toUpperCase(Locale.ROOT));
        properties.put("interval", timeframe);
        if (startTime != null) properties.put("startTime", startTime);
        if (endTime != null) properties.put("endTime", endTime);
        properties.put("limit", 5);

        String result = binanceMarketClient.klines(properties);

        return deserializeAPIResponse(result);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class PastKlineArray {
        public String data;
    }

    private List<Kline> deserializeAPIResponse(String response) {
        ArrayList<ArrayList<String>> entries;
        try {
            PastKlineArray pastKlineArray = pastKlineArrayObjMapper.readValue(response, PastKlineArray.class);
            TypeReference<ArrayList<ArrayList<String>>> tr = new TypeReference<ArrayList<ArrayList<String>>>() {};
            entries = pastKlineEntryObjMapper.readValue(pastKlineArray.data, tr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

        List<Kline> klineList = new LinkedList<>();
        for (ArrayList<String> entry: entries) {
            try {
                Kline k = new Kline();
                k.setProvider("binance");
                k.setOpenTime(Long.parseLong(entry.get(0)));
                k.setCloseTime(Long.parseLong(entry.get(6)));
                k.setOpenPrice(Float.parseFloat(entry.get(1)));
                k.setClosePrice(Float.parseFloat(entry.get(4)));
                k.setHighPrice(Float.parseFloat(entry.get(2)));
                k.setLowPrice(Float.parseFloat(entry.get(3)));
                k.setVolume(Float.parseFloat(entry.get(5)));

                klineList.add(k);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return klineList;
    }
}
