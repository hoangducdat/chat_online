package org.project.hubt.chat_online.service;

import lombok.RequiredArgsConstructor;
import org.project.hubt.chat_online.enity.Message;
import org.project.hubt.chat_online.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

  private final MessageRepository messageRepository;

  public Message saveMessage(Message message) {
    return messageRepository.save(message);
  }

  public List<Message> getChatHistory(Long userId, Long friendId) {
    return messageRepository.findChatHistory(userId, friendId);
  }
}
