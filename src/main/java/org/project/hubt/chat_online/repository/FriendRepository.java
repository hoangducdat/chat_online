package org.project.hubt.chat_online.repository;

import org.project.hubt.chat_online.enity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
  List<Friend> findByUserId(Long userId);
}
