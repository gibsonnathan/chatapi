package com.proximitychat.api.service;

import com.proximitychat.api.domain.Room;
import com.proximitychat.api.repository.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

  private final RoomRepository roomRepository;

  private Room globalRoom = new Room("32.4673 N", "84.9920 W");

  public RoomService(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
    roomRepository.add(globalRoom);
  }

  // TODO: work out how to see if we need a new room or
  //   if we can use an existing
  public Room getRoom(String longitude, String latitude) {
    return globalRoom;
  }

  public Room getRoom(Long id) {
    return roomRepository.get(id);
  }
}
