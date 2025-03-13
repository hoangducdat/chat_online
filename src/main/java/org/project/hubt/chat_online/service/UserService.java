package org.project.hubt.chat_online.service;

import lombok.RequiredArgsConstructor;
import org.project.hubt.chat_online.enity.User;
import org.project.hubt.chat_online.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public User register(User user) {
    return userRepository.save(user);
  }

  public Optional<User> login(String username, String password) {
    Optional<User> user = userRepository.findByUsername(username);
    if (user.isPresent() && user.get().getPassword().equals(password)) {
      return user;
    }
    return Optional.empty();
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }
}