package com.gt.datafetcher.gtdatafetcher.alerts;

public class Price {

    private float oldPrice;

    public Price(float oldPrice) {
        this.oldPrice = oldPrice;
    }

    public float getOldPriceAndPutNewPrice (float newPrice) {
        float tempPrice = this.oldPrice;
        this.oldPrice = newPrice;
        return tempPrice;
    }
}
