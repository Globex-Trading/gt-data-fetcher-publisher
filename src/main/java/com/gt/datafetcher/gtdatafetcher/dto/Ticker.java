package com.gt.datafetcher.gtdatafetcher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Ticker {
    //binance
    private String provider;
    //ticker
    private String event;
    private long eventTime;

    private String symbol;
    private float priceChange;
    private float priceChangePercent;

    private float lastPrice;
    private float openPrice;
    private float highPrice;
    private float lowPrice;
}
