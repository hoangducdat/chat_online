package org.project.hubt.chat_online.repository;

import org.project.hubt.chat_online.enity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

  @Query("SELECT m FROM Message m WHERE (m.sender.id = :userId AND m.receiver.id = :friendId) "
      + "OR (m.sender.id = :friendId AND m.receiver.id = :userId) ORDER BY m.timestamp ASC")
  List<Message> findChatHistory(@Param("userId") Long userId, @Param("friendId") Long friendId);
}
