package com.proximitychat.api.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@Getter
@EqualsAndHashCode
public class User {
  private static long nextId = 1;

  private final long id;
  private final String username;
  @Setter private WebSocketSession session;
  @Setter private String latitude;
  @Setter private String longitude;
  @Setter private Long roomId;

  public User(String username) {
    this.id = nextId++;
    this.username = username;
  }

  public boolean hasRoom() {
    return roomId == null;
  }
}
