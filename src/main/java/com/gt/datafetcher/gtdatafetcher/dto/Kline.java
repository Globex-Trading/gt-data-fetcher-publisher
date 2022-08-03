package com.gt.datafetcher.gtdatafetcher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Kline {
    //binance
    private String provider;
    //kline
    private String event;
    private String symbol;
    private long openTime;
    private long closeTime;
    private String interval;

    private float openPrice;
    private float closePrice;
    private float highPrice;
    private float lowPrice;

    //Take quote asset volume
    private float volume;

    private boolean isKlineClosed;

    private long eventTime;
}
