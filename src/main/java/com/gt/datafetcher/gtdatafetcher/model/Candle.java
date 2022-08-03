package com.gt.datafetcher.gtdatafetcher.model;

import lombok.*;
import org.bson.types.Decimal128;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Candle {

    @Id
    private String id;

    private long open_time;
    private long close_time;
    @Field(targetType = FieldType.DECIMAL128)
    private Decimal128 open_price;
    @Field(targetType = FieldType.DECIMAL128)
    private Decimal128 close_price;
    @Field(targetType = FieldType.DECIMAL128)
    private Decimal128 high_price;
    @Field(targetType = FieldType.DECIMAL128)
    private Decimal128 low_price;
    @Field(targetType = FieldType.DECIMAL128)
    private Decimal128 volume;
}
