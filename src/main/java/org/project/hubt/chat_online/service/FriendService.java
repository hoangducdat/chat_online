package org.project.hubt.chat_online.service;

import lombok.RequiredArgsConstructor;
import org.project.hubt.chat_online.dto.ApiResponse;
import org.project.hubt.chat_online.entity.Friend;
import org.project.hubt.chat_online.entity.FriendRequest;
import org.project.hubt.chat_online.entity.User;
import org.project.hubt.chat_online.repository.FriendRepository;
import org.project.hubt.chat_online.repository.FriendRequestRepository;
import org.project.hubt.chat_online.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendService {

  private final FriendRepository friendRepository;
  private final FriendRequestRepository friendRequestRepository;
  private final UserRepository userRepository;

  public ApiResponse<Friend> addFriend(Long userId, Long friendId) {
    try {
      User user = userRepository.findById(userId)
          .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
      User friendUser = userRepository.findById(friendId)
          .orElseThrow(() -> new RuntimeException("Bạn bè không tồn tại"));

      List<Friend> existingFriends = friendRepository.findByUserId(userId);
      boolean alreadyFriends = existingFriends.stream()
          .anyMatch(f -> f.getFriend().getId().equals(friendId));

      if (alreadyFriends) {
        return ApiResponse.error("Đã là bạn bè rồi");
      }

      Friend friendship = new Friend();
      friendship.setUser(user);
      friendship.setFriend(friendUser);

      Friend savedFriendship = friendRepository.save(friendship);
      return ApiResponse.success("Thêm bạn thành công", savedFriendship);
    } catch (Exception e) {
      return ApiResponse.error("Lỗi khi thêm bạn: " + e.getMessage());
    }
  }

  public ApiResponse<List<Friend>> getFriendsList(Long userId) {
    try {
      if (!userRepository.existsById(userId)) {
        return ApiResponse.error("Người dùng không tồn tại");
      }

      List<Friend> friends = friendRepository.findByUserId(userId);
      return ApiResponse.success("Lấy danh sách bạn bè thành công", friends);
    } catch (Exception e) {
      return ApiResponse.error("Lỗi khi lấy danh sách bạn bè: " + e.getMessage());
    }
  }

  public ApiResponse<List<User>> getAllPotentialFriends(Long currentUserId) {
    try {
      List<User> allUsers = userRepository.findAll();
      List<User> potentialFriends = allUsers.stream()
          .filter(user -> !user.getId().equals(currentUserId))
          .toList();
      
      return ApiResponse.success("Lấy danh sách người dùng thành công", potentialFriends);
    } catch (Exception e) {
      return ApiResponse.error("Lỗi khi lấy danh sách người dùng: " + e.getMessage());
    }
  }

  public ApiResponse<String> removeFriend(Long friendshipId) {
    try {
      if (!friendRepository.existsById(friendshipId)) {
        return ApiResponse.error("Mối quan hệ bạn bè không tồn tại");
      }

      friendRepository.deleteById(friendshipId);
      return ApiResponse.success("Xóa bạn thành công", null);
    } catch (Exception e) {
      return ApiResponse.error("Lỗi khi xóa bạn: " + e.getMessage());
    }
  }

  public ApiResponse<List<User>> searchUsers(String searchTerm, Long currentUserId) {
    try {
      if (searchTerm == null || searchTerm.trim().isEmpty()) {
        return ApiResponse.error("Từ khóa tìm kiếm không được để trống");
      }

      List<User> users = userRepository.searchByUsernameOrEmail(searchTerm.trim());
      
      List<User> filteredUsers = users.stream()
          .filter(user -> !user.getId().equals(currentUserId))
          .toList();

      return ApiResponse.success("Tìm kiếm thành công", filteredUsers);
    } catch (Exception e) {
      return ApiResponse.error("Lỗi khi tìm kiếm: " + e.getMessage());
    }
  }

  public ApiResponse<FriendRequest> sendFriendRequest(Long senderId, Long receiverId) {
    try {
      User sender = userRepository.findById(senderId)
          .orElseThrow(() -> new RuntimeException("Người gửi không tồn tại"));
      User receiver = userRepository.findById(receiverId)
          .orElseThrow(() -> new RuntimeException("Người nhận không tồn tại"));

      List<Friend> existingFriends = friendRepository.findByUserId(senderId);
      boolean alreadyFriends = existingFriends.stream()
          .anyMatch(f -> f.getFriend().getId().equals(receiverId));
      
      if (alreadyFriends) {
        return ApiResponse.error("Đã là bạn bè rồi");
      }

      Optional<FriendRequest> existingRequest = friendRequestRepository
          .findBySenderIdAndReceiverId(senderId, receiverId);
      
      if (existingRequest.isPresent()) {
        FriendRequest request = existingRequest.get();
        if (request.getStatus() == FriendRequest.RequestStatus.PENDING) {
          return ApiResponse.error("Đã gửi lời mời kết bạn rồi");
        }
      }

      Optional<FriendRequest> reverseRequest = friendRequestRepository
          .findBySenderIdAndReceiverId(receiverId, senderId);
      
      if (reverseRequest.isPresent() && 
          reverseRequest.get().getStatus() == FriendRequest.RequestStatus.PENDING) {
        return ApiResponse.error("Người này đã gửi lời mời cho bạn rồi");
      }

      FriendRequest friendRequest = new FriendRequest();
      friendRequest.setSender(sender);
      friendRequest.setReceiver(receiver);
      friendRequest.setStatus(FriendRequest.RequestStatus.PENDING);

      FriendRequest savedRequest = friendRequestRepository.save(friendRequest);
      return ApiResponse.success("Gửi lời mời kết bạn thành công", savedRequest);
    } catch (Exception e) {
      return ApiResponse.error("Lỗi khi gửi lời mời: " + e.getMessage());
    }
  }

  public ApiResponse<List<FriendRequest>> getPendingRequests(Long userId) {
    try {
      List<FriendRequest> requests = friendRequestRepository
          .findByReceiverIdAndStatus(userId, FriendRequest.RequestStatus.PENDING);
      
      return ApiResponse.success("Lấy danh sách yêu cầu thành công", requests);
    } catch (Exception e) {
      return ApiResponse.error("Lỗi khi lấy danh sách yêu cầu: " + e.getMessage());
    }
  }

  public ApiResponse<List<FriendRequest>> getSentRequests(Long userId) {
    try {
      List<FriendRequest> requests = friendRequestRepository
          .findBySenderIdAndStatus(userId, FriendRequest.RequestStatus.PENDING);
      
      return ApiResponse.success("Lấy danh sách lời mời đã gửi thành công", requests);
    } catch (Exception e) {
      return ApiResponse.error("Lỗi khi lấy danh sách lời mời đã gửi: " + e.getMessage());
    }
  }

  public ApiResponse<Friend> acceptFriendRequest(Long requestId) {
    try {
      FriendRequest request = friendRequestRepository.findById(requestId)
          .orElseThrow(() -> new RuntimeException("Yêu cầu kết bạn không tồn tại"));

      if (request.getStatus() != FriendRequest.RequestStatus.PENDING) {
        return ApiResponse.error("Yêu cầu đã được xử lý rồi");
      }

      request.setStatus(FriendRequest.RequestStatus.ACCEPTED);
      friendRequestRepository.save(request);

      Friend friendship1 = new Friend();
      friendship1.setUser(request.getSender());
      friendship1.setFriend(request.getReceiver());

      Friend friendship2 = new Friend();
      friendship2.setUser(request.getReceiver());
      friendship2.setFriend(request.getSender());

      friendRepository.save(friendship1);
      friendRepository.save(friendship2);

      return ApiResponse.success("Chấp nhận kết bạn thành công", friendship1);
    } catch (Exception e) {
      return ApiResponse.error("Lỗi khi chấp nhận kết bạn: " + e.getMessage());
    }
  }

  public ApiResponse<String> rejectFriendRequest(Long requestId) {
    try {
      FriendRequest request = friendRequestRepository.findById(requestId)
          .orElseThrow(() -> new RuntimeException("Yêu cầu kết bạn không tồn tại"));

      if (request.getStatus() != FriendRequest.RequestStatus.PENDING) {
        return ApiResponse.error("Yêu cầu đã được xử lý rồi");
      }

      request.setStatus(FriendRequest.RequestStatus.REJECTED);
      friendRequestRepository.save(request);

      return ApiResponse.success("Từ chối kết bạn thành công", null);
    } catch (Exception e) {
      return ApiResponse.error("Lỗi khi từ chối kết bạn: " + e.getMessage());
    }
  }
}