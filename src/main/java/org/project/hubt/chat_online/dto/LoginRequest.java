package org.project.hubt.chat_online.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
  @NotBlank(message = "Username không được để trống")
  private String username;
  
  @NotBlank(message = "Password không được để trống")
  private String password;
}