package com.gt.datafetcher.gtdatafetcher.db;

import com.gt.datafetcher.gtdatafetcher.model.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertDBService {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Alert> getAlertsForAPriceRange(String provider, String symbol, float oldPrice, float newPrice) {
        float maxPrice = Math.max(oldPrice, newPrice);
        float minPrice = Math.min(oldPrice, newPrice);

        Query query = new Query();
        query.addCriteria(Criteria.where("provider").is(provider).and("symbol").is(symbol)
                .and("trigger_price").gte(minPrice).lte(maxPrice).and("is_triggered").is(false));
        query.fields().include("_id");

        return mongoTemplate.find(query, Alert.class, "alerts");
    }
}
