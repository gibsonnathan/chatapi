package com.proximitychat.api.service;

import com.proximitychat.api.domain.User;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class MessageService {

  private final ChatService chatService;

  public MessageService(ChatService chatService) {
    this.chatService = chatService;
  }

  public Map<String, String> handleJoinMessage(
      Map<String, String> message, WebSocketSession session) {
    String latitude = message.get("latitude");
    String longitude = message.get("longitude");
    User user = chatService.joinChat(latitude, longitude, session);
    Map<String, String> response = new HashMap<>();
    response.put("status", "SUCCESS");
    response.put("client_id", String.valueOf(user.getId()));
    return response;
  }

  public Map<String, String> handleSendMessage(Map<String, String> message) {
    Long userId = Long.valueOf(message.get("user_id"));
    String content = message.get("content");
    boolean success = chatService.sendMessage(userId, content);
    Map<String, String> response = new HashMap<>();
    response.put("status", success ? "SUCCESS" : "FAILURE");
    return response;
  }

  public Map<String, String> handleLeaveMessage(Map<String, String> message) {
    Long userId = Long.valueOf(message.get("user_id"));
    chatService.leave(userId);
    Map<String, String> response = new HashMap<>();
    response.put("status", "SUCCESS");
    return response;
  }
}
