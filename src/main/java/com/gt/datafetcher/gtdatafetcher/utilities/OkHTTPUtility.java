package com.gt.datafetcher.gtdatafetcher.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gt.datafetcher.gtdatafetcher.dto.ProviderResponse;
import com.gt.datafetcher.gtdatafetcher.dto.ResponseBodyDTO;
import com.gt.datafetcher.gtdatafetcher.enums.Endpoints;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OkHTTPUtility {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient okHttpClient;

    @Value("${custom.gt-api.hostname}")
    private String gtAPIHostname;

    public OkHTTPUtility() {
        this.okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public boolean postAlertsToBeTriggered (List<String> alertIds) {
        JSONArray alertIdsJsonArray = new JSONArray(alertIds);
        JSONObject body = new JSONObject();
        body.put("alert_ids", alertIdsJsonArray);

        Request request = new Request.Builder()
                .url(gtAPIHostname + Endpoints.ALERT_TRIGGER_ENDPOINT.getEndpoint())
                .post(RequestBody.create(body.toString(), OkHTTPUtility.JSON))
                .build();

        ResponseBodyDTO response = sendRequest(request);
        return response != null && response.isSuccessful();

    }

    public ProviderResponse getAvailableSymbolsAndTimeframes () {
        Request request = new Request.Builder()
                .url(gtAPIHostname + Endpoints.SYMBOL_FETCH_ENDPOINT.getEndpoint())
                .build();

        ResponseBodyDTO response = sendRequest(request);
        if(response != null && response.isSuccessful()) {
            if(response.getBody() != null) {
                String responseBody = response.getBody();
                System.out.println("Received Symbols and Timeframes\n" + responseBody);

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    return objectMapper.readValue(responseBody, ProviderResponse.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        } else {
            System.out.println("Error fetching Symbols from API.");
        }
        return null;

    }

    public ResponseBodyDTO sendRequest(Request request) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            ResponseBodyDTO responseBodyDTO = new ResponseBodyDTO(response.code(), response.message());
            if (response.body() != null) responseBodyDTO.setBody(response.body().string());
            return responseBodyDTO;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
