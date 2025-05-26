package com.proximitychat.api.service;

import com.proximitychat.api.domain.User;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
    String username = message.get("username");
    String latitude = message.get("latitude");
    String longitude = message.get("longitude");
    Optional<User> optionalUser = chatService.joinChat(username, latitude, longitude, session);
    Map<String, String> response = new HashMap<>();
    response.put("status", optionalUser.isPresent() ? "SUCCESS" : "FAILURE");
    return response;
  }

  public Map<String, String> handleSendMessage(Map<String, String> message) {
    String username = message.get("username");
    String content = message.get("content");
    boolean success = chatService.sendMessage(username, content);
    Map<String, String> response = new HashMap<>();
    response.put("status", success ? "SUCCESS" : "FAILURE");
    return response;
  }

  public Map<String, String> handleLeaveMessage(Map<String, String> message) {
    String username = message.get("username");
    boolean success = chatService.leave(username);
    Map<String, String> response = new HashMap<>();
    response.put("status", success ? "SUCCESS" : "FAILURE");
    return response;
  }
}
