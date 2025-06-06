package org.project.hubt.chat_online.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.hubt.chat_online.dto.ApiResponse;
import org.project.hubt.chat_online.entity.Message;
import org.project.hubt.chat_online.service.MessageService;
import org.springframework.http.HttpStatus;
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

  @MessageMapping("/sendMessage")
  @SendTo("/topic/messages")
  public Message sendMessage(@Valid Message message) {
    return messageService.processAndSaveMessage(message);
  }

  @GetMapping("/history/{userId}/{friendId}")
  public ResponseEntity<ApiResponse<List<Message>>> getChatHistory(@PathVariable Long userId, @PathVariable Long friendId) {
    ApiResponse<List<Message>> response = messageService.getChatHistoryWithValidation(userId, friendId);
    HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status).body(response);
  }
}
