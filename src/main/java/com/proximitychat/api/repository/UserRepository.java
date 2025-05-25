package com.proximitychat.api.repository;

import com.proximitychat.api.domain.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

  private final Map<Long, User> users = new HashMap<>();

  public User get(Long id) {
    return users.get(id);
  }

  public List<User> getAll() {
    return users.values().stream().toList();
  }

  public void add(User user) {
    users.put(user.getId(), user);
  }

  public User remove(User user) {
    return users.remove(user.getId());
  }
}
