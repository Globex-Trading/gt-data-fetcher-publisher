package com.gt.datafetcher.gtdatafetcher.db;

import com.gt.datafetcher.gtdatafetcher.dto.Kline;
import com.gt.datafetcher.gtdatafetcher.model.Candle;
import org.bson.Document;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class CandleDBService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Async("threadPoolTaskExecutor")
    public void asyncInsertCandleData(Kline kline, String eventKey) {
        insertCandleData(kline, eventKey);
    }

    public void insertCandleData(Kline kline, String eventKey) {

        Candle candle = new Candle(null, kline.getOpenTime(), kline.getCloseTime(),
                Decimal128.parse(Float.toString(kline.getOpenPrice())),
                Decimal128.parse(Float.toString(kline.getClosePrice())),
                Decimal128.parse(Float.toString(kline.getHighPrice())),
                Decimal128.parse(Float.toString(kline.getLowPrice())),
                Decimal128.parse(Float.toString(kline.getVolume()))
                );

        //mongoTemplate.insert(candle, eventKey);
        upsertCandleData(candle, eventKey);
    }

    private void upsertCandleData(Candle candle, String eventKey) {
        Query query = new Query(
                Criteria.where("open_time").is(candle.getOpen_time()).and("close_time").is(candle.getClose_time())
        );

        Document document = new Document();
        mongoTemplate.getConverter().write(candle, document);
        Update updateDefinition = Update.fromDocument(document);

        mongoTemplate.upsert(query, updateDefinition, Candle.class , eventKey);
    }

    public long getEarliestCandleTime(String currencyPair, String timeframe) {
        String collectionName = "binance_" + currencyPair.toUpperCase(Locale.ROOT) + "_" + timeframe;

        Query query = new Query();
        query.fields().include("open_time");
        query.with(Sort.by(Sort.Direction.ASC, "open_time"));
        query.limit(1);

        List<Candle> earlyCandle = mongoTemplate.find(query, Candle.class, collectionName);
        if(earlyCandle.isEmpty()) return -1;
        Candle c = earlyCandle.get(0);
        return c.getOpen_time();
    }
}
