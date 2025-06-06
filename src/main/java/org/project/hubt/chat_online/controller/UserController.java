package org.project.hubt.chat_online.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.hubt.chat_online.dto.ApiResponse;
import org.project.hubt.chat_online.dto.LoginRequest;
import org.project.hubt.chat_online.entity.User;
import org.project.hubt.chat_online.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<User>> register(@Valid @RequestBody User user) {
    ApiResponse<User> response = userService.registerUser(user);
    HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<User>> login(@Valid @RequestBody LoginRequest loginRequest) {
    ApiResponse<User> response = userService.loginUser(loginRequest);
    HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
    return ResponseEntity.status(status).body(response);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
    ApiResponse<List<User>> response = userService.getAllUsers();
    HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
    return ResponseEntity.status(status).body(response);
  }
}
