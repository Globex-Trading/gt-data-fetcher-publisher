package com.gt.datafetcher.gtdatafetcher.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.config.StompBrokerRelayRegistration;
import org.springframework.messaging.simp.stomp.StompReactorNettyCodec;
import org.springframework.messaging.tcp.reactor.ReactorNettyTcpClient;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${custom.cors.allowed_urls}")
    private String allowedOrigins;

    @Value("${custom.ws.message_queue.enable_external_broker}")
    private String enableExternalBroker;

    @Value("${custom.ws.message_queue.enable_ssl}")
    private String enableSSL;
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

            StompBrokerRelayRegistration stompBrokerRelayRegistration =
                    registry.setApplicationDestinationPrefixes("/app")
                    .enableStompBrokerRelay("/queue", "/topic")
                    .setUserDestinationBroadcast("/topic/unresolved.user.dest")
                    .setUserRegistryBroadcast("/topic/registry.broadcast");

            if (enableSSL.equals("true")) {
                stompBrokerRelayRegistration.setTcpClient(createTcpClient());
            } else {
                stompBrokerRelayRegistration.setRelayHost(mq_relay_host)
                        .setRelayPort(Integer.parseInt(mq_relay_port));
            }
            stompBrokerRelayRegistration.setClientLogin(mq_client_login)
                    .setClientPasscode(mq_client_passcode)
                    .setSystemLogin(mq_system_login)
                    .setSystemPasscode(mq_system_passcode);

        } else {
            registry.enableSimpleBroker("/topic/");
        }

    }

    private SocketAddress getAddress(){
        try {
            InetAddress address = InetAddress.getByName(mq_relay_host);
            return new InetSocketAddress(address, Integer.parseInt(mq_relay_port));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private ReactorNettyTcpClient<byte[]> createTcpClient() {
        return new ReactorNettyTcpClient<>(
                client -> client.remoteAddress(this::getAddress).secure(),
        new StompReactorNettyCodec());
    }

    @EventListener
    public void brokerAvailability(BrokerAvailabilityEvent brokerAvailabilityEvent){
        System.out.println(brokerAvailabilityEvent);
    }
}
