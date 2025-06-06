package org.project.hubt.chat_online.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "friend_requests", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"sender_id", "receiver_id"})
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequest {
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

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RequestStatus status = RequestStatus.PENDING;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public enum RequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED
  }

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}