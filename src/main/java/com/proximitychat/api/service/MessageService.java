package com.proximitychat.api.service;

import com.proximitychat.api.domain.Error;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class MessageService {

  private final ChatService chatService;

  public MessageService(ChatService chatService) {
    this.chatService = chatService;
  }

  public Map<String, Object> handleJoinMessage(
      Map<String, String> message, WebSocketSession session) {
    String username = message.get("username");
    String password = message.get("password");
    String latitude = message.get("latitude");
    String longitude = message.get("longitude");
    List<Error> errors = chatService.joinChat(username, password, latitude, longitude, session);
    Map<String, Object> response = new HashMap<>();
    response.put("status", errors.isEmpty() ? "SUCCESS" : "FAILURE");
    response.put("errors", errors);
    return response;
  }

  public Map<String, Object> handleSendMessage(Map<String, String> message) {
    String username = message.get("username");
    String content = message.get("content");
    List<Error> errors = chatService.sendMessage(username, content);
    Map<String, Object> response = new HashMap<>();
    response.put("status", errors.isEmpty() ? "SUCCESS" : "FAILURE");
    response.put("errors", errors);
    return response;
  }

  public Map<String, Object> handleLeaveMessage(Map<String, String> message) {
    String username = message.get("username");
    List<Error> errors = chatService.leave(username);
    Map<String, Object> response = new HashMap<>();
    response.put("status", errors.isEmpty() ? "SUCCESS" : "FAILURE");
    response.put("errors", errors);
    return response;
  }
}
