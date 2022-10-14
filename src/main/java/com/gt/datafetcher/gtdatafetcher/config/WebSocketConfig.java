package com.gt.datafetcher.gtdatafetcher.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${custom.cors.allowed_urls}")
    private String allowedOrigins;

    @Value("${custom.ws.message_queue.enable_external_broker}")
    private String enableExternalBroker;
    @Value("${custom.ws.message_queue.relay_host}")
    private String mq_relay_host;
    @Value("${custom.ws.message_queue.relay_port}")
    private String mq_relay_port;
    @Value("${custom.ws.message_queue.client_login}")
    private String mq_client_login;
    @Value("${custom.ws.message_queue.client_passcode}")
    private String mq_client_passcode;
    @Value("${custom.ws.message_queue.system_login}")
    private String mq_system_login;
    @Value("${custom.ws.message_queue.system_passcode}")
    private String mq_system_passcode;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins(allowedOrigins.split(",")).withSockJS();
    }

    @Override
    public void configureMessageBroker(@NotNull MessageBrokerRegistry registry) {
        if (enableExternalBroker.equals("true")) {
            registry.setApplicationDestinationPrefixes("/app")
                    .enableStompBrokerRelay("/queue", "/topic")
                    .setUserDestinationBroadcast("/topic/unresolved.user.dest")
                    .setUserRegistryBroadcast("/topic/registry.broadcast")
                    .setRelayHost(mq_relay_host)
                    .setRelayPort(Integer.parseInt(mq_relay_port))
                    .setClientLogin(mq_client_login)
                    .setClientPasscode(mq_client_passcode)
                    .setSystemLogin(mq_system_login)
                    .setSystemPasscode(mq_system_passcode);
        } else {
            registry.enableSimpleBroker("/topic/");
        }

    }
}
