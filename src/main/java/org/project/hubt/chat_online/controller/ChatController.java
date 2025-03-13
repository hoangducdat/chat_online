package org.project.hubt.chat_online.controller;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.project.hubt.chat_online.enity.Message;
import org.project.hubt.chat_online.enity.User;
import org.project.hubt.chat_online.repository.UserRepository;
import org.project.hubt.chat_online.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

  private final MessageService messageService;
  private final UserRepository userRepository;

  @MessageMapping("/sendMessage")
  @SendTo("/topic/messages")
  public Message sendMessage(Message message) {
    // Lấy thông tin User từ database dựa trên sender_id và receiver_id
    User sender = userRepository.findById(message.getSender().getId())
        .orElseThrow(() -> new RuntimeException("Sender not found"));
    User receiver = userRepository.findById(message.getReceiver().getId())
        .orElseThrow(() -> new RuntimeException("Receiver not found"));

    // Tạo đối tượng Message mới với sender và receiver hợp lệ
    Message newMessage = new Message();
    newMessage.setSender(sender);
    newMessage.setReceiver(receiver);
    newMessage.setContent(message.getContent());
    newMessage.setTimestamp(LocalDateTime.now());

    // Lưu vào database và trả về
    return messageService.saveMessage(newMessage);
  }

  @GetMapping("/history/{userId}/{friendId}")
  public ResponseEntity<List<Message>> getChatHistory(@PathVariable Long userId, @PathVariable Long friendId) {
    List<Message> messages = messageService.getChatHistory(userId, friendId);
    return ResponseEntity.ok(messages);
  }
}
