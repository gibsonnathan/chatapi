package com.proximitychat.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proximitychat.api.domain.Room;
import com.proximitychat.api.domain.User;
import com.proximitychat.api.repository.UserRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Service
public class ChatService {

  private final RoomService roomService;
  private final UserRepository userRepository;
  private final ObjectMapper objectMapper;

  public ChatService(
      RoomService roomService, UserRepository userRepository, ObjectMapper objectMapper) {
    this.roomService = roomService;
    this.userRepository = userRepository;
    this.objectMapper = objectMapper;
  }

  public Optional<User> joinChat(
      String username, String latitude, String longitude, WebSocketSession session) {
    Optional<User> optionalUser = userRepository.get(username);

    if (optionalUser.isEmpty()) {
      return Optional.empty();
    }

    User user = optionalUser.get();
    user.setLatitude(latitude);
    user.setLongitude(longitude);
    user.setSession(session);
    Room newRoom = roomService.getRoom(longitude, latitude);
    if (user.hasRoom() && user.getRoomId() != newRoom.getId()) {
      Room oldRoom = roomService.getRoom(user.getRoomId());
      oldRoom.removeUser(user);
    }
    user.setRoomId(newRoom.getId());
    newRoom.addUser(user);
    userRepository.put(user);
    return Optional.of(user);
  }

  public boolean sendMessage(String username, String content) {
    Optional<User> optionalUser = userRepository.get(username);

    if (optionalUser.isEmpty()) {
      return false;
    }

    User user = optionalUser.get();
    Long roomId = user.getRoomId();

    if (roomId == null) {
      return false;
    }

    Room room = roomService.getRoom(roomId);
    try {
      room.send(objectMapper, content);
    } catch (Exception e) {
      log.debug("Caught while sending message", e);
      return false;
    }

    return true;
  }

  public boolean leave(String username) {
    Optional<User> optionalUser = userRepository.get(username);
    if (optionalUser.isEmpty()) {
      return false;
    }
    User user = optionalUser.get();
    Long roomId = user.getRoomId();
    if (roomId == null) {
      return false;
    }
    Room room = roomService.getRoom(roomId);
    room.removeUser(user);
    userRepository.put(user);
    return true;
  }
}
