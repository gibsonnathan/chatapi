package com.proximitychat.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proximitychat.api.domain.Room;
import com.proximitychat.api.domain.User;
import com.proximitychat.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Service
public class ChatService {

  private final RoomService roomService;
  private final UserRepository userRepository;
  private final ObjectMapper objectMapper;

  public ChatService(RoomService roomService, UserRepository userRepository, ObjectMapper objectMapper) {
    this.roomService = roomService;
    this.userRepository = userRepository;
    this.objectMapper = objectMapper;
  }

  public User joinChat(String latitude, String longitude, WebSocketSession session) {
    User user = new User(session, latitude, longitude);
    Room room = roomService.getRoom(user.getLongitude(), user.getLatitude());
    user.setRoomId(room.getId());
    room.addUser(user);
    userRepository.add(user);
    return user;
  }

  public boolean sendMessage(Long userId, String content) {
    User user = userRepository.get(userId);
    Room room = roomService.getRoom(user.getRoomId());
    try {
      room.send(objectMapper, content);
    } catch (Exception e) {
      log.debug("Caught while sending message", e);
      return false;
    }

    return true;
  }

  public void leave(Long userId) {
    User user = userRepository.get(userId);
    Room room = roomService.getRoom(user.getRoomId());
    room.removeUser(user);
    userRepository.remove(user);
  }
}
