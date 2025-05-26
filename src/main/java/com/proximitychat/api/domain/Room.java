package com.proximitychat.api.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import lombok.Getter;
import org.springframework.web.socket.TextMessage;

public class Room {

  private static long nextId = 1;

  @Getter private final long id;
  @Getter private final String latitude;
  @Getter private final String longitude;
  private List<User> users = new ArrayList<>();

  public Room(String latitude, String longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.id = nextId++;
  }

  public void send(ObjectMapper objectMapper, String content) throws Exception {
    Map<String, String> message = new HashMap<>();
    message.put("type", "message");
    message.put("content", content);
    String body = objectMapper.writeValueAsString(message);
    for (User us : users) {
      us.getSession().sendMessage(new TextMessage(body));
    }
  }

  public boolean addUser(User user) {
    if (!users.contains(user)) {
      users.add(user);
      return true;
    }
    return false;
  }

  public void removeUser(User user) {
    this.users = users.stream().filter(us -> us.getId() != user.getId()).toList();
    user.setRoomId(null);
  }

  public List<User> getUsers() {
    return Collections.unmodifiableList(users);
  }

  public int getSize() {
    return users.size();
  }
}
