package org.project.hubt.chat_online.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "messages")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "sender_id", nullable = false)
  @NotNull(message = "Người gửi không được để trống")
  private User sender;

  @ManyToOne
  @JoinColumn(name = "receiver_id", nullable = false)
  @NotNull(message = "Người nhận không được để trống")
  private User receiver;

  @Column(nullable = false)
  @NotBlank(message = "Nội dung tin nhắn không được để trống")
  @Size(max = 1000, message = "Tin nhắn không được quá 1000 ký tự")
  private String content;

  @Column(name = "timestamp")
  private LocalDateTime timestamp;

  @PrePersist
  protected void onCreate() {
    this.timestamp = LocalDateTime.now();
  }
}
