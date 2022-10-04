package com.gt.datafetcher.gtdatafetcher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBodyDTO {
    private int code;
    private String message;
    private String body;

    public ResponseBodyDTO(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public boolean isSuccessful() {
        return code >= 200 && code <= 299;
    }
}
