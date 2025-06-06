package org.project.hubt.chat_online.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "friends", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "friend_id"})
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friend {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  @NotNull(message = "User không được để trống")
  private User user;

  @ManyToOne
  @JoinColumn(name = "friend_id", nullable = false)
  @NotNull(message = "Friend không được để trống")
  private User friend;
}
