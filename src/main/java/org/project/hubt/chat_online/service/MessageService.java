package org.project.hubt.chat_online.service;

import lombok.RequiredArgsConstructor;
import org.project.hubt.chat_online.dto.ApiResponse;
import org.project.hubt.chat_online.entity.Message;
import org.project.hubt.chat_online.entity.User;
import org.project.hubt.chat_online.repository.MessageRepository;
import org.project.hubt.chat_online.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

  private final MessageRepository messageRepository;
  private final UserRepository userRepository;

  public Message processAndSaveMessage(Message message) {
    try {
      User sender = userRepository.findById(message.getSender().getId())
          .orElseThrow(() -> new RuntimeException("Người gửi không tồn tại"));
      User receiver = userRepository.findById(message.getReceiver().getId())
          .orElseThrow(() -> new RuntimeException("Người nhận không tồn tại"));

      Message newMessage = new Message();
      newMessage.setSender(sender);
      newMessage.setReceiver(receiver);
      newMessage.setContent(message.getContent());
      newMessage.setTimestamp(LocalDateTime.now());

      return messageRepository.save(newMessage);
    } catch (Exception e) {
      throw new RuntimeException("Lỗi khi gửi tin nhắn: " + e.getMessage());
    }
  }

  public ApiResponse<List<Message>> getChatHistoryWithValidation(Long userId, Long friendId) {
    try {
      if (!userRepository.existsById(userId)) {
        return ApiResponse.error("Người dùng không tồn tại");
      }
      if (!userRepository.existsById(friendId)) {
        return ApiResponse.error("Bạn bè không tồn tại");
      }

      List<Message> messages = messageRepository.findChatHistory(userId, friendId);
      return ApiResponse.success("Lấy lịch sử chat thành công", messages);
    } catch (Exception e) {
      return ApiResponse.error("Lỗi khi lấy lịch sử chat: " + e.getMessage());
    }
  }

  @Deprecated
  public Message saveMessage(Message message) {
    return messageRepository.save(message);
  }

  @Deprecated
  public List<Message> getChatHistory(Long userId, Long friendId) {
    return messageRepository.findChatHistory(userId, friendId);
  }
}
