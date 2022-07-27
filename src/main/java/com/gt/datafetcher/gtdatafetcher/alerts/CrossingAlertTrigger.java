package com.gt.datafetcher.gtdatafetcher.alerts;

import com.gt.datafetcher.gtdatafetcher.db.AlertDBService;
import com.gt.datafetcher.gtdatafetcher.dto.Ticker;
import com.gt.datafetcher.gtdatafetcher.model.Alert;
import com.gt.datafetcher.gtdatafetcher.utilities.OkHTTPUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrossingAlertTrigger {
    @Autowired
    private AlertDBService alertDBService;
    @Autowired
    private RecentTriggeredAlertStore recentTriggeredAlertStore;
    @Autowired
    private OkHTTPUtility okHTTPUtility;

    @Async("threadPoolTaskExecutor")
    public void triggerCrossingAlerts(Ticker ticker, String eventKey, Float oldPrice) {
        if (oldPrice == null || oldPrice.equals(ticker.getLastPrice()))
            return;

        //System.out.println(eventKey + " - " + ticker.getLastPrice() + "," + oldPrice);

        //Find alerts
        List<Alert> alertList =
                alertDBService.getAlertsForAPriceRange(ticker.getProvider(), ticker.getSymbol(),
                        oldPrice, ticker.getLastPrice());
        //Check whether they have already been triggered and Filter them
        List<String> filteredAlerts = recentTriggeredAlertStore.filterTriggeredAlerts(eventKey, alertList);

        if (filteredAlerts.isEmpty()) return;

        if (okHTTPUtility.postAlertsToBeTriggered(filteredAlerts)) {
            System.out.println("Successfully posted the alert IDs for triggering.");
        } else {
            System.out.println("Error in posting the alert IDs - " + filteredAlerts);
        }

    }
}
