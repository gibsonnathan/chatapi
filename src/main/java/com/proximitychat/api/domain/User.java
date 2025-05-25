package com.proximitychat.api.domain;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

@Data
public class User {
  private static long nextId = 1;

  private long id;
  private WebSocketSession session;
  private String latitude;
  private String longitude;
  private Long roomId;

  public User(WebSocketSession session, String latitude, String longitude) {
    this.id = nextId++;
    this.session = session;
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
