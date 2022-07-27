package com.gt.datafetcher.gtdatafetcher.alerts;

import java.util.*;

import com.gt.datafetcher.gtdatafetcher.model.Alert;
import org.apache.commons.collections4.queue.CircularFifoQueue;

public class RecentTriggeredAlertList {
    private Set<String> triggeredAlertsSet;
    private CircularFifoQueue<String> triggeredAlertsQueue;

    public RecentTriggeredAlertList() {
        this(100);
    }

    public RecentTriggeredAlertList(int initialCapacity) {
        this.triggeredAlertsSet = new HashSet<>(initialCapacity);
        this.triggeredAlertsQueue = new CircularFifoQueue<>(initialCapacity);
    }

    public synchronized List<String> filterTriggeredAlerts(List<Alert> alerts) {
        List<String> filteredAlerts = new LinkedList<>();
        for (Alert a: alerts) {
            if(!this.triggeredAlertsSet.contains(a.getId())) {
                filteredAlerts.add(a.getId());
                if(this.triggeredAlertsQueue.isAtFullCapacity()) {
                    this.triggeredAlertsSet.remove(this.triggeredAlertsQueue.peek());
                }
                this.triggeredAlertsSet.add(a.getId());
                this.triggeredAlertsQueue.add(a.getId());
            }
        }
        return filteredAlerts;
    }
}
