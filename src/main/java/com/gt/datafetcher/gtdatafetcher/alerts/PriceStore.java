package com.gt.datafetcher.gtdatafetcher.alerts;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PriceStore {
    private final Map<String, Price> previousPrices;

    public PriceStore() {
        this.previousPrices = new ConcurrentHashMap<>();
    }

    public Float getOldPriceAndPutNewPrice(String eventKey, float newPrice) {

        Float oldPrice = null;
        if (previousPrices.containsKey(eventKey)) {
            oldPrice = previousPrices.get(eventKey).getOldPriceAndPutNewPrice(newPrice);
        } else {
            previousPrices.put(eventKey, new Price(newPrice));
        }
        /*System.out.println("Accessed by thread name - " + Thread.currentThread().getId() + "\n" + eventKey
        + "\nold price - " + oldPrice + "\nnew price - " + newPrice);*/
        return oldPrice;
    }
}
