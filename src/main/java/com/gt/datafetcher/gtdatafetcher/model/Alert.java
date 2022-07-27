package com.gt.datafetcher.gtdatafetcher.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class Alert {
    @Id
    private String id;
}
