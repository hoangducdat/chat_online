package org.project.hubt.chat_online.controller;

import lombok.RequiredArgsConstructor;
import org.project.hubt.chat_online.enity.User;
import org.project.hubt.chat_online.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<User> register(@RequestBody User user) {
    return ResponseEntity.ok(userService.register(user));
  }

  @PostMapping("/login")
  public ResponseEntity<User> login(@RequestBody User user) {
    Optional<User> loggedInUser = userService.login(user.getUsername(), user.getPassword());
    return loggedInUser.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(401).build());
  }

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }
}
