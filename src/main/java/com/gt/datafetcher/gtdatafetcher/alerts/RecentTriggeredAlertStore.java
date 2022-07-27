package com.gt.datafetcher.gtdatafetcher.alerts;

import com.gt.datafetcher.gtdatafetcher.model.Alert;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RecentTriggeredAlertStore {
    private final ConcurrentHashMap<String,RecentTriggeredAlertList> recentTriggeredAlerts;

    public RecentTriggeredAlertStore() {
        this.recentTriggeredAlerts = new ConcurrentHashMap<>();
    }

    public List<String> filterTriggeredAlerts(String eventKey, List<Alert> alerts) {
        if(this.recentTriggeredAlerts.containsKey(eventKey)) {
            return recentTriggeredAlerts.get(eventKey).filterTriggeredAlerts(alerts);
        } else {
            RecentTriggeredAlertList list = new RecentTriggeredAlertList(100);
            List<String> filteredAlerts = list.filterTriggeredAlerts(alerts);
            this.recentTriggeredAlerts.put(eventKey, list);
            return filteredAlerts;
        }

    }
}
