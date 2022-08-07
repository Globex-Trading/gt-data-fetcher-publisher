package com.gt.datafetcher.gtdatafetcher.alerts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class PriceStore {

    private static final Logger logger = LoggerFactory.getLogger(PriceStore.class);

    private final Map<String, Price> previousPrices;

    public PriceStore() {
        this.previousPrices = new HashMap<>();
    }

    public Float getOldPriceAndPutNewPrice(String eventKey, float newPrice) {

        Float oldPrice = null;
        if (previousPrices.containsKey(eventKey)) {
            oldPrice = previousPrices.get(eventKey).getOldPriceAndPutNewPrice(newPrice);
        } else {
            previousPrices.put(eventKey, new Price(newPrice));
        }

        return oldPrice;
    }
}
