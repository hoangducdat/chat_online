package org.project.hubt.chat_online.service;

import lombok.RequiredArgsConstructor;
import org.project.hubt.chat_online.dto.ApiResponse;
import org.project.hubt.chat_online.dto.LoginRequest;
import org.project.hubt.chat_online.entity.User;
import org.project.hubt.chat_online.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public ApiResponse<User> registerUser(User user) {
    try {
      if (userRepository.findByUsername(user.getUsername()).isPresent()) {
        return ApiResponse.error("Username đã tồn tại");
      }
      
      User savedUser = userRepository.save(user);
      return ApiResponse.success("Đăng ký thành công", savedUser);
    } catch (Exception e) {
      return ApiResponse.error("Lỗi khi đăng ký: " + e.getMessage());
    }
  }

  public ApiResponse<User> loginUser(LoginRequest loginRequest) {
    try {
      Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
      
      if (userOpt.isEmpty()) {
        return ApiResponse.error("Tài khoản không tồn tại");
      }
      
      User user = userOpt.get();
      if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
        return ApiResponse.error("Mật khẩu không chính xác");
      }
      
      return ApiResponse.success("Đăng nhập thành công", user);
    } catch (Exception e) {
      return ApiResponse.error("Lỗi hệ thống khi đăng nhập");
    }
  }

  public ApiResponse<List<User>> getAllUsers() {
    try {
      List<User> users = userRepository.findAll();
      return ApiResponse.success("Lấy danh sách người dùng thành công", users);
    } catch (Exception e) {
      return ApiResponse.error("Lỗi khi lấy danh sách người dùng");
    }
  }

  // Các method cũ để tương thích ngược (có thể xóa sau)
  @Deprecated
  public User register(User user) {
    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
      throw new RuntimeException("Username đã tồn tại");
    }
    return userRepository.save(user);
  }

  @Deprecated
  public Optional<User> login(String username, String password) {
    Optional<User> user = userRepository.findByUsername(username);
    if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
      return user;
    }
    return Optional.empty();
  }
}