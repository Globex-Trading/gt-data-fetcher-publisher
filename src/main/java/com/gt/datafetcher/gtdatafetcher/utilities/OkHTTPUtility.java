package com.gt.datafetcher.gtdatafetcher.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gt.datafetcher.gtdatafetcher.dto.ProviderResponse;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OkHTTPUtility {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient okHttpClient;

    @Value("${custom.alerts.to_be_triggered_alerts_endpoint}")
    private String toBeTriggeredAlertsEndpoint;

    @Value("${custom.symbols.endpoint}")
    private String symbolsEndpoint;

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
                .url(toBeTriggeredAlertsEndpoint)
                .post(RequestBody.create(body.toString(), OkHTTPUtility.JSON))
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if(response.isSuccessful()) return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public ProviderResponse getAvailableSymbolsAndTimeframes () {
        Request request = new Request.Builder()
                .url(symbolsEndpoint)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if(response.isSuccessful()) {
                if(response.body() != null) {
                    String responseBody = response.body().string();
                    System.out.println("Received Symbols and Timeframes\n" + responseBody);

                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.readValue(responseBody, ProviderResponse.class);
                }
            } else {
                System.out.println("Error fetching Symbols from API.");
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
