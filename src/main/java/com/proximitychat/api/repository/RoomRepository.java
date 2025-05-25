package com.proximitychat.api.repository;

import com.proximitychat.api.domain.Room;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class RoomRepository {

  private final Map<Long, Room> rooms = new HashMap<>();

  public Room get(Long id) {
    return rooms.get(id);
  }

  public List<Room> getAll() {
    return rooms.values().stream().toList();
  }

  public void add(Room room) {
    rooms.put(room.getId(), room);
  }

  public Room remove(Room room) {
    return rooms.remove(room.getId());
  }
}
