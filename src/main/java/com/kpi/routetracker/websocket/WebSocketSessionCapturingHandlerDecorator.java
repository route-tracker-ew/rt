package com.kpi.routetracker.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

@Slf4j
public class WebSocketSessionCapturingHandlerDecorator extends WebSocketHandlerDecorator {

    public WebSocketSessionCapturingHandlerDecorator(WebSocketHandler delegate) {
        super(delegate);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("afterConnectionEstablished");
        super.afterConnectionEstablished(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("handleMessage");
        super.handleMessage(session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("handleTransportError");
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("afterConnectionClosed");
        super.afterConnectionClosed(session, closeStatus);
    }
}
