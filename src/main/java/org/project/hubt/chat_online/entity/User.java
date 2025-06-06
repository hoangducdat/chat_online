package org.project.hubt.chat_online.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  @NotBlank(message = "Username không được để trống")
  @Size(min = 3, max = 50, message = "Username phải từ 3-50 ký tự")
  private String username;

  @Column(nullable = false)
  @NotBlank(message = "Password không được để trống")
  @Size(min = 6, message = "Password phải ít nhất 6 ký tự")
  private String password;

  @Column(nullable = false, unique = true)
  @NotBlank(message = "Email không được để trống")
  @Email(message = "Email không hợp lệ")
  private String email;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "avatar")
  private String avatar;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    if (this.password != null && !this.password.startsWith("$2a$")) {
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      this.password = encoder.encode(this.password);
    }
  }
}