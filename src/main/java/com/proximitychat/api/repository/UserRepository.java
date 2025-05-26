package com.proximitychat.api.repository;

import com.proximitychat.api.domain.User;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

  private final Map<String, User> usersByUsername = new HashMap<>();

  public Optional<User> get(String username) {
    return Optional.ofNullable(usersByUsername.get(username));
  }

  public void put(User user) {
    usersByUsername.put(user.getUsername(), user);
  }
}
