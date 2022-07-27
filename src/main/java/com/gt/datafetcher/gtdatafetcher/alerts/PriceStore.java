package com.gt.datafetcher.gtdatafetcher.alerts;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PriceStore {
    private Map<String, Float> previousPrices;

    public PriceStore() {
        this.previousPrices = new HashMap<>();
    }

    public Float getOldPriceAndPutNewPrice(String eventKey, float newPrice) {
        Float oldPrice = null;
        if (previousPrices.containsKey(eventKey)) {
            oldPrice = previousPrices.get(eventKey);
        }
        previousPrices.put(eventKey, newPrice);
        return oldPrice;
    }
}
