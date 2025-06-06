package org.project.hubt.chat_online.controller;

import lombok.RequiredArgsConstructor;
import org.project.hubt.chat_online.dto.ApiResponse;
import org.project.hubt.chat_online.entity.Friend;
import org.project.hubt.chat_online.entity.FriendRequest;
import org.project.hubt.chat_online.entity.User;
import org.project.hubt.chat_online.service.FriendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

  private final FriendService friendService;

  @PostMapping("/add/{userId}/{friendId}")
  public ResponseEntity<ApiResponse<Friend>> addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
    ApiResponse<Friend> response = friendService.addFriend(userId, friendId);
    HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status).body(response);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<ApiResponse<List<Friend>>> getFriendsList(@PathVariable Long userId) {
    ApiResponse<List<Friend>> response = friendService.getFriendsList(userId);
    HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status).body(response);
  }

  @GetMapping("/potential/{userId}")
  public ResponseEntity<ApiResponse<List<User>>> getPotentialFriends(@PathVariable Long userId) {
    ApiResponse<List<User>> response = friendService.getAllPotentialFriends(userId);
    HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status).body(response);
  }

  @GetMapping("/search")
  public ResponseEntity<ApiResponse<List<User>>> searchUsers(@RequestParam String q, @RequestParam Long userId) {
    ApiResponse<List<User>> response = friendService.searchUsers(q, userId);
    HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status).body(response);
  }

  @PostMapping("/request/{senderId}/{receiverId}")
  public ResponseEntity<ApiResponse<FriendRequest>> sendFriendRequest(@PathVariable Long senderId, @PathVariable Long receiverId) {
    ApiResponse<FriendRequest> response = friendService.sendFriendRequest(senderId, receiverId);
    HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status).body(response);
  }

  @GetMapping("/requests/received/{userId}")
  public ResponseEntity<ApiResponse<List<FriendRequest>>> getPendingRequests(@PathVariable Long userId) {
    ApiResponse<List<FriendRequest>> response = friendService.getPendingRequests(userId);
    HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status).body(response);
  }

  @GetMapping("/requests/sent/{userId}")
  public ResponseEntity<ApiResponse<List<FriendRequest>>> getSentRequests(@PathVariable Long userId) {
    ApiResponse<List<FriendRequest>> response = friendService.getSentRequests(userId);
    HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status).body(response);
  }

  @PostMapping("/requests/{requestId}/accept")
  public ResponseEntity<ApiResponse<Friend>> acceptFriendRequest(@PathVariable Long requestId) {
    ApiResponse<Friend> response = friendService.acceptFriendRequest(requestId);
    HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status).body(response);
  }

  @PostMapping("/requests/{requestId}/reject")
  public ResponseEntity<ApiResponse<String>> rejectFriendRequest(@PathVariable Long requestId) {
    ApiResponse<String> response = friendService.rejectFriendRequest(requestId);
    HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status).body(response);
  }

  @DeleteMapping("/{friendshipId}")
  public ResponseEntity<ApiResponse<String>> removeFriend(@PathVariable Long friendshipId) {
    ApiResponse<String> response = friendService.removeFriend(friendshipId);
    HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status).body(response);
  }
}