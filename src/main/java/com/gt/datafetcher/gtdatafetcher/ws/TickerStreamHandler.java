package com.gt.datafetcher.gtdatafetcher.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TickerStreamHandler {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Async("threadPoolTaskExecutor")
    public void publishTickerEventToTopic(String topicName, String event){
        messagingTemplate.convertAndSend("/topic/" + topicName, event);
    }
}
