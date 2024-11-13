package com.kpi.routetracker.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/route-tracker/socket/authorized")
                .setAllowedOriginPatterns("http://*", "https://*")
                .withSockJS();
        registry.addEndpoint("/route-tracker/socket/anonymous")
                .setAllowedOriginPatterns("http://*", "https://*")
                .withSockJS();
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(
                WebSocketSessionCapturingHandlerDecorator::new);
    }
}
