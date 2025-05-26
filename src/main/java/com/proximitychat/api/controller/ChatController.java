package com.proximitychat.api.controller;

import com.proximitychat.api.datatransfer.RegisterRequest;
import com.proximitychat.api.datatransfer.RegisterResponse;
import com.proximitychat.api.domain.User;
import com.proximitychat.api.repository.UserRepository;
import java.util.Optional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

  private final UserRepository userRepository;

  public ChatController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PostMapping("/register")
  public RegisterResponse register(@RequestBody RegisterRequest request) {
    String username = request.getUsername();
    Optional<User> optionalUser = userRepository.get(username);

    if (optionalUser.isEmpty()) {
      User user = new User(username);
      userRepository.put(user);
    }

    RegisterResponse response = new RegisterResponse();
    response.setError(null);
    response.setSuccessful(true);
    return response;
  }
}
