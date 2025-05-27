package com.proximitychat.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proximitychat.api.domain.Error;
import com.proximitychat.api.domain.Room;
import com.proximitychat.api.domain.User;
import com.proximitychat.api.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Service
public class ChatService {

  private final RoomService roomService;
  private final UserRepository userRepository;
  private final ObjectMapper objectMapper;
  private final PasswordEncoder passwordEncoder;

  public ChatService(
      RoomService roomService,
      UserRepository userRepository,
      ObjectMapper objectMapper,
      PasswordEncoder passwordEncoder) {
    this.roomService = roomService;
    this.userRepository = userRepository;
    this.objectMapper = objectMapper;
    this.passwordEncoder = passwordEncoder;
  }

  public List<com.proximitychat.api.domain.Error> joinChat(
      String username,
      String password,
      String latitude,
      String longitude,
      WebSocketSession session) {
    Optional<User> optionalUser = userRepository.get(username);
    List<Error> errors = new ArrayList<>();

    if (optionalUser.isEmpty()) {
      errors.add(new Error("User does not exist."));
      return errors;
    }

    User user = optionalUser.get();

    if (!passwordEncoder.matches(password, user.getPasswordHash())) {
      errors.add(new Error("Wrong password."));
      return errors;
    }

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
    return errors;
  }

  public List<com.proximitychat.api.domain.Error> sendMessage(String username, String content) {
    List<Error> errors = new ArrayList<>();
    Optional<User> optionalUser = userRepository.get(username);

    if (optionalUser.isEmpty()) {
      errors.add(new Error("User does not exist."));
      return errors;
    }

    User user = optionalUser.get();
    Long roomId = user.getRoomId();

    if (roomId == null) {
      errors.add(new Error("User's room is null."));
      return errors;
    }

    Room room = roomService.getRoom(roomId);
    try {
      room.send(objectMapper, content);
    } catch (Exception e) {
      log.debug("Caught while sending message", e);
      errors.add(new Error("Failed to send message."));
      return errors;
    }

    return errors;
  }

  public List<com.proximitychat.api.domain.Error> leave(String username) {
    List<Error> errors = new ArrayList<>();
    Optional<User> optionalUser = userRepository.get(username);
    if (optionalUser.isEmpty()) {
      errors.add(new Error("User does not exist."));
      return errors;
    }
    User user = optionalUser.get();
    Long roomId = user.getRoomId();
    if (roomId == null) {
      errors.add(new Error("User's room is null."));
      return errors;
    }
    Room room = roomService.getRoom(roomId);
    room.removeUser(user);
    userRepository.put(user);
    return errors;
  }
}
