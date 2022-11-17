package com.gt.datafetcher.gtdatafetcher.db;

import com.gt.datafetcher.gtdatafetcher.dto.Kline;
import com.gt.datafetcher.gtdatafetcher.model.Candle;
import com.mongodb.bulk.BulkWriteResult;
import org.bson.Document;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
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

        Candle candle = getCandleFromKline(kline);

        upsertCandleData(candle, eventKey);
    }

    private Candle getCandleFromKline(Kline kline) {
        return new Candle(null, kline.getOpenTime(), kline.getCloseTime(),
                Decimal128.parse(Float.toString(kline.getOpenPrice())),
                Decimal128.parse(Float.toString(kline.getClosePrice())),
                Decimal128.parse(Float.toString(kline.getHighPrice())),
                Decimal128.parse(Float.toString(kline.getLowPrice())),
                Decimal128.parse(Float.toString(kline.getVolume()))
        );
    }

    private Update getUpdateDocumentFromCandle(Candle candle) {
        Update update = new Update();
        update.set("open_time", candle.getOpen_time());
        update.set("close_time", candle.getClose_time());
        update.set("open_price", candle.getOpen_price());
        update.set("close_price", candle.getClose_price());
        update.set("high_price", candle.getHigh_price());
        update.set("low_price", candle.getLow_price());
        update.set("volume", candle.getVolume());
        return update;
    }

    private String getCollectionName(String currencyPair, String timeframe) {
        return "binance_" + currencyPair.toUpperCase(Locale.ROOT) + "_" + timeframe;
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

    public long[] getEarliestCandleTime(String currencyPair, String timeframe) {
        String collectionName = getCollectionName(currencyPair, timeframe);

        Query query = new Query();
        query.fields().include("open_time", "close_time");
        query.with(Sort.by(Sort.Direction.ASC, "open_time"));
        query.limit(1);

        List<Candle> earlyCandle = mongoTemplate.find(query, Candle.class, collectionName);
        if(earlyCandle.isEmpty()) return null;
        Candle c = earlyCandle.get(0);
        long[] array = new long[2];
        array[0] = c.getOpen_time();
        array[1] = c.getClose_time();
        return array;
    }

    public void insertManyCandles(List<Kline> klines, String currencyPair, String timeframe) {
        BulkOperations bulkUpserts = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED,
                getCollectionName(currencyPair, timeframe));

        List<Pair<Query, Update>> updates = new LinkedList<>();

        for (Kline kline: klines) {
            Candle candle = getCandleFromKline(kline);
            Query query = new Query(
                    Criteria.where("open_time").is(candle.getOpen_time())
            );
            updates.add(Pair.of(query, getUpdateDocumentFromCandle(candle)));
        }

        bulkUpserts.upsert(updates);
        try {
            BulkWriteResult result = bulkUpserts.execute();
            System.out.println("Inserted " + result.getInsertedCount() + " new entries for "
                    + currencyPair + " " + timeframe);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
