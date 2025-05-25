package com.proximitychat.api.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proximitychat.api.service.MessageService;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
public class WsHandler extends TextWebSocketHandler {

  private final ObjectMapper objectMapper;
  private final MessageService messageService;

  public WsHandler(ObjectMapper objectMapper, MessageService messageService) {
    this.objectMapper = objectMapper;
    this.messageService = messageService;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    log.debug("Connection established: " + session.getId());
  }

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage messageString)
      throws Exception {
    String payload = messageString.getPayload();
    log.debug("Message received: " + payload);

    HashMap<String, String> message = objectMapper.readValue(payload, HashMap.class);

    String type = message.get("type");

    Map<String, String> response =
        switch (type) {
          case "JOIN" -> messageService.handleJoinMessage(message, session);
          case "SEND" -> messageService.handleSendMessage(message);
          case "LEAVE" -> messageService.handleLeaveMessage(message);
          default -> throw new IllegalStateException("Unexpected value: " + type);
        };

    String responseString = objectMapper.writeValueAsString(response);
    session.sendMessage(new TextMessage(responseString));
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    log.debug("Connection closed: " + session.getId());
  }
}
